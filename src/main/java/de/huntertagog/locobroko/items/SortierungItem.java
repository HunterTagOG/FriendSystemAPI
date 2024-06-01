package de.huntertagog.locobroko.items;

import de.huntertagog.locobroko.FriendSystemPlugin;
import de.huntertagog.locobroko.db.DatabaseManager;
import de.huntertagog.locobroko.utils.FriendUtils;
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

public class SortierungItem extends AbstractItem {

    private final Player player;
    DatabaseManager db = new DatabaseManager();
    HeadDatabaseManager headDatabaseManager = new HeadDatabaseManager();

    String name = FriendSystemPlugin.instance.getMessages().getString("SortierungsItem.name", "Sortierung");
    String sort = FriendSystemPlugin.instance.getMessages().getString("SortierungsItem.sortierung", "Aktuelle Sortierung");

    public SortierungItem(Player player) {
        this.player = player;
    }

    @Override
    public ItemProvider getItemProvider() {

        String sortierung = null;
        try {
            sortierung = String.valueOf(db.getCurrentSortMode(player.getName()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<Component> MainHead3 = new ArrayList<>();
        MainHead3.add(Component.empty());
        MainHead3.add(Component.text(sort)
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.GRAY));
        MainHead3.add(Component.text("-> " + sortierung)
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.YELLOW));

        return new ItemBuilder(headDatabaseManager.getItemWithLore(Material.HOPPER, 1,
                Component.text(name)
                        .decoration(TextDecoration.ITALIC, false)
                        .color(NamedTextColor.GOLD), MainHead3));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        if (clickType.isLeftClick()) {
            try {
                DatabaseManager.SortMode currentSortMode = db.getCurrentSortMode(player.getName());
                db.updateSortMode(player.getName(), currentSortMode);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            notifyWindows();
        }
    }
}
