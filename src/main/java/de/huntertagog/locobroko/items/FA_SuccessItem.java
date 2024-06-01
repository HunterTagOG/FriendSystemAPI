package de.huntertagog.locobroko.items;

import de.huntertagog.locobroko.FriendSystemPlugin;
import de.huntertagog.locobroko.db.DatabaseManager;
import de.huntertagog.locobroko.renders.MainGui;
import de.huntertagog.locobroko.utils.FriendUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class FA_SuccessItem extends AbstractItem {

    private final String friendName;

    DatabaseManager database = new DatabaseManager();
    String name = FriendSystemPlugin.instance.getMessages().getString("FA_SuccessItem.name", "§6Freundschaftsanfrage §aakzeptieren");


    public FA_SuccessItem(String friendName) {
        this.friendName = friendName;
    }

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.GREEN_CONCRETE).setDisplayName(name);
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        if (clickType.isLeftClick()) {
            database.acceptFriendRequest(player, friendName);
            MainGui.openGui(player);
        }
    }
}
