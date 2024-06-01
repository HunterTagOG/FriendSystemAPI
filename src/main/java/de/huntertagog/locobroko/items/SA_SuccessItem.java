package de.huntertagog.locobroko.items;

import de.huntertagog.locobroko.FriendSystemPlugin;
import de.huntertagog.locobroko.db.DatabaseManager;
import de.huntertagog.locobroko.renders.SettingsGui;
import de.huntertagog.locobroko.renders.StatusGui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.sql.SQLException;

public class SA_SuccessItem extends AbstractItem {

    DatabaseManager database = new DatabaseManager();

    String name = FriendSystemPlugin.instance.getMessages().getString("SA_SuccessItem.name", "§6Statusänderung §abestätigen");

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.GREEN_CONCRETE).setDisplayName(name);
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        if (clickType.isLeftClick()) {
            String playerInputMap = StatusGui.getInstance().getPlayerInput(player.getUniqueId());
            database.setStatus(player.getName(), playerInputMap);
            SettingsGui sg = new SettingsGui();
            try {
                sg.openGui(player);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
