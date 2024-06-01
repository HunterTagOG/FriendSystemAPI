package de.huntertagog.locobroko.items;

import de.huntertagog.locobroko.FriendSystemPlugin;
import de.huntertagog.locobroko.db.DatabaseManager;
import de.huntertagog.locobroko.renders.FA_PlayerGui;
import de.huntertagog.locobroko.utils.FriendUtils;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FA_PlayerItem extends AbstractItem {

    private final String friendName;

    DatabaseManager db = new DatabaseManager();
    HeadDatabaseManager headDatabaseManager = new HeadDatabaseManager();
    FriendUtils friendUtils = FriendUtils.getInstance();

    String anfrage = FriendSystemPlugin.instance.getMessages().getString("FA_PlayerItem.anfragezeit", "Anfrage vor");
    String ablauf = FriendSystemPlugin.instance.getMessages().getString("FA_PlayerItem.ablaufzeit", "Läuft ab in");

    public FA_PlayerItem(String friendName) {
        this.friendName = friendName;
    }

    @Override
    public ItemProvider getItemProvider() {
        int id = db.getUserId(friendName);

        List<Component> OnlineHead = new ArrayList<>();
        OnlineHead.add(Component.empty());
        OnlineHead.add(Component.text(anfrage)
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.GRAY));
        OnlineHead.add(Component.text("-> " + friendUtils.getDuration(db.getRequestDate(id)))
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.YELLOW));
        OnlineHead.add(Component.empty());
        OnlineHead.add(Component.text(ablauf)
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.GRAY));
        OnlineHead.add(Component.text("-> " + friendUtils.getDurationInverted(db.getExpireDate(id)))
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.YELLOW));


        return new ItemBuilder(headDatabaseManager.getItemFromPlayer(UUID.fromString(db.getUUID(friendName)), 1,
                Component.text(friendName)
                        .decoration(TextDecoration.ITALIC, false)
                        .color(NamedTextColor.GRAY), OnlineHead));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        if (clickType.isLeftClick()) {
            FA_PlayerGui fpg = new FA_PlayerGui();
            fpg.openGui(player, friendName);
        }
    }
}
