package me.kiyoshi.pINSafe.PINSafe;

import me.kiyoshi.pINSafe.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReloadRegisterPINChest {

    public static final ReloadRegisterPINChest instance = new ReloadRegisterPINChest();

    private ReloadRegisterPINChest() {
    }

    public void saveRegisterPinChest() {
        List<PINChest> registerPINChestList = PINChestManager.instance.registerPINChestList;
        Map<Player, WaitingPINChest> waitingPINChestList = PINChestManager.instance.waitingPINChestList;

        File folder = new File(Plugin.getInstance().getDataFolder().getAbsolutePath() + "/data");
        folder.mkdirs();
        File file = new File(folder.getAbsolutePath() + "/safes.yml");
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        YamlConfiguration config = new YamlConfiguration();
        int cnt = 0;
        for (PINChest pinChest : registerPINChestList) {
            ConfigurationSection section = config.createSection(String.valueOf(cnt));
            section.set("password", pinChest.getPassword());
            section.set("uuid", pinChest.getPlayerUuid().toString());
            section.set("location", pinChest.getLocation());
            section.set("size", pinChest.getInventory().getSize());

            ConfigurationSection inventorySection = section.createSection("inventory");
            Inventory inventory = pinChest.getInventory();
            for (int i = 0; i < inventory.getSize(); i++) {
                inventorySection.set(i + "", inventory.getItem(i));
            }
            cnt++;
        }
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadRegisterPinChest() {
        File folder = new File(Plugin.getInstance().getDataFolder().getAbsolutePath() + "/data");
        if (folder.exists()) {
            File file = new File(folder.getAbsolutePath() + "/" + "safes" + ".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            for (String key : config.getKeys(false)) {
                String password = config.getString(key + ".password");
                UUID uuid = UUID.fromString(config.getString(key + ".uuid"));
                Location location = config.getLocation(key + ".location");
                int size = config.getInt(key + ".size");
                Inventory inventory = Bukkit.createInventory(null, size, "Хранилище");
                ConfigurationSection inventorySection = config.getConfigurationSection(key + ".inventory");
                for (String slotKey: inventorySection.getKeys(false)) {
                    int slot = Integer.parseInt(slotKey);
                    ItemStack item = inventorySection.getItemStack(slot + "");
                    inventory.setItem(slot, item);
                }
                new PINChest(password, uuid, location, inventory);
            }
        }
    }
}