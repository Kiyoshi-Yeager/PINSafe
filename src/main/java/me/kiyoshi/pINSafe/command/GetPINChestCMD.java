package me.kiyoshi.pINSafe.command;

import me.kiyoshi.pINSafe.ConfigLoad;
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

public class GetPINChestCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player!");
            return true;
        }
        if (!sender.isOp()) {
            ChatUtil.sendMessage(sender, "Недостаточно прав!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            ChatUtil.sendMessage(player , "Введите размер инвенторя");
            return true;
        }

        try {
            int size = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            return true;
        }
        int size = Integer.parseInt(args[0]);

        if (size > 54 || size < 1) {
            ChatUtil.sendMessage(player, "Укажите число от 1 до 54");
            return true;
        }


        ItemStack itemStack = new ItemBuilder(ConfigLoad.material, 1)
                .setName("&rХранилище")
                .addPersistent("pin_chest", PersistentDataType.BOOLEAN, true)
                .addPersistent("size", PersistentDataType.INTEGER, size)
                .build();
        player.getInventory().addItem(itemStack);

        return true;
    }
}
