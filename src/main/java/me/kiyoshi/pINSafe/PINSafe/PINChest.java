package me.kiyoshi.pINSafe.PINSafe;

import me.kiyoshi.pINSafe.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class PINChest {

    private final String password;
    private final UUID playerUuid;
    private final Location location;
    private final Inventory inventory;

    public PINChest(String password, UUID playerUuid, Location location, Inventory inventory) {
        this.password = password;
        this.playerUuid = playerUuid;
        this.location = location;
        this.inventory = inventory;

        PINChestManager.instance.registerPINChestList.add(this);
    }

    public PINChest(String password, UUID playerUuid, Location location, int size) {
        this.password = password;
        this.playerUuid = playerUuid;
        this.location = location;
        if (size % 9 != 0) {
            int newSize = ((size / 9) + 1) * 9;
            this.inventory = Bukkit.createInventory(null, newSize, "Хранилище");
            int remains = newSize - size;
            ItemStack itemStack = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1)
                    .setName("&rBlock_slot")
                    .addPersistent("block_slot", PersistentDataType.BOOLEAN, true)
                    .build();
            for (int i = newSize - 1; i >= newSize - remains; i--) {
                this.inventory.setItem(i, itemStack);
            }
        } else {
            this.inventory = Bukkit.createInventory(null, size, "Хранилище");
        }

        PINChestManager.instance.registerPINChestList.add(this);
    }

    public String getPassword() {
        return password;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public Location getLocation() {
        return location;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
