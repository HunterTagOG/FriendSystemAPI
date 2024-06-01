package de.huntertagog.locobroko.utils;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.UUID;

public class HeadDatabaseManager implements Listener {

    private HeadDatabaseAPI apiInstance;

    public HeadDatabaseManager() {
        apiInstance = new HeadDatabaseAPI();
    }

    public HeadDatabaseAPI getAPI() {
        if (!isHeadDatabaseLoaded()) {
            throw new IllegalStateException("HeadDatabase plugin is not loaded or enabled.");
        }

        if (apiInstance == null) {
            apiInstance = new HeadDatabaseAPI();
        }
        return apiInstance;
    }

    private boolean isHeadDatabaseLoaded() {
        Plugin headDatabasePlugin = Bukkit.getPluginManager().getPlugin("HeadDatabase");
        return headDatabasePlugin != null && headDatabasePlugin.isEnabled();
    }

    public ItemStack getItem(String itemName, int amount, Component headName) {
        ItemStack returnItem = getAPI().getItemHead(itemName);
        returnItem.setAmount(amount);
        ItemMeta meta = returnItem.getItemMeta();
        meta.displayName(headName);
        returnItem.setItemMeta(meta);
        return returnItem;
    }

    public ItemStack getItemFromPlayer(UUID uuid, int amount, Component headName, List<Component> lore) {
        ItemStack returnItem = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) returnItem.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
        meta.lore(lore);
        meta.displayName(headName);
        returnItem.setItemMeta(meta);
        returnItem.setAmount(amount);
        return returnItem;
    }

    public ItemStack getItemWithLore(Material material, int amount, Component headName, List<Component> lore) {
        ItemStack returnItem = new ItemStack(material);
        ItemMeta meta = returnItem.getItemMeta();
        meta.lore(lore);
        meta.displayName(headName);
        returnItem.setItemMeta(meta);
        returnItem.setAmount(amount);
        return returnItem;
    }
}
