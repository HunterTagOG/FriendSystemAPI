package de.huntertagog.locobroko;

import de.huntertagog.locobroko.api.FriendSystemAPI;
import de.huntertagog.locobroko.db.DatabaseManager;
import de.huntertagog.locobroko.db.MySQLConnector;
import de.huntertagog.locobroko.utils.FriendRequestCleanupTask;
import de.huntertagog.locobroko.utils.HeadDatabaseManager;
import de.huntertagog.locobroko.events.PluginMessageEvent;
import lombok.Getter;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@Getter
public final class FriendSystemPlugin extends JavaPlugin implements CommandExecutor, Listener {

    @Getter
    public static FriendSystemPlugin instance;
    private FileConfiguration config;
    private FileConfiguration messages;

    @Override
    public void onEnable() {

        instance = this;

        FriendSystemAPI.init(this);

        try {
            loadConfig();
            loadMessages();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String secret = getConfig().getString("secret");
        getLogger().info("Secret: " + secret);
        if (secret == null || secret.isEmpty()) {
            getLogger().severe("Secret is not set!");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        MySQLConnector.getInstance();

        DatabaseManager database = new DatabaseManager();

        this.getServer().getPluginManager().registerEvents(new HeadDatabaseManager(), this);

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "friendsystemproxy:" + secret);
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "friendsystemproxy:" + secret, PluginMessageEvent.getInstance());
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "friendsystemproxy:friend", PluginMessageEvent.getInstance());

        // Starte den Cleanup-Task und lasse ihn alle 24 Stunden ausführen
        int cleanupIntervalTicks = 20 * 60 * 60 * 6; // 24 Stunden in Ticks
        //int cleanupIntervalTicks = 20 * 60; // 24 Stunden in Ticks
        new FriendRequestCleanupTask(database).runTaskTimer(this, 0, cleanupIntervalTicks);

        String asciiArt =
                """
                          _____     _                _ ____            _                \s
                         |  ___| __(_) ___ _ __   __| / ___| _   _ ___| |_ ___ _ __ ___ \s
                         | |_ | '__| |/ _ \\ '_ \\ / _` \\___ \\| | | / __| __/ _ \\ '_ ` _ \\\s
                         |  _|| |  | |  __/ | | | (_| |___) | |_| \\__ \\ ||  __/ | | | | |
                         |_|  |_|  |_|\\___|_| |_|\\__,_|____/ \\__, |___/\\__\\___|_| |_| |_|
                                                             |___/       \
                        """;

        getLogger().info("*****************************************************************");
        for (String line : asciiArt.split("\n")) {
            getLogger().info(line);
        }
        getLogger().info("");
        getLogger().info("                                         Loading version 1.0.0");
        getLogger().info("*****************************************************************");


    }

    @Override
    public void onDisable() {

        try {
            MySQLConnector.getInstance().disconnect();
        } catch (SQLException e) {
            getLogger().severe("Failed to disconnect from MySQL database!");
            e.printStackTrace();
        }

        getLogger().info("Plugin disabled!");

        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
    }

    private void loadConfig() throws IOException {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            // Wenn die Konfigurationsdatei nicht existiert, erstellen Sie eine neue
            saveResource("config.yml", false);
        }

        // Laden der Konfigurationsdatei
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    private void loadMessages() throws IOException {
        File messagesFile = new File(getDataFolder(), "language.yml");
        if (!messagesFile.exists()) {
            // Wenn die Konfigurationsdatei nicht existiert, erstellen Sie eine neue
            saveResource("language.yml", false);
        }

        // Laden der Konfigurationsdatei
        messages = YamlConfiguration.loadConfiguration(messagesFile);
    }
}
