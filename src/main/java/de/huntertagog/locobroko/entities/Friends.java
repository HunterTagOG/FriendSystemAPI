package de.huntertagog.locobroko.entities;

import de.huntertagog.locobroko.db.MySQLConnector;
import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Friends {

    MySQLConnector database = MySQLConnector.getInstance();

    @Getter
    private int userId;
    @Getter
    private int friendId;

    public Friends(int userId, int friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }


    public String getFriendName() throws SQLException, ClassNotFoundException  {

        String query = "SELECT username FROM users WHERE id = ?";

        try (PreparedStatement statement = database.getConnection().prepareStatement(query)) {
            statement.setInt(1, friendId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("username");
                }
            }
        }
        return "none";
    }

    public boolean isFavorite() throws SQLException, ClassNotFoundException {

        String query = "SELECT favorite FROM friendships WHERE friend_id = ?";

        try (PreparedStatement statement = database.getConnection().prepareStatement(query)) {
            statement.setInt(1, friendId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean("favorite");
                }
            }
        }
        return false;
    }

    public void toogleFavorite() throws SQLException, ClassNotFoundException {
        String query = "UPDATE friendships SET favorite = NOT favorite WHERE friend_id = ?";
        try (PreparedStatement statement = database.getConnection().prepareStatement(query)) {
            statement.setInt(1, friendId);
            statement.executeUpdate();
        }
    }
}
