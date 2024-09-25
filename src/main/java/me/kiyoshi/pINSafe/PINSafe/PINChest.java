package me.kiyoshi.pINSafe.PINSafe;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;

public class PINChest {

    private final String password;
    private final String playerUuid;
    private final Location location;
    private final Inventory inventory;

    public PINChest(String password, String playerUuid, Location location, Inventory inventory) {
        this.password = password;
        this.playerUuid = playerUuid;
        this.location = location;
        this.inventory = inventory;

        PINChestManager.instance.registerPINChestList.add(this);
    }

    public PINChest(String password, String playerUuid, Location location, int size) {
        this.password = password;
        this.playerUuid = playerUuid;
        this.location = location;
        this.inventory = Bukkit.createInventory(null, size, "Хранилище");

        PINChestManager.instance.registerPINChestList.add(this);
    }

    public String getPassword() {
        return password;
    }

    public String getPlayerUuid() {
        return playerUuid;
    }

    public Location getLocation() {
        return location;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
