package de.huntertagog.locobroko.items;

import de.huntertagog.locobroko.FriendSystemPlugin;
import de.huntertagog.locobroko.utils.HeadDatabaseManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.PageItem;

public class ForwardItem extends PageItem {

    public ForwardItem() {
        super(true);
    }

    HeadDatabaseManager headDatabaseManager = new HeadDatabaseManager();

    String name = FriendSystemPlugin.instance.getMessages().getString("Item_Right.name", "Nach Rechts");
    String id = FriendSystemPlugin.instance.getMessages().getString("Item_Right.id", "10675");

    @Override
    public ItemProvider getItemProvider(PagedGui<?> gui) {

        return new ItemBuilder(headDatabaseManager.getItem(id, 1,
                Component.text(name)
                        .decoration(TextDecoration.ITALIC, false)
                        .color(NamedTextColor.YELLOW)));
    }
}
