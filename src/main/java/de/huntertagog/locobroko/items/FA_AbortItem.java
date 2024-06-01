package de.huntertagog.locobroko.items;

import de.huntertagog.locobroko.db.DatabaseManager;
import de.huntertagog.locobroko.renders.MainGui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class FA_AbortItem extends AbstractItem {

    DatabaseManager database = new DatabaseManager();

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.RED_CONCRETE).setDisplayName("§6Freundschaftsanfrage §cablehnen");
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        if (clickType.isLeftClick()) {
            database.denyFriendRequest(player);
            MainGui.openGui(player);
        }
    }
}
