package me.kiyoshi.pINSafe.PINSafe;

import me.kiyoshi.pINSafe.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PINChestManager implements Listener {

    public static final PINChestManager instance = new PINChestManager();

    private PINChestManager() {}

    public final List<PINChest> registerPINChestList = new ArrayList<PINChest>();
    public final Map<Player ,WaitingPINChest> waitingPINChestList = new HashMap<>();

    @Nullable
    public PINChest getPINChestByLocation(Location location) {
        for (PINChest pinChest : registerPINChestList) {
            if (location.getX() == pinChest.getLocation().getX()
                && location.getY() == pinChest.getLocation().getY()
                && location.getZ() == pinChest.getLocation().getZ()
                && pinChest.getLocation().getWorld().getName().equals(location.getWorld().getName())) {
                return pinChest;
            }
        }
        return null;
    }

    public void placePinChest(Block block, Player player) {
        Inventory inventory = Bukkit.createInventory(null, InventoryType.WORKBENCH, "Создайте пароль: ");
        inventory.setItem(0, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE, 1)
                .setName("&r&aСохранить PIN код")
                .addPersistent("save_password_button", PersistentDataType.BOOLEAN, true)
                .build());
        player.openInventory(inventory);
        new WaitingPINChest(block.getLocation(), block.getBlockData(), player);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (event.getItemInHand().getItemMeta().getPersistentDataContainer().has(NamespacedKey.fromString("PINChest"))) {
            event.setCancelled(true);
            placePinChest(event.getBlock(), event.getPlayer());
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getType() == InventoryType.WORKBENCH) {
            if (event.getInventory().getItem(0).getItemMeta().getPersistentDataContainer().has(NamespacedKey.fromString("save_password_button"))) {
                Player player = (Player) event.getWhoClicked();
                Inventory eventInventory = event.getInventory();
                event.setCancelled(true);
                if (event.getSlot() == 0) {
                    int cnt = 0;
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 1; i < 10; i++) {
                        if (eventInventory.getItem(i) != null) {
                            cnt++;
                            stringBuilder.append("1");
                        } else {
                            stringBuilder.append("0");
                        }
                    }
                    String password = stringBuilder.toString();
                    if (cnt > 1) {
                        player.closeInventory();
                        int size = 9;
                        if (player.getInventory().getItemInMainHand().getItemMeta() != null || player.getInventory().getItemInOffHand().getItemMeta() != null) {
                            if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(NamespacedKey.fromString("PINChest"))) {
                                size = player.getInventory().getItemInMainHand().getPersistentDataContainer().get(NamespacedKey.fromString("size"), PersistentDataType.INTEGER);
                                if (player.getInventory().getItemInMainHand().getAmount() <= 1) {
                                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                                }
                                else {
                                    player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                                }
                            }
                            else if (player.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(NamespacedKey.fromString("PINChest"))) {
                                size = player.getInventory().getItemInOffHand().getPersistentDataContainer().get(NamespacedKey.fromString("size"), PersistentDataType.INTEGER);
                                if (player.getInventory().getItemInOffHand().getAmount() <= 1) {
                                    player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                                }
                                else {
                                    player.getInventory().getItemInOffHand().setAmount(player.getInventory().getItemInOffHand().getAmount() - 1);
                                }
                            }
                            waitingPINChestList.get(player).getLocation().getBlock().setType(Material.ENDER_CHEST);
                            PINChest pinChest = new PINChest(password, player.getUniqueId().toString(), waitingPINChestList.get(player).getLocation(), size);
                            player.openInventory(pinChest.getInventory());
                            waitingPINChestList.remove(player);
                        }
                    }
                }
            }
        }
    }

}
