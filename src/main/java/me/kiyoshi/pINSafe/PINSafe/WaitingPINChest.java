package me.kiyoshi.pINSafe.PINSafe;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

public class WaitingPINChest {

    private final Location location;
    private final BlockData blockData;

    public WaitingPINChest(Location location, BlockData blockData, Player player) {
        this.location = location;
        this.blockData = blockData;

        PINChestManager.instance.waitingPINChestList.put(player, this);
    }

    public Location getLocation() {
        return location;
    }

    public BlockData getBlockData() {
        return blockData;
    }
}
