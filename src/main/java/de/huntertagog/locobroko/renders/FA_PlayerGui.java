package de.huntertagog.locobroko.renders;

import de.huntertagog.locobroko.FriendSystemPlugin;
import de.huntertagog.locobroko.items.FA_AbortItem;
import de.huntertagog.locobroko.items.HomeItem;
import de.huntertagog.locobroko.items.FA_SuccessItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.structure.Structure;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.window.Window;

public class FA_PlayerGui {

    public void openGui(Player player, String friendName) {

        String name = FriendSystemPlugin.instance.getMessages().getString("FA_PlayerGui.name", "§6Gewählter Spieler §7 - §6 ");

        // create a structure
        Structure structure = new Structure(
                "B # # S # A # # #")
                .addIngredient('#', new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("§r")) // this will just create a SimpleItem with the given ItemBuilder
                .addIngredient('B', new HomeItem())
                .addIngredient('S', new FA_SuccessItem(friendName))
                .addIngredient('A', new FA_AbortItem());

        Gui gui = Gui.normal().setStructure(structure).build();

        Window window = Window.single()
                .setViewer(player)
                .setTitle(name + friendName)
                .setGui(gui)
                .build();
        window.open();
    }
}
