package de.huntertagog.locobroko.items;

import de.huntertagog.locobroko.FriendSystemPlugin;
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

public class StatusItem extends AbstractItem {

    String name = FriendSystemPlugin.instance.getMessages().getString("StatusItem.name", "§r§6Status ändern");

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.ANVIL).setDisplayName(name);
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        if (clickType.isLeftClick()) {
            try {
                StatusGui.getInstance().openGui(player);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
