package de.huntertagog.locobroko.api;

import de.huntertagog.locobroko.FriendSystemPlugin;
import de.huntertagog.locobroko.renders.MainGui;
import lombok.Getter;
import org.bukkit.entity.Player;

public class FriendSystemAPI {
    @Getter
    private static FriendSystemAPI instance;
    private final FriendSystemPlugin plugin;

    private FriendSystemAPI(FriendSystemPlugin plugin) {
        this.plugin = plugin;
    }

    public static void init(FriendSystemPlugin plugin) {
        if (instance == null) {
            instance = new FriendSystemAPI(plugin);
        }
    }

    public void openFriendGUI(Player player) {
        MainGui.openGui(player);
    }
}


