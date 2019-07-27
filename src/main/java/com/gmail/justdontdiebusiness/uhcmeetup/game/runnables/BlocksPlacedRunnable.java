package com.gmail.justdontdiebusiness.uhcmeetup.game.runnables;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class BlocksPlacedRunnable extends BukkitRunnable {

    private Block block;

    public BlocksPlacedRunnable(Block block) {
        this.block = block;
    }

    @Override
    public void run() {
        block.setType(Material.AIR);
    }
}
