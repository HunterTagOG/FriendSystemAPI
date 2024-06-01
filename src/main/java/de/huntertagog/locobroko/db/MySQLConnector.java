package de.huntertagog.locobroko.db;

import de.huntertagog.locobroko.FriendSystemPlugin;
import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnector {

    private static MySQLConnector instance;
    private final String url;
    private final String username;
    private final String password;
    @Getter
    private Connection connection;

    public MySQLConnector(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    // Füge hier die restlichen Klassenvariablen und Methoden hinzu wie in der vorherigen Antwort

    public static MySQLConnector getInstance() {
        if (instance == null) {
            // Hier sollten die Verbindungsdaten aus deiner Konfiguration geladen werden
            String url = FriendSystemPlugin.instance.getConfig().getString("database.url");
            String username = FriendSystemPlugin.instance.getConfig().getString("database.user");
            String password = FriendSystemPlugin.instance.getConfig().getString("database.password");

            instance = new MySQLConnector(url, username, password);
            try {
                instance.connect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
        FriendSystemPlugin.instance.getLogger().info("Connected to MySQL database!");
    }

    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            FriendSystemPlugin.instance.getLogger().info("Disconnected from MySQL database!");
        }
    }

}
