package de.huntertagog.locobroko.utils;

import org.bukkit.scheduler.BukkitRunnable;
import de.huntertagog.locobroko.db.DatabaseManager;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class FriendRequestCleanupTask extends BukkitRunnable {

    private final DatabaseManager database;

    public FriendRequestCleanupTask(DatabaseManager database) {
        this.database = database;
    }

    @Override
    public void run() {
        // Bestimme die maximale Anzahl an Sekunden, nach denen Freundesanfragen gelöscht werden sollen
        int maxAgeSeconds = 24 * 60 * 60; // 24 Stunden

        // Aktuelles Datum und Uhrzeit
        LocalDateTime currentTime = LocalDateTime.now();

        // Berechne das Datum und die Uhrzeit, die maxAgeSeconds vor dem aktuellen Zeitpunkt liegen
        LocalDateTime thresholdTime = currentTime.minusSeconds(maxAgeSeconds);

        // Konvertiere LocalDateTime in einen Timestamp für die Datenbankabfrage
        Timestamp thresholdTimestamp = Timestamp.valueOf(thresholdTime);

        // Lösche Freundesanfragen, die älter sind als thresholdTimestamp
        database.deleteOldFriendRequests(thresholdTimestamp);
    }
}
