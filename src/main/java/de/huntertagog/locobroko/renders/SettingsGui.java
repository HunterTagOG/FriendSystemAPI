package de.huntertagog.locobroko.renders;

import de.huntertagog.locobroko.FriendSystemPlugin;
import de.huntertagog.locobroko.db.DatabaseManager;
import de.huntertagog.locobroko.items.*;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.structure.Structure;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.sql.SQLException;

public class SettingsGui {

    public void openGui(Player player) throws SQLException {

        String name = FriendSystemPlugin.instance.getMessages().getString("SettingsGui.name", "§cEinstellungen");

        Window window = Window.merged()
                .setViewer(player)
                .setTitle(name)
                .setGui(getSettingsGui(player))
                .build();
        window.open();
    }

    public Gui getSettingsGui(Player player) {
        DatabaseManager database = new DatabaseManager();

        String toggleinvites_true = FriendSystemPlugin.instance.getMessages().getString("SettingsGui.toggleinvites_true", "§r§6Freundesanfragen sind §aaktiviert");
        String toggleinvites_false = FriendSystemPlugin.instance.getMessages().getString("SettingsGui.toggleinvites_false", "§r§6Freundesanfragen sind §cdeaktiviert");
        String togglenotify_true = FriendSystemPlugin.instance.getMessages().getString("SettingsGui.togglenotify_true", "§r§6Join/Leave-Nachrichten sind §aaktiviert");
        String togglenotify_false = FriendSystemPlugin.instance.getMessages().getString("SettingsGui.togglenotify_false", "§r§6Join/Leave-Nachrichten sind §cdeaktiviert");
        String togglejump_true = FriendSystemPlugin.instance.getMessages().getString("SettingsGui.togglejump_true", "§r§6Teleportanfragen sind §aaktiviert");
        String togglejump_false = FriendSystemPlugin.instance.getMessages().getString("SettingsGui.togglejump_false", "§r§6Teleportanfragen sind §cdeaktiviert");
        String togglemsgs_true = FriendSystemPlugin.instance.getMessages().getString("SettingsGui.togglemsgs_true", "§r§6Nachrichten sind §aaktiviert");
        String togglemsgs_false = FriendSystemPlugin.instance.getMessages().getString("SettingsGui.togglemsgs_false", "§r§6Nachrichten sind §cdeaktiviert");

        SimpleItem toogleFA;
        SimpleItem toogleNO;
        SimpleItem toogleTP;
        SimpleItem toogleMSG;

        if (database.getCurrentToggle(player.getName(), "toogleinvites")) {
            toogleFA = new SimpleItem(new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName(toggleinvites_true));
        } else {
            toogleFA = new SimpleItem(new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(toggleinvites_false));
        }

        if (database.getCurrentToggle(player.getName(), "tooglenotify")) {
            toogleNO = new SimpleItem(new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName(togglenotify_true));
        } else {
            toogleNO = new SimpleItem(new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(togglenotify_false));
        }

        if (database.getCurrentToggle(player.getName(), "tooglejump")) {
            toogleTP = new SimpleItem(new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName(togglejump_true));
        } else {
            toogleTP = new SimpleItem(new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(togglejump_false));
        }

        if (database.getCurrentToggle(player.getName(), "tooglemsgs")) {
            toogleMSG = new SimpleItem(new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName(togglemsgs_true));
        } else {
            toogleMSG = new SimpleItem(new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(togglemsgs_false));
        }

        // create a structure
        Structure structure = new Structure(
                "# # # # # # # # #",
                "# # I J N T S # #",
                "# # G K L M # # #",
                "# # # # # # # # #",
                "# # # # h # # # #")
                .addIngredient('#', new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("§r"))
                .addIngredient('S', new StatusItem())
                .addIngredient('T', new ToogleTeleportItem())
                .addIngredient('N', new ToogleMessageItem())
                .addIngredient('J', new ToogleNotifyItem())
                .addIngredient('I', new ToogleRequesItem())
                .addIngredient('h', new HomeItem())
                .addIngredient('G', toogleFA)
                .addIngredient('K', toogleNO)
                .addIngredient('L', toogleMSG)
                .addIngredient('M', toogleTP);

        return Gui.normal().setStructure(structure).build();
    }
}
