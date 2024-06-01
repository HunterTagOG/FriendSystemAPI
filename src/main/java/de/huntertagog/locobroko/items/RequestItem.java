package de.huntertagog.locobroko.items;

import de.huntertagog.locobroko.FriendSystemPlugin;
import de.huntertagog.locobroko.db.DatabaseManager;
import de.huntertagog.locobroko.renders.RequestGui;
import de.huntertagog.locobroko.renders.SettingsGui;
import de.huntertagog.locobroko.utils.HeadDatabaseManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RequestItem extends AbstractItem {

    private final Player player;

    public RequestItem(Player player) {
        this.player = player;
    }

    HeadDatabaseManager headDatabaseManager = new HeadDatabaseManager();
    DatabaseManager db = new DatabaseManager();

    String name = FriendSystemPlugin.instance.getMessages().getString("Request_Item.name", "Freundschaftsanfragen");
    String anfragen = FriendSystemPlugin.instance.getMessages().getString("Request_Item.anfragen", "Anfragen");

    @Override
    public ItemProvider getItemProvider() {
        List<Component> MainHead2 = new ArrayList<>();
        MainHead2.add(Component.empty());
        MainHead2.add(Component.text(anfragen)
                .color(NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false));
        MainHead2.add(Component.text("-> " + db.getFriendRequestCount(db.getUserId(player.getName())))
                .color(NamedTextColor.YELLOW)
                .decoration(TextDecoration.ITALIC, false));

        return new ItemBuilder(headDatabaseManager.getItemWithLore(Material.ENCHANTED_BOOK, 1,
                Component.text(name)
                        .decoration(TextDecoration.ITALIC, false)
                        .color(NamedTextColor.AQUA), MainHead2));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        if (clickType.isLeftClick()) {
            RequestGui rg = new RequestGui();
            rg.openGui(player);
        }

    }
}
