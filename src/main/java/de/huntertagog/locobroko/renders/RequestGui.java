package de.huntertagog.locobroko.renders;

import de.huntertagog.locobroko.FriendSystemPlugin;
import de.huntertagog.locobroko.db.DatabaseManager;
import de.huntertagog.locobroko.items.*;
import de.huntertagog.locobroko.utils.FriendUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.gui.structure.Structure;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.window.Window;

import java.util.List;

public class RequestGui {

    DatabaseManager db = new DatabaseManager();
    FriendUtils friendUtils = FriendUtils.getInstance();

    String name = FriendSystemPlugin.instance.getMessages().getString("RequestGui.name", "§9Freundschaftsanfragen");

    public void openGui(Player player) {

        Structure pagestructure = new Structure(
                "x x x x x x x x x",
                "x x x x x x x x x",
                "x x x x x x x x x",
                "x x x x x x x x x",
                "x x x x x x x x x",
                "x x x x x x x x x")
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL);

        // create a structure
        Structure structure = new Structure(
                "# # # # # # # # #",
                "# # # # # # # # #",
                "# # # # # # # # #",
                "< # # # B # # # >")
                .addIngredient('#', new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("§r"))
                .addIngredient('B', new HomeItem())
                .addIngredient('<', new BackItem())
                .addIngredient('>', new ForwardItem());

        List<Item> items = friendUtils.getFriendRequests(friendUtils.getFriendRequestsNames(db.getUserId(player.getName())));

        Gui pagegui = PagedGui.items().setStructure(pagestructure).setContent(items).build();
        Gui gui = PagedGui.items().setStructure(structure).build();

        Window window = Window.split()
                .setViewer(player)
                .setTitle(name)
                .setUpperGui(pagegui)
                .setLowerGui(gui)
                .build();
        window.open();
    }
}
