package de.huntertagog.locobroko.renders;

import de.huntertagog.locobroko.FriendSystemPlugin;
import de.huntertagog.locobroko.db.DatabaseManager;
import de.huntertagog.locobroko.items.SA_AbortItem;
import de.huntertagog.locobroko.items.SA_SuccessItem;
import de.huntertagog.locobroko.utils.HeadDatabaseManager;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.structure.Structure;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.window.AnvilWindow;
import xyz.xenondevs.invui.window.Window;

import java.sql.SQLException;
import java.util.*;

public class StatusGui {

    DatabaseManager database = new DatabaseManager();

    String name = FriendSystemPlugin.instance.getMessages().getString("StatusGui.name", "Status bearbeiten");
    String aktuell = FriendSystemPlugin.instance.getMessages().getString("StatusGui.aktuell", "Dein aktueller Status:");

    private static StatusGui instance;

    private final Map<UUID, String> playerInputMap = new HashMap<>();

    private StatusGui() {

    }

    public static StatusGui getInstance() {
        if (instance == null) {
            instance = new StatusGui();
        }
        return instance;
    }

    Timer timer = new Timer();
    TimerTask task = null;

    public void openGui(Player player) throws SQLException {

        String status = database.getStatus(player.getName());
        HeadDatabaseManager headdatabase = new HeadDatabaseManager();

        List<Component> STATUS = new ArrayList<>();
        STATUS.add(Component.text(aktuell)
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.GRAY));
        STATUS.add(Component.empty());
        STATUS.add(Component.text("-> " + status)
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.YELLOW));
        STATUS.add(Component.empty());

        Structure Structure = new Structure(
                "Ö A B")
                .addIngredient('#', new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("§r"))
                .addIngredient('Ö', new ItemBuilder(headdatabase.getItemWithLore(Material.PAPER, 1, Component.text(""), STATUS)))
                .addIngredient('A', new SA_AbortItem())
                .addIngredient('B', new SA_SuccessItem());

        Gui upperGui = Gui.normal()
                .setStructure(Structure)
                .build();

        SettingsGui settingsGui = new SettingsGui();

        Window window = AnvilWindow.split()
                .setViewer(player)
                .setTitle(name)
                .setLowerGui(settingsGui.getSettingsGui(player))
                .setUpperGui(upperGui)
                .addRenameHandler(input -> {
                    if (!input.isEmpty()) {
                        if (task != null) {
                            task.cancel();
                        }
                        task = new TimerTask() {
                            public void run() {
                                addInput(player.getUniqueId(), input);
                            }
                        };
                        timer.schedule(task, 1000);
                    }
                })
                .build();
        window.open();
    }

    public void addInput(UUID playerUUID, String input) {
        playerInputMap.put(playerUUID, input);
    }

    public String getPlayerInput(UUID playerUUID) {
        return playerInputMap.get(playerUUID);
    }
}
