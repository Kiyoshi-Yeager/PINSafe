package me.kiyoshi.pINSafe.command;

import me.kiyoshi.pINSafe.util.ChatUtil;
import me.kiyoshi.pINSafe.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class GetPINChestCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player!");
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            ChatUtil.sendMessage(player , "Введите размер инвенторя");
            return true;
        }
        ItemStack itemStack = new ItemBuilder(Material.ENDER_CHEST, 1)
                .setName("&rХранилище")
                .addPersistent("pin_chest", PersistentDataType.BOOLEAN, true)
                .addPersistent("size", PersistentDataType.INTEGER, Integer.parseInt(args[0]))
                .build();
        player.getInventory().addItem(itemStack);

        return true;
    }
}
