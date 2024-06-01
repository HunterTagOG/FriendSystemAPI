package de.huntertagog.locobroko.db;

import de.huntertagog.locobroko.FriendSystemPlugin;
import de.huntertagog.locobroko.entities.Friends;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class DatabaseManager {

    MySQLConnector database = MySQLConnector.getInstance();

    public enum SortMode {
        ALPHABETISCH,
        FAVORITE,
        LASTSEEN,
        OLDESTFRIEND
    }

    public List<Friends> getFriends(int userId) {
        List<Friends> friends = new ArrayList<>();
        String query = "SELECT * FROM friendships WHERE user_id = ? OR friend_id = ?";
        try (PreparedStatement statement = database.getConnection().prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int friendId = 0;
                    if(resultSet.getInt("user_id") == userId) {
                        friendId = resultSet.getInt("friend_id");
                    } else if (resultSet.getInt("friend_id") == userId) {
                        friendId = resultSet.getInt("user_id");
                    }
                    Friends friend = new Friends(userId, friendId);
                    friends.add(friend);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friends;
    }

    public String getFriendCode(UUID uniqueId) {
        String query = "SELECT friendcode FROM users WHERE uuid = ?";
        try (PreparedStatement statement = database.getConnection().prepareStatement(query)) {
            statement.setString(1, uniqueId.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("friend_code");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getUserId(String username) {
        String query = "SELECT id FROM users WHERE username = ?";
        try (PreparedStatement statement = database.getConnection().prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public UUID getUserUUID(String username) {
        String query = "SELECT uuid FROM users WHERE username = ?";
        try (PreparedStatement statement = database.getConnection().prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return UUID.fromString(resultSet.getString("uuid"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUUID(String username) {
        String query = "SELECT uuid FROM users WHERE username = ?";
        try (PreparedStatement statement = database.getConnection().prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("uuid");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Timestamp getCreatedAt(int id) {
        String query = "SELECT created_at FROM friendships WHERE user_id = ? OR friend_id = ?";
        try (PreparedStatement statement = database.getConnection().prepareStatement(query)) {
            statement.setInt(1, id);
            statement.setInt(2, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getTimestamp("created_at");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Timestamp getLastLogin(String username) {
        String query = "SELECT login_at FROM users WHERE username = ?";
        try (PreparedStatement statement = database.getConnection().prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getTimestamp("login_at");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Timestamp getLastLogout(String username) {
        String query = "SELECT logout_at FROM users WHERE username = ?";
        try (PreparedStatement statement = database.getConnection().prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getTimestamp("logout_at");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public String getStatus(String username) {
        String query = "SELECT status FROM users WHERE username = ?";
        try (PreparedStatement statement = database.getConnection().prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("status");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public int getFriendRequestCount(int userId) {
        int requestCount = 0;
        String query = "SELECT COUNT(*) AS request_count FROM friend_requests WHERE receiver_id = ? AND status = 'PENDING'";
        try (PreparedStatement statement = database.getConnection().prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    requestCount = resultSet.getInt("request_count");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requestCount;
    }

    public void setToggle(String username, String toggle) {
        // Zuerst den aktuellen Wert abrufen
        boolean currentToggle = getCurrentToggle(username, toggle);
        // Den invertierten Wert berechnen
        boolean newToggle = !currentToggle;

        // Dann das Update mit dem invertierten Wert durchführen
        String query = "UPDATE users SET " + toggle + "= ? WHERE username = ?";
        try (PreparedStatement statement = database.getConnection().prepareStatement(query)) {
            statement.setBoolean(1, newToggle);
            statement.setString(2, username);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getCurrentToggle(String username, String toggle) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement statement = database.getConnection().prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean(toggle);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Falls der Wert nicht abgerufen werden kann, gib einfach false zurück
        return false;
    }

    public void setStatus(String username, String input) {
        String query = "UPDATE users SET status= ? WHERE username = ?";
        try (PreparedStatement statement = database.getConnection().prepareStatement(query)) {
            statement.setString(1, input);
            statement.setString(2, username);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Timestamp getRequestDate(int id) {
        String query = "SELECT created_at FROM friend_requests WHERE sender_id = ?";
        try (PreparedStatement statement = database.getConnection().prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getTimestamp("created_at");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Timestamp getExpireDate(int id) {
        Timestamp requestDate = getRequestDate(id);
        long millisecondsIn24Hours = 24 * 60 * 60 * 1000;
        return new Timestamp(requestDate.getTime() + millisecondsIn24Hours);
    }

    public void deleteOldFriendRequests(Timestamp thresholdTimestamp) {
        String query = "DELETE FROM friend_requests WHERE created_at < ?";
        try (PreparedStatement statement = database.getConnection().prepareStatement(query)) {
            statement.setTimestamp(1, thresholdTimestamp);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addFriend(Player player, String friend){
        // Überprüfen, ob der Freund existiert
        if (userExists(friend)) {
            int playerId = getUserId(player.getName());
            int friendId = getUserId(friend);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            // Freundschaft in die Datenbank einfügen
            this.addFriendship(playerId, friendId, timestamp);
            player.sendMessage(Component.text("Freund erfolgreich hinzugefügt!"));
        } else {
            player.sendMessage(Component.text("Der angegebene Benutzer existiert nicht."));
        }
        FriendSystemPlugin.instance.getLogger().info("Freund wurde hinzugefügt!");
    }

    private void addFriendship(int userId, int friendId, Timestamp created_at) {
        String query = "INSERT INTO friendships (user_id, friend_id, created_at) VALUES (?, ?, ?)";
        try (PreparedStatement statement = database.getConnection().prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, friendId);
            statement.setTimestamp(3, created_at);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean userExists(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (PreparedStatement statement = database.getConnection().prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean denyFriendRequest(Player player) {
        int receiverId = getUserId(player.getName());
        int reuquestId = findRequestIdByReceiverId(receiverId);
        if(reuquestId == -1) {
            return false;
        }
        removeFriendRequest(reuquestId);
        return true;
    }

    public boolean acceptFriendRequest(Player player, String friend) {
        int receiverId = getUserId(player.getName());
        int reuquestId = findRequestIdByReceiverId(receiverId);
        if(reuquestId == -1) {
            return false;
        }
        removeFriendRequest(reuquestId);
        addFriend(player, friend);
        return true;
    }

    public int findRequestIdByReceiverId(int receiverId) {
        String query = "SELECT id FROM friend_requests WHERE receiver_id = ? AND status = 'PENDING'";
        try (PreparedStatement statement = database.getConnection().prepareStatement(query)) {
            statement.setInt(1, receiverId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void removeFriendRequest(int requestId) {
        String query = "DELETE FROM friend_requests WHERE id = ?";
        try (PreparedStatement statement = database.getConnection().prepareStatement(query)) {
            statement.setInt(1, requestId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getFavorite(String name) {
        int id = getUserId(name);
        String query = "SELECT favorite_user FROM friendships WHERE user_id = ?";
        String query2 = "SELECT favorite_friend FROM friendships WHERE friend_id = ?";
        try (PreparedStatement statement = database.getConnection().prepareStatement(query);
             PreparedStatement statement2 = database.getConnection().prepareStatement(query2)) {
            statement.setInt(1, id);
            statement2.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery();
                 ResultSet resultSet2 = statement2.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean("favorite_user");
                }
                if (resultSet2.next()) {
                    return resultSet.getBoolean("favorite_friend");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Methode zum Abrufen des aktuellen Sortiermodus für einen bestimmten Benutzer
    public SortMode getCurrentSortMode(String username) throws SQLException {
        String query = "SELECT sortierung FROM users WHERE username = ?";
        try (PreparedStatement statement = database.getConnection().prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String sortModeString = resultSet.getString("sortierung");
                    return SortMode.valueOf(sortModeString);
                }
            }
        }
        // Rückgabe des Standard-Sortiermodus, wenn der Benutzer nicht gefunden wird oder ein Fehler auftritt
        return SortMode.ALPHABETISCH;
    }

    // Methode zum Aktualisieren des Sortiermodus für einen bestimmten Benutzer
    public void updateSortMode(String username, SortMode newSortMode) throws SQLException {
        // Berechnen des nächsten Sortiermodus
        SortMode nextSortMode = getNextSortMode(newSortMode);

        String query = "UPDATE users SET sortierung = ? WHERE username = ?";
        try (PreparedStatement statement = database.getConnection().prepareStatement(query)) {
            statement.setString(1, nextSortMode.name());
            statement.setString(2, username);
            statement.executeUpdate();
        }
    }

    // Hilfsmethode zum Berechnen des nächsten Sortiermodus
    private SortMode getNextSortMode(SortMode currentSortMode) {
        // Definieren Sie die Reihenfolge der Sortiermodi
        SortMode[] sortModes = SortMode.values();

        // Ermitteln Sie die Position des aktuellen Sortiermodus in der Liste
        int currentIndex = Arrays.asList(sortModes).indexOf(currentSortMode);

        // Berechnen Sie die Position des nächsten Sortiermodus
        int nextIndex = (currentIndex + 1) % sortModes.length;

        // Rückgabe des nächsten Sortiermodus
        return sortModes[nextIndex];
    }

    public boolean isOnline(String username) {
        String query = "SELECT online FROM users WHERE username = ?";
        try (PreparedStatement statement = database.getConnection().prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean("online");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
