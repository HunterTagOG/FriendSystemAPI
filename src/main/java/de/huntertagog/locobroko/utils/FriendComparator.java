package de.huntertagog.locobroko.utils;

import de.huntertagog.locobroko.db.DatabaseManager;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.Comparator;

public class FriendComparator implements Comparator<String> {

    private final String sortBy;

    private final Player player;

    DatabaseManager db = new DatabaseManager();

    public FriendComparator(String sortBy, Player player) {
        this.sortBy = sortBy;
        this.player = player;
    }

    @Override
    public int compare(String friend1, String friend2) {
        switch (sortBy) {
            case "Alphabetisch":
                return friend1.compareTo(friend2);
            case "Favorite":
                // Implementieren Sie hier Ihre Logik zur Sortierung nach Favoriten
                boolean favorite1 = db.getFavorite(friend1);
                boolean favorite2 = db.getFavorite(friend2);
                if (favorite1 && !favorite2) {
                    return -1;
                } else if (!favorite1 && favorite2) {
                    return 1;
                } else {
                    // Wenn beide Freunde Favoriten sind oder keine Favoriten sind, alphabetisch sortieren
                    return friend1.compareTo(friend2);
                }
            case "Lastseen":
                Timestamp last_login1 = db.getLastLogin(friend1);
                Timestamp last_login2 = db.getLastLogin(friend2);
                // Sortieren Sie nach dem Zeitpunkt des letzten Logins (zuletzt gesehen zuerst)
                if (last_login1 != null && last_login2 != null) {
                    return last_login2.compareTo(last_login1); // Sortieren absteigend nach dem Zeitpunkt des letzten Logins
                } else if (last_login1 != null) {
                    return -1; // Der erste Freund wurde zuletzt gesehen
                } else if (last_login2 != null) {
                    return 1; // Der zweite Freund wurde zuletzt gesehen
                } else {
                    return 0; // Beide Freunde wurden nie gesehen
                }
            case "Oldestfriend":
                Timestamp created_at1 = db.getCreatedAt(db.getUserId(friend1));
                Timestamp created_at2 = db.getCreatedAt(db.getUserId(friend2));
                // Sortieren Sie nach dem Zeitpunkt, an dem der Freund erstellt wurde (ältester Freund zuerst)
                if (created_at1 != null && created_at2 != null) {
                    return created_at1.compareTo(created_at2); // Sortieren aufsteigend nach dem Zeitpunkt, an dem der Freund erstellt wurde
                } else if (created_at1 != null) {
                    return 1; // Der zweite Freund wurde nicht gefunden, daher ist der erste Freund älter
                } else if (created_at2 != null) {
                    return -1; // Der erste Freund wurde nicht gefunden, daher ist der zweite Freund älter
                } else {
                    return 0; // Beide Freunde wurden nie gefunden
                }
            default:
                // Standardmäßig alphabetisch sortieren, wenn das Sortierkriterium nicht erkannt wird
                return friend1.compareTo(friend2);
        }
    }
}
