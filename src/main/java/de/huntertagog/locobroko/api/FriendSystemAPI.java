package de.huntertagog.locobroko.api;

import de.huntertagog.locobroko.FriendSystemPlugin;
import de.huntertagog.locobroko.renders.MainGui;
import org.bukkit.entity.Player;

public class FriendSystemAPI {

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

    public static FriendSystemAPI getInstance() {
        if (instance == null) {
            throw new IllegalStateException("FriendSystemAPI is not enabled");
        }
        return instance;
    }

    public void openFriendGUI(Player player) {
        MainGui.openGui(player);
    }
}


