package me.kiyoshi.pINSafe;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class ConfigLoad {
    public static Material material = Material.valueOf(Plugin.getInstance().getConfig().getString("safe_block"));

    public static String create_password = ChatColor.translateAlternateColorCodes('&', Plugin.getInstance().getConfig().getString("menu_title.create_password"));
    public static String save_password = ChatColor.translateAlternateColorCodes('&', Plugin.getInstance().getConfig().getString("menu_title.save_password"));
    public static String enter_password = ChatColor.translateAlternateColorCodes('&', Plugin.getInstance().getConfig().getString("menu_title.enter_password"));
    public static String try_open_safe = ChatColor.translateAlternateColorCodes('&', Plugin.getInstance().getConfig().getString("menu_title.try_open_safe"));

    private ConfigLoad() {
    }
}
