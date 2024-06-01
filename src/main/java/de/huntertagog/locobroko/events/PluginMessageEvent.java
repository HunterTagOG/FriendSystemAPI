package de.huntertagog.locobroko.events;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import de.huntertagog.locobroko.FriendSystemPlugin;
import de.huntertagog.locobroko.db.DatabaseManager;
import de.huntertagog.locobroko.renders.MainGui;
import de.huntertagog.locobroko.renders.StatusGui;
import de.huntertagog.locobroko.utils.FriendUtils;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class PluginMessageEvent implements PluginMessageListener {

    @Getter
    private static final PluginMessageEvent instance = new PluginMessageEvent();

    String secret = FriendSystemPlugin.instance.getConfig().getString("secret");

    DatabaseManager database = new DatabaseManager();

    private final ConcurrentHashMap<UUID, CountDownLatch> latchMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, String> onlineStatusRequests = new ConcurrentHashMap<>();


    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte @NotNull [] bytes) {
        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        String channelName = in.readUTF();
        if(channelName.equals("friendsystemproxy:" + secret)) {
            String subchannel = in.readUTF();
            if(subchannel.equals("friendGui")) {
                String secretMessage = in.readUTF();
                MainGui.openGui(player);
                player.sendMessage(Component.text(secretMessage));
            } else if (subchannel.equals("friendStatusGui")) {
                String secretMessage = in.readUTF();
                try {
                    StatusGui.getInstance().openGui(player);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                player.sendMessage(Component.text(secretMessage));
            }
        } else if (channelName.equals("friendsystemproxy:friend")) {
            String subchannel = in.readUTF();
            if (subchannel.equals("friendIsOnline")) {
                FriendSystemPlugin.getInstance().getLogger().severe("------------------Received Message------------------");
                String playerName = in.readUTF();
                String isOnline = in.readUTF();
                UUID uuid = database.getUserUUID(playerName);

                FriendSystemPlugin.getInstance().getLogger().severe("Received UUID: " + uuid);
                onlineStatusRequests.put(uuid, isOnline);
                CountDownLatch latch = getLatch(uuid);
                FriendSystemPlugin.getInstance().getLogger().severe("------------------ Latch for " + uuid + ": " + latch + " ------------------");
                if (latch != null) {
                    latch.countDown();  // Release the latch
                    FriendSystemPlugin.getInstance().getLogger().severe("------------------ Latch released for " + playerName + " ------------------");
                } else {
                    FriendSystemPlugin.getInstance().getLogger().severe("Latch not found for " + playerName);
                }
            }
        }
    }

    public synchronized void putLatch(UUID uuid, CountDownLatch latch) {
        FriendSystemPlugin.getInstance().getLogger().severe("Putting Latch for UUID: " + uuid + " Latch: " + latch);
        latchMap.put(uuid, latch);
    }

    public synchronized CountDownLatch getLatch(UUID uuid) {
        FriendSystemPlugin.getInstance().getLogger().severe("Getting Latch for UUID: " + uuid);
        return latchMap.get(uuid);
    }

    public String getOnlineStatusRequest(UUID uuid) {
        return onlineStatusRequests.getOrDefault(uuid, "false");
    }

    public void removeOnlineStatusRequest(UUID uuid) {
        onlineStatusRequests.remove(uuid);
        latchMap.remove(uuid);
    }
}
