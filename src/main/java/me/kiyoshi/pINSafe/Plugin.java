package me.kiyoshi.pINSafe;

import me.kiyoshi.pINSafe.PINSafe.PINChestManager;
import me.kiyoshi.pINSafe.PINSafe.ReloadRegisterPINChest;
import me.kiyoshi.pINSafe.command.GetPINChestCMD;
import org.bukkit.plugin.java.JavaPlugin;

public final class Plugin extends JavaPlugin {

    private static Plugin instance;

    public static Plugin getInstance() {return instance;}

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        ReloadRegisterPINChest.instance.loadRegisterPinChest();
        getCommand("getpinsafe").setExecutor(new GetPINChestCMD());
        getCommand("getpinsafe").setTabCompleter(new GetPINChestCMD());
        getServer().getPluginManager().registerEvents(PINChestManager.instance, this);
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        ReloadRegisterPINChest.instance.saveRegisterPinChest();
    }
}
