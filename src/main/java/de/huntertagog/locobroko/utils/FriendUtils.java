package de.huntertagog.locobroko.utils;

import de.huntertagog.locobroko.FriendSystemPlugin;
import de.huntertagog.locobroko.db.DatabaseManager;
import de.huntertagog.locobroko.db.MySQLConnector;
import de.huntertagog.locobroko.items.*;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.impl.SimpleItem;

import java.sql.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class FriendUtils {
    @Getter
    private static final FriendUtils instance = new FriendUtils();

    static MySQLConnector database = MySQLConnector.getInstance();
    static DatabaseManager db = new DatabaseManager();
    static HeadDatabaseManager headDatabaseManager = new HeadDatabaseManager();

    public List<Item> getFriendRequests(List<String> friendNames) {
        List<Item> friendItems = new ArrayList<>();
        for (String friendName : friendNames) {
            ItemStack playerHead = getPlayerHead(friendName);

            if (playerHead != null) {
                FA_PlayerItem item = new FA_PlayerItem(friendName);
                friendItems.add(item);
            }
        }
        return friendItems;
    }

    public ItemStack getPlayerHead(String playerName) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        if (meta != null) {
            meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerName));
            skull.setItemMeta(meta);
            return skull;
        }
        return null;
    }

    public static List<String> getFriendNames(int user_id, Player player) {
        List<String> friendNames = new ArrayList<>();
        // Hier die Verbindung zur Datenbank herstellen
        try {
            String query = "SELECT * FROM friendships WHERE user_id = ? OR friend_id = ?";
            PreparedStatement statement = database.getConnection().prepareStatement(query);
            statement.setInt(1, user_id);
            statement.setInt(2, user_id);
            ResultSet resultID = statement.executeQuery();

            while (resultID.next()) {
                int friendId = 0;
                if(resultID.getInt("user_id") == user_id) {
                    friendId = resultID.getInt("friend_id");
                } else if (resultID.getInt("friend_id") == user_id) {
                    friendId = resultID.getInt("user_id");
                }
                PreparedStatement state = database.getConnection().prepareStatement("SELECT username FROM users WHERE id = ?");
                state.setInt(1, friendId);
                ResultSet resultString = state.executeQuery();

                while (resultString.next()) {
                    String friendName = resultString.getString("username");
                    friendNames.add(friendName);
                }

                String sortBy = String.valueOf(db.getCurrentSortMode(player.getName()));
                friendNames.sort(new FriendComparator(sortBy, player));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendNames;
    }

    public List<String> getFriendRequestsNames(int receiver_id) {
        List<String> friendNames = new ArrayList<>();
        // Hier die Verbindung zur Datenbank herstellen
        try {
            String query = "SELECT * FROM friend_requests WHERE receiver_id = ?";
            PreparedStatement statement = database.getConnection().prepareStatement(query);
            statement.setInt(1, receiver_id);
            ResultSet resultID = statement.executeQuery();

            while (resultID.next()) {
                int senderId = resultID.getInt("sender_id");
                PreparedStatement state = database.getConnection().prepareStatement("SELECT username FROM users WHERE id = ?");
                state.setInt(1, senderId);
                ResultSet resultString = state.executeQuery();

                while (resultString.next()) {
                    String friendName = resultString.getString("username");
                    friendNames.add(friendName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendNames;
    }

    public String getDurationInverted(Timestamp timestamp) {
        Instant endTime = timestamp.toInstant();
        Instant startTime = Instant.now(); // Annahme: Die aktuelle Zeit

        Duration duration = Duration.between(startTime, endTime);

        long seconds = duration.getSeconds();
        long days = seconds / (60 * 60 * 24);
        seconds %= (60 * 60 * 24);
        long hours = seconds / (60 * 60);
        seconds %= (60 * 60);
        long minutes = seconds / 60;
        seconds %= 60;

        String formattedTime = "";
        if (days > 30) {
            formattedTime = startTime.toString(); // Falls mehr als 30 Tage vergangen sind, gib das Startdatum aus
        } else if (days > 0) {
            formattedTime = days + " Tage";
        } else if (hours > 0) {
            formattedTime = hours + " Std";
        } else if (minutes > 0) {
            formattedTime = minutes + " Min";
        } else {
            formattedTime = seconds + " Sek";
        }

        return formattedTime;
    }

    public String getDuration(Timestamp timestamp) {
        if (timestamp == null) {
            return "N/A";
        }
        Instant startTime = timestamp.toInstant();
        Instant endTime = Instant.now(); // Annahme: Die aktuelle Zeit

        Duration duration = Duration.between(startTime, endTime);

        long seconds = duration.getSeconds();
        long days = seconds / (60 * 60 * 24);
        seconds %= (60 * 60 * 24);
        long hours = seconds / (60 * 60);
        seconds %= (60 * 60);
        long minutes = seconds / 60;
        seconds %= 60;

        String formattedTime = "";
        if (days > 30) {
            formattedTime = startTime.toString(); // Falls mehr als 30 Tage vergangen sind, gib das Startdatum aus
        } else if (days > 0) {
            formattedTime = days + " Tage";
        } else if (hours > 0) {
            formattedTime = hours + " Std";
        } else if (minutes > 0) {
            formattedTime = minutes + " Min";
        } else {
            formattedTime = seconds + " Sek";
        }

        return formattedTime;
    }

    public UUID getPlayerUUID(String friendName) {
        Player friend = Bukkit.getPlayer(friendName);
        if (friend == null) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(friendName);
            return offlinePlayer.getUniqueId();
        }
        return friend.getUniqueId();
    }

    public static List<Item> getFriendItems(Player player) {
        List<String> friends = getFriendNames(db.getUserId(player.getName()), player);
        List<Item> friendItems = new ArrayList<>();
        for (String friendName : friends) {
            ItemStack playerHead = FriendUtils.getInstance().getPlayerHead(friendName);
            UUID uuid = FriendUtils.getInstance().getPlayerUUID(friendName);
            int id = db.getUserId(friendName);

            List<Component> OnlineHead = new ArrayList<>();
            OnlineHead.add(Component.empty());
            OnlineHead.add(Component.text("Online seit")
                    .decoration(TextDecoration.ITALIC, false)
                    .color(NamedTextColor.GRAY));
            OnlineHead.add(Component.text("-> " + FriendUtils.getInstance().getDuration(db.getLastLogin(friendName)))
                    .decoration(TextDecoration.ITALIC, false)
                    .color(NamedTextColor.YELLOW));
            OnlineHead.add(Component.empty());
            OnlineHead.add(Component.text("Status")
                    .decoration(TextDecoration.ITALIC, false)
                    .color(NamedTextColor.GRAY));
            OnlineHead.add(Component.text("-> " + db.getStatus(friendName))
                    .decoration(TextDecoration.ITALIC, false)
                    .color(NamedTextColor.YELLOW));
            OnlineHead.add(Component.empty());
            OnlineHead.add(Component.text("Freunde seit")
                    .decoration(TextDecoration.ITALIC, false)
                    .color(NamedTextColor.GRAY));
            OnlineHead.add(Component.text("-> " + FriendUtils.getInstance().getDuration(db.getCreatedAt(id)))
                    .decoration(TextDecoration.ITALIC, false)
                    .color(NamedTextColor.YELLOW));

            List<Component> OfflineHead = new ArrayList<>();
            OfflineHead.add(Component.empty());
            OfflineHead.add(Component.text("Zuletzt online")
                    .decoration(TextDecoration.ITALIC, false)
                    .color(NamedTextColor.GRAY));
            OfflineHead.add(Component.text("-> " + FriendUtils.getInstance().getDuration(db.getLastLogout(friendName)))
                    .decoration(TextDecoration.ITALIC, false)
                    .color(NamedTextColor.YELLOW));
            OfflineHead.add(Component.empty());
            OfflineHead.add(Component.text("Status")
                    .decoration(TextDecoration.ITALIC, false)
                    .color(NamedTextColor.GRAY));
            OfflineHead.add(Component.text("-> " + db.getStatus(friendName))
                    .decoration(TextDecoration.ITALIC, false)
                    .color(NamedTextColor.YELLOW));
            OfflineHead.add(Component.empty());
            OfflineHead.add(Component.text("Freunde seit")
                    .decoration(TextDecoration.ITALIC, false)
                    .color(NamedTextColor.GRAY));
            OfflineHead.add(Component.text("-> " + FriendUtils.getInstance().getDuration(db.getCreatedAt(id)))
                    .decoration(TextDecoration.ITALIC, false)
                    .color(NamedTextColor.YELLOW));
            if (playerHead != null) {
                SimpleItem item;
                if (!db.isOnline(friendName)) {
                    item = new SimpleItem(headDatabaseManager.getItemWithLore(Material.SKELETON_SKULL, 1,
                            Component.text(friendName)
                                    .decoration(TextDecoration.ITALIC, false)
                                    .color(NamedTextColor.RED), OfflineHead));
                } else {
                    item = new SimpleItem(headDatabaseManager.getItemFromPlayer(uuid, 1,
                            Component.text(friendName)
                                    .decoration(TextDecoration.ITALIC, false)
                                    .color(NamedTextColor.GREEN), OnlineHead));
                }
                friendItems.add(item);
            }
        }
        return friendItems;
    }
}
