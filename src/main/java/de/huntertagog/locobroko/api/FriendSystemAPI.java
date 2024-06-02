package de.huntertagog.locobroko.api;

import de.huntertagog.locobroko.renders.MainGui;
import org.bukkit.entity.Player;

public class FriendSystemAPI {

    private static FriendSystemAPI instance;

    public FriendSystemAPI() {
    }

    public static FriendSystemAPI getInstance() {
        if (instance == null) {
            instance = new FriendSystemAPI();
        }
        return instance;
    }

    public void openFriendGUI(Player player) {
        MainGui.openGui(player);
    }
}


