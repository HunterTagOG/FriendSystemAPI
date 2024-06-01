package de.huntertagog.locobroko.entities;

import de.huntertagog.locobroko.db.MySQLConnector;
import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class FriendRequest {

    MySQLConnector database = MySQLConnector.getInstance();

    @Getter
    private int id;
    @Getter
    private int senderId;
    @Getter
    private int receiverId;
    @Getter
    private final Timestamp createdAt;

    public FriendRequest(int id, int senderId, int receiverId, Timestamp createdAt) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.createdAt = createdAt;
    }

    public String getSenderName() throws SQLException, ClassNotFoundException {
        String query = "SELECT username FROM users WHERE id = ?";
        try (PreparedStatement statement = database.getConnection().prepareStatement(query)) {
            statement.setInt(1, senderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("username");
                }
            }
        }
        return "none";
    }
}
