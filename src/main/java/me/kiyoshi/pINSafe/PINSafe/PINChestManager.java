package me.kiyoshi.pINSafe.PINSafe;

import me.kiyoshi.pINSafe.ConfigLoad;
import me.kiyoshi.pINSafe.util.ItemBuilder;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
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

    private PINChestManager() {
    }

    public final List<PINChest> registerPINChestList = new ArrayList<PINChest>();
    public final Map<Player, WaitingPINChest> waitingPINChestList = new HashMap<>();

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
        Inventory inventory = Bukkit.createInventory(null, InventoryType.WORKBENCH, ConfigLoad.create_password);
        inventory.setItem(0, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE, 1)
                .setName(ConfigLoad.save_password)
                .addPersistent("save_password_button", PersistentDataType.BOOLEAN, true)
                .build());
        player.openInventory(inventory);
        player.playSound(player.getLocation(), ConfigLoad.open_password_menu_sound, ConfigLoad.open_password_menu_volume, ConfigLoad.open_password_menu_speed);
        new WaitingPINChest(block.getLocation(), block.getBlockData(), player);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (event.getItemInHand().getItemMeta().getPersistentDataContainer().has(NamespacedKey.fromString("pin_chest"))) {
            event.setCancelled(true);
            placePinChest(event.getBlock(), event.getPlayer());
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getItem(event.getSlot()) != null) {
            if (event.getInventory().getItem(event.getSlot()).getItemMeta() != null) {
                if (event.getInventory().getItem(event.getSlot()).getItemMeta().getPersistentDataContainer().has(NamespacedKey.fromString("block_slot"))) {
                    event.setCancelled(true);
                }
            }
        }
            if (event.getInventory().getType() == InventoryType.WORKBENCH) {
                if (event.getInventory().getItem(0) != null) {
                    if (event.getInventory().getItem(0).getItemMeta() != null) {
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
                                    if (player.getInventory().getItemInMainHand().getItemMeta() != null) {
                                        if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(NamespacedKey.fromString("pin_chest"))) {
                                            size = player.getInventory().getItemInMainHand().getPersistentDataContainer().get(NamespacedKey.fromString("size"), PersistentDataType.INTEGER);
                                            if (player.getInventory().getItemInMainHand().getAmount() <= 1) {
                                                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                                            } else {
                                                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                                            }
                                        }
                                    }
                                    if (player.getInventory().getItemInOffHand().getItemMeta() != null) {
                                        if (player.getInventory().getItemInOffHand().getItemMeta().getPersistentDataContainer().has(NamespacedKey.fromString("pin_chest"))) {
                                            size = player.getInventory().getItemInOffHand().getPersistentDataContainer().get(NamespacedKey.fromString("size"), PersistentDataType.INTEGER);
                                            if (player.getInventory().getItemInOffHand().getAmount() <= 1) {
                                                player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                                            } else {
                                                player.getInventory().getItemInOffHand().setAmount(player.getInventory().getItemInOffHand().getAmount() - 1);
                                            }
                                        }
                                    }
                                    waitingPINChestList.get(player).getLocation().getBlock().setType(ConfigLoad.safe_material);
                                    waitingPINChestList.get(player).getLocation().getBlock().setBlockData(waitingPINChestList.get(player).getBlockData());
                                    PINChest pinChest = new PINChest(password, player.getUniqueId(), waitingPINChestList.get(player).getLocation(), size);
                                    player.openInventory(pinChest.getInventory());
                                    player.playSound(pinChest.getLocation(), ConfigLoad.safe_open_sound, ConfigLoad.safe_open_volume, ConfigLoad.safe_open_speed);
                                    waitingPINChestList.remove(player);
                                }
                            } else if (event.getSlot() <= 10) {
                                int slot = event.getSlot();
                                if (eventInventory.getItem(slot) == null) {
                                    eventInventory.setItem(slot, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1).setName(" ").build());
                                } else {
                                    eventInventory.setItem(slot, null);
                                }
                            }
                        } else if (event.getInventory().getItem(0).getItemMeta().getPersistentDataContainer().has(NamespacedKey.fromString("open_chest_button"))) {
                            event.setCancelled(true);
                            if (event.getSlot() == 0) {
                                StringBuilder stringBuilder = new StringBuilder();
                                for (int i = 1; i < 10; i++) {
                                    if (event.getInventory().getItem(i) == null) {
                                        stringBuilder.append("0");
                                    } else {
                                        stringBuilder.append("1");
                                    }
                                }
                                String password = stringBuilder.toString();
                                if (password.equals(event.getInventory().getItem(0).getItemMeta().getPersistentDataContainer().get(NamespacedKey.fromString("password"), PersistentDataType.STRING))) {
                                    Player player = (Player) event.getWhoClicked();
                                    List<String> locationString = List.of(event.getInventory().getItem(0).getPersistentDataContainer().get(NamespacedKey.fromString("location"), PersistentDataType.STRING).toString().split(","));
                                    World world = Bukkit.getWorld(locationString.get(0));
                                    int x = Integer.parseInt(locationString.get(1));
                                    int y = Integer.parseInt(locationString.get(2));
                                    int z = Integer.parseInt(locationString.get(3));
                                    Location location = new Location(world, x, y, z);
                                    PINChest pinChest = getPINChestByLocation(location);
                                    player.openInventory(pinChest.getInventory());
                                    player.playSound(pinChest.getLocation(), ConfigLoad.safe_open_sound, ConfigLoad.safe_open_volume, ConfigLoad.safe_open_speed);
                                }
                                else {
                                    Player player = (Player) event.getWhoClicked();
                                    player.playSound(player.getLocation(), ConfigLoad.invalid_password_sound, ConfigLoad.invalid_password_volume, ConfigLoad.invalid_password_speed);
                                }
                            } else if (event.getSlot() <= 10) {
                                int slot = event.getSlot();
                                if (event.getInventory().getItem(slot) == null) {
                                    event.getInventory().setItem(slot, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1).setName(" ").build());
                                } else {
                                    event.getInventory().setItem(slot, null);
                                }
                            }
                        }
                    }
                }
            }
    }
    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        PINChest pinChest = getPINChestByLocation(event.getBlock().getLocation());
        if (pinChest != null) {
            registerPINChestList.remove(pinChest);
        }
    }

    @EventHandler
    public void onOpen(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            Block block = event.getClickedBlock();
            if (block.getType() == ConfigLoad.safe_material) {
                PINChest pinChest = getPINChestByLocation(block.getLocation());
                if (pinChest != null) {
                    event.setCancelled(true);
                    player.closeInventory();
                    Inventory inventory = Bukkit.createInventory(null, InventoryType.WORKBENCH, ConfigLoad.enter_password);
                    Location location = pinChest.getLocation();
                    String stringLocation = location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
                    inventory.setItem(0, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE, 1)
                            .setName(ConfigLoad.try_open_safe)
                            .addPersistent("open_chest_button", PersistentDataType.BOOLEAN, true)
                            .addPersistent("password", PersistentDataType.STRING, pinChest.getPassword())
                            .addPersistent("location", PersistentDataType.STRING, stringLocation)
                            .build());
                    player.openInventory(inventory);
                    player.playSound(player.getLocation(), ConfigLoad.open_password_menu_sound, ConfigLoad.open_password_menu_volume, ConfigLoad.open_password_menu_speed);
                }
            }
        }
    }
}
