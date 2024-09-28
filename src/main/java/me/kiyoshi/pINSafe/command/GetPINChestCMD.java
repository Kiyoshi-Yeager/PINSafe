package me.kiyoshi.pINSafe.command;

import me.kiyoshi.pINSafe.ConfigLoad;
import me.kiyoshi.pINSafe.util.ChatUtil;
import me.kiyoshi.pINSafe.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GetPINChestCMD implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 0) {
            ChatUtil.sendMessage(commandSender, ConfigLoad.enter_inventory_size);
            return true;
        }
        try {
            Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            ChatUtil.sendMessage(commandSender, ConfigLoad.incorrect_inventory_size);
            return true;
        }
        int size = Integer.parseInt(args[0]);
        Player player;
        if (!(commandSender instanceof Player)) {
            if (args.length < 2) {
                ChatUtil.sendMessage(commandSender, ConfigLoad.enter_the_player_message);
                return true;
            }
            player = Bukkit.getPlayer(args[1]);
        }
        else {
            if (args.length > 1) {
                player = Bukkit.getPlayer(args[1]);
            }
            else {
                player = (Player) commandSender;
            }
        }

        List<String> lore = new ArrayList<>();
        for (String line: ConfigLoad.safe_item_lore) {
            lore.add(line.replace("%size%", size + ""));
        }

        player.getInventory().addItem(new ItemBuilder(ConfigLoad.safe_material, 1)
                .setName(ConfigLoad.safe_item_name)
                .setLore(lore)
                .addPersistent("pin_chest", PersistentDataType.BOOLEAN, true)
                .addPersistent("size", PersistentDataType.INTEGER, size)
                .build());
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return List.of();
    }
}