package de.huntertagog.locobroko.renders;

import de.huntertagog.locobroko.FriendSystemPlugin;
import de.huntertagog.locobroko.db.DatabaseManager;
import de.huntertagog.locobroko.items.*;
import de.huntertagog.locobroko.utils.FriendUtils;
import de.huntertagog.locobroko.utils.HeadDatabaseManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.gui.structure.Structure;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.*;

public class MainGui {

    private static MainGui instance;

    private MainGui() {

    }

    public static MainGui getInstance() {
        if (instance == null) {
            instance = new MainGui();
        }
        return instance;
    }

    static DatabaseManager db = new DatabaseManager();
    static HeadDatabaseManager headDatabaseManager = new HeadDatabaseManager();

    public static void openGui(Player player) {

        String name = FriendSystemPlugin.instance.getMessages().getString("MainGui.name", "§6Gewählter Spieler §7 - §6 ");
        String online = FriendSystemPlugin.instance.getMessages().getString("MainGui.online", "Du bist online seit");
        String status = FriendSystemPlugin.instance.getMessages().getString("MainGui.status", "Dein Status");
        String friendcode = FriendSystemPlugin.instance.getMessages().getString("MainGui.friendcode", "Dein FriendCode");

        List<Component> MainHead = new ArrayList<>();
        MainHead.add(Component.empty());
        MainHead.add(Component.text(online)
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.GRAY));
        MainHead.add(Component.text("-> " + FriendUtils.getInstance().getDuration(db.getLastLogin(player.getName())))
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.YELLOW));
        MainHead.add(Component.empty());
        MainHead.add(Component.text(status)
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.GRAY));
        MainHead.add(Component.text("-> " + db.getStatus(player.getName()))
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.YELLOW));
        MainHead.add(Component.empty());
        MainHead.add(Component.text(friendcode)
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.GRAY));
        MainHead.add(Component.text("-> " + db.getFriendCode(player.getUniqueId()))
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.YELLOW));

        // create a structure
        Structure structure = new Structure(
                "# # # # # # # # #",
                "# # # # # # # # #",
                "# # # # T # # # #",
                "< # # B H R # # >")
                .addIngredient('#', new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("§r"))
                .addIngredient('T', new SortierungItem(player))
                .addIngredient('B', new RequestItem(player))
                .addIngredient('H', new SimpleItem(headDatabaseManager.getItemFromPlayer(player.getUniqueId(), 1,
                        Component.text(player.getName())
                                .decoration(TextDecoration.ITALIC, false)
                                .color(NamedTextColor.GOLD), MainHead)))
                .addIngredient('R', new SettingsItem())
                .addIngredient('<', new BackItem())
                .addIngredient('>', new ForwardItem());

        // create a structure
        Structure pagestructure = new Structure(
                "x x x x x x x x x",
                "x x x x x x x x x",
                "x x x x x x x x x",
                "x x x x x x x x x",
                "x x x x x x x x x",
                "x x x x x x x x x")
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL);

        List<Item> items = FriendUtils.getFriendItems(player);

        Gui pagegui = PagedGui.items().setStructure(pagestructure).setContent(items).build();

        Gui gui = PagedGui.items().setStructure(structure).build();

        Window window = Window.split()
                .setUpperGui(pagegui)
                .setLowerGui(gui)
                .setViewer(player)
                .setTitle(name)
                .build();
        window.open();
    }
}
