package de.huntertagog.locobroko.items;

import de.huntertagog.locobroko.FriendSystemPlugin;
import de.huntertagog.locobroko.db.DatabaseManager;
import de.huntertagog.locobroko.renders.SettingsGui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.sql.SQLException;

public class ToogleMessageItem extends AbstractItem {

    DatabaseManager database = new DatabaseManager();

    String name = FriendSystemPlugin.instance.getMessages().getString("ToggleMessageItem.name", "§r§6Nachrichten aktivieren/deaktivieren");

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.PAPER).setDisplayName(name);
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        if (clickType.isLeftClick()) {
            database.setToggle(player.getName(), "tooglemsgs");
            notifyWindows();
            SettingsGui sg = new SettingsGui();
            try {
                sg.openGui(player);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
