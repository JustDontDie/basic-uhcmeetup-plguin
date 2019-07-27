package com.gmail.justdontdiebusiness.uhcmeetup.listeners;

import com.gmail.justdontdiebusiness.uhcmeetup.game.Game;
import com.gmail.justdontdiebusiness.uhcmeetup.game.GameState;
import com.gmail.justdontdiebusiness.uhcmeetup.game.player.GamePlayer;
import com.gmail.justdontdiebusiness.uhcmeetup.game.runnables.BlocksPlacedRunnable;
import com.gmail.justdontdiebusiness.uhcmeetup.game.runnables.CountdownRunnable;
import com.gmail.justdontdiebusiness.uhcmeetup.game.runnables.ScoreboardRunnable;
import com.gmail.justdontdiebusiness.uhcmeetup.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class PlayerListeners implements Listener {

    private Game game;
    private CountdownRunnable countdown;

    public PlayerListeners(Game game) {
        this.game = game;
        this.countdown = new CountdownRunnable(game);
    }

    private Game getGame() {
        return game;
    }

    @EventHandler
    public void onJoin(PlayerLoginEvent e) {
        if (getGame().getGameState() == GameState.STARTING) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Server is starting up!");
            return;
        }

        if (Bukkit.getOnlinePlayers().size() > getGame().getMaxPlayers()) {
            e.disallow(PlayerLoginEvent.Result.KICK_FULL, "Server is full!");
            return;
        }

        if (getGame().getGameState() != GameState.LOBBY) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Game has already started");
            return;
        }

        GamePlayer gamePlayer = new GamePlayer(e.getPlayer());
        getGame().getPlayers().add(gamePlayer);

        if (countdown.getSeconds() == -1) {
            if (getGame().getPlayers().size() >= getGame().getMinPlayers()) {
                this.countdown.setSeconds(30);
                this.countdown.runTaskTimer(getGame().getPlugin(), 0L, 20L);
            }
        } else {
            if (getGame().getPlayers().size() == getGame().getMaxPlayers()) {
                if (this.countdown.getSeconds() > 10) this.countdown.setSeconds(10);
            }
        }

        new ScoreboardRunnable(getGame(), gamePlayer).runTaskTimer(getGame().getPlugin(), 1L, 20L);
    }

    private Location safe() {
        Location safeLocation = new Location(getGame().getPlugin().getServer().getWorld("world"), getRandomNumber(), 200, getRandomNumber());
        safeLocation.setY(safeLocation.getWorld().getHighestBlockYAt(safeLocation));

        boolean safe = false;
        while (!safe) {
            if (safeLocation.getX() >= 150 || safeLocation.getZ() >= 150) {
                safeLocation = new Location(getGame().getPlugin().getServer().getWorld("world"), getRandomNumber(), 200, getRandomNumber());
                safeLocation.setY(safeLocation.getWorld().getHighestBlockYAt(safeLocation));

                continue;
            }

            boolean isSolid = true;

            for (int i = 0; i < 3; i++) {
                Location blockYBelow = new Location(safeLocation.getWorld(), safeLocation.getX(), safeLocation.getY() - i, safeLocation.getZ());

                if (blockYBelow.getBlock().getType() == Material.WATER || blockYBelow.getBlock().getType() == Material.STATIONARY_WATER
                || blockYBelow.getBlock().getType() == Material.LAVA || blockYBelow.getBlock().getType() == Material.STATIONARY_LAVA) isSolid = false;
            }

            if (!isSolid) {
                safeLocation = new Location(getGame().getPlugin().getServer().getWorld("world"), getRandomNumber(), 200, getRandomNumber());
                safeLocation.setY(safeLocation.getWorld().getHighestBlockYAt(safeLocation));

                continue;
            }
            safe = true;
        }
        return safeLocation;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.getPlayer().teleport(safe());
        e.getPlayer().getInventory().clear();

        ItemStack ironSword = new ItemBuilder(Material.IRON_SWORD).setEnchantments(Enchantment.DAMAGE_ALL, 2).build();
        ItemStack fishingRod = new ItemBuilder(Material.FISHING_ROD).build();
        ItemStack bow = new ItemBuilder(Material.BOW).build();
        ItemStack goldenApples = new ItemBuilder(Material.GOLDEN_APPLE, 4).build();
        ItemStack goldenApplesHead = new ItemBuilder(Material.GOLDEN_APPLE, 2).setName("Golden Head").build();
        ItemStack cobbleStone = new ItemBuilder(Material.COBBLESTONE, 32).build();
        ItemStack woodPlanks = new ItemBuilder(Material.WOOD, 32).build();
        ItemStack arrow = new ItemBuilder(Material.ARROW, 16).build();

        ItemStack helmet = new ItemBuilder(Material.DIAMOND_HELMET).setEnchantments(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build();
        ItemStack chestplate = new ItemBuilder(Material.DIAMOND_CHESTPLATE).setEnchantments(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build();
        ItemStack leggings = new ItemBuilder(Material.DIAMOND_LEGGINGS).setEnchantments(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build();
        ItemStack boots = new ItemBuilder(Material.DIAMOND_BOOTS).setEnchantments(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build();

        e.getPlayer().getInventory().setItem(0, ironSword);
        e.getPlayer().getInventory().setItem(1, fishingRod);
        e.getPlayer().getInventory().setItem(2, bow);
        e.getPlayer().getInventory().setItem(3, goldenApples);
        e.getPlayer().getInventory().setItem(4, goldenApplesHead);
        e.getPlayer().getInventory().setItem(5, cobbleStone);
        e.getPlayer().getInventory().setItem(6, woodPlanks);
        e.getPlayer().getInventory().setItem(8, arrow);

        e.getPlayer().getInventory().setHelmet(helmet);
        e.getPlayer().getInventory().setChestplate(chestplate);
        e.getPlayer().getInventory().setLeggings(leggings);
        e.getPlayer().getInventory().setBoots(boots);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (getGame().getGameState() != GameState.STARTED) e.setCancelled(true);

        new BlocksPlacedRunnable(e.getBlockPlaced()).runTaskLater(getGame().getPlugin(), 100L);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (getGame().getGameState() != GameState.STARTED || getGame().getGameState() != GameState.ENDING) {
            e.setTo(e.getFrom());
        }
    }

    @EventHandler
    public void onEat(PlayerInteractEvent e) {
        if (getGame().getGameState() != GameState.STARTED || getGame().getGameState() != GameState.ENDING) e.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (getGame().getGameState() != GameState.STARTED || getGame().getGameState() != GameState.ENDING) e.setCancelled(true);
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        if (getGame().getGameState() != GameState.STARTED || getGame().getGameState() != GameState.ENDING) e.setCancelled(true);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        Player killer = e.getEntity().getKiller();

        getGame().getPlayer(killer).addKills();
        player.kickPlayer("You have died! Better luck next time :)");

        if (getGame().getPlayers().size() == 1) {
            getGame().setGameState(GameState.ENDING);
            Bukkit.broadcastMessage(ChatColor.GREEN + killer.getName() + " has won the game!");
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        getGame().getPlayers().remove(getGame().getPlayer(e.getPlayer()));

        if (countdown.getSeconds() != -1) {
            if (getGame().getPlayers().size() < getGame().getMaxPlayers()) {
                Bukkit.getScheduler().cancelTask(countdown.getTaskId());
                Bukkit.broadcastMessage(ChatColor.RED + "Need 4 players to start the game!");
            }
        }
    }

    private int getRandomNumber() {
        int randomNumber = new Random().nextInt(145-5) + 5;

        System.out.println(randomNumber);

        return randomNumber;
    }
}
