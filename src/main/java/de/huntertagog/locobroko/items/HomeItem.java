package de.huntertagog.locobroko.items;

import de.huntertagog.locobroko.FriendSystemPlugin;
import de.huntertagog.locobroko.renders.MainGui;
import de.huntertagog.locobroko.utils.HeadDatabaseManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class HomeItem extends AbstractItem {

    HeadDatabaseManager headDatabaseManager = new HeadDatabaseManager();

    String name = FriendSystemPlugin.instance.getMessages().getString("Home_Item.name", "Zurück");
    String id = FriendSystemPlugin.instance.getMessages().getString("Home_Item.id", "8791");

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(headDatabaseManager.getItem(id, 1,
                Component.text(name)
                        .decoration(TextDecoration.ITALIC, false)
                        .color(NamedTextColor.RED)));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        if (clickType.isLeftClick()) {
            MainGui.openGui(player);
        }
    }
}
