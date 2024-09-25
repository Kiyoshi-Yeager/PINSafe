package me.kiyoshi.pINSafe.command;

import me.kiyoshi.pINSafe.PINSafe.PINChest;
import me.kiyoshi.pINSafe.util.ChatUtil;
import me.kiyoshi.pINSafe.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class GetPINChestCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player!");
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            ChatUtil.sendMessage(player , "Введите размер инвенторя");
        }
        int size = Integer.parseInt(args[0]);
        if (size % 9 != 0
            || size < 9
            || size > 54) {
            player.sendMessage("Не корректный размер инвенторя!");
        }
        player.getInventory().addItem(new ItemBuilder(Material.ENDER_CHEST, 1)
                .setName("&r&5Хранилище")
                .setLore(Arrays.asList("Хранилище защищенное", "PIN кодом", "Размером " + size ))
                .addPersistent("PINChest", PersistentDataType.BOOLEAN, true)
                .addPersistent("size", PersistentDataType.INTEGER, size)
                .build());
        ChatUtil.sendMessage(player, "&9Выдано хранилище размером " + size);

        return true;
    }
}
