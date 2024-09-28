package me.kiyoshi.pINSafe;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.List;

public class ConfigLoad {
    public static Material safe_material = Material.valueOf(Plugin.getInstance().getConfig().getString("safe_block"));
    public static final String safe_item_name = ChatColor.translateAlternateColorCodes('&', Plugin.getInstance().getConfig().getString("safe_item_name"));
    public static final List<String> safe_item_lore = translateColorLore(Plugin.getInstance().getConfig().getStringList("safe_item_lore"));

    public static final String drop_safe_if_break = Plugin.getInstance().getConfig().getString("drop_safe_if_break");
    public static final String drop_resource_if_break = Plugin.getInstance().getConfig().getString("drop_resource_if_break");

    public static final Sound safe_open_sound = Sound.valueOf(Plugin.getInstance().getConfig().getString("sound.safe_open.sound"));
    public static final Float safe_open_volume = Float.valueOf(Plugin.getInstance().getConfig().getString("sound.safe_open.volume"));
    public static final Float safe_open_speed = Float.valueOf(Plugin.getInstance().getConfig().getString("sound.safe_open.speed"));

    public static final Sound open_password_menu_sound = Sound.valueOf(Plugin.getInstance().getConfig().getString("sound.open_password_menu.sound"));
    public static final Float open_password_menu_volume = Float.valueOf(Plugin.getInstance().getConfig().getString("sound.open_password_menu.volume"));
    public static final Float open_password_menu_speed = Float.valueOf(Plugin.getInstance().getConfig().getString("sound.open_password_menu.speed"));

    public static final Sound invalid_password_sound = Sound.valueOf(Plugin.getInstance().getConfig().getString("sound.invalid_password.sound"));
    public static final Float invalid_password_volume = Float.valueOf(Plugin.getInstance().getConfig().getString("sound.invalid_password.volume"));
    public static final Float invalid_password_speed = Float.valueOf(Plugin.getInstance().getConfig().getString("sound.invalid_password.speed"));

    public static final Material locket_slot_material = Material.valueOf(Plugin.getInstance().getConfig().getString("menu.locked_slot.material"));
    public static final String locket_slot_name = ChatColor.translateAlternateColorCodes('&', Plugin.getInstance().getConfig().getString("menu.locked_slot.name"));

    public static final String enter_the_player_message = ChatColor.translateAlternateColorCodes('&', Plugin.getInstance().getConfig().getString("message.enter_the_player"));
    public static final String enter_inventory_size_massage = ChatColor.translateAlternateColorCodes('&', Plugin.getInstance().getConfig().getString("message.enter_inventory_size"));
    public static final String incorrect_inventory_size_message = ChatColor.translateAlternateColorCodes('&', Plugin.getInstance().getConfig().getString("message.incorrect_inventory_size"));
    public static final String player_not_found_message = ChatColor.translateAlternateColorCodes('&', Plugin.getInstance().getConfig().getString("message.player_not_found"));

    public static final String success_safe_give_message = ChatColor.translateAlternateColorCodes('&', Plugin.getInstance().getConfig().getString("message.success_safe_give"));

    public static final String no_permission_to_create_message = ChatColor.translateAlternateColorCodes('&', Plugin.getInstance().getConfig().getString("message.no_permission_to_create"));
    public static final String no_permission_to_open_message = ChatColor.translateAlternateColorCodes('&', Plugin.getInstance().getConfig().getString("message.no_permission_to_open"));

    public static final String safe_menu_title = ChatColor.translateAlternateColorCodes('&', Plugin.getInstance().getConfig().getString("menu.safe_menu_title"));
    public static final String create_password = ChatColor.translateAlternateColorCodes('&', Plugin.getInstance().getConfig().getString("menu.create_password"));
    public static final String save_password = ChatColor.translateAlternateColorCodes('&', Plugin.getInstance().getConfig().getString("menu.save_password"));
    public static final String enter_password = ChatColor.translateAlternateColorCodes('&', Plugin.getInstance().getConfig().getString("menu.enter_password"));
    public static final String try_open_safe = ChatColor.translateAlternateColorCodes('&', Plugin.getInstance().getConfig().getString("menu.try_open_safe"));

    public static final Material pressed_button_material = Material.valueOf(Plugin.getInstance().getConfig().getString("menu.pressed_button_material"));
    public static final Material done_button_material = Material.valueOf(Plugin.getInstance().getConfig().getString("menu.done_button_material"));

    private ConfigLoad() {
    }

    private static List<String> translateColorLore(List<String> lore) {
        List<String> newLore = new ArrayList<>();
        for (String string: lore) {
            newLore.add(ChatColor.translateAlternateColorCodes('&', string));
        }
        return newLore;
    }

}
