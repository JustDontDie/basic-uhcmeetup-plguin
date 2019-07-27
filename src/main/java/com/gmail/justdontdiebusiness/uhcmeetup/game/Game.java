package com.gmail.justdontdiebusiness.uhcmeetup.game;

import com.gmail.justdontdiebusiness.uhcmeetup.UHCMeetup;
import com.gmail.justdontdiebusiness.uhcmeetup.commands.PingCommand;
import com.gmail.justdontdiebusiness.uhcmeetup.game.player.GamePlayer;
import com.gmail.justdontdiebusiness.uhcmeetup.game.runnables.MessagesRunnable;
import com.gmail.justdontdiebusiness.uhcmeetup.game.runnables.ScoreboardRunnable;
import com.gmail.justdontdiebusiness.uhcmeetup.listeners.PlayerListeners;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private static Game game;

    private UHCMeetup plugin;

    private GameState gameState;

    private int minPlayers;
    private int maxPlayers;

    private List<GamePlayer> players;

    private int minutes;
    private int seconds;


    public Game(UHCMeetup plugin) {
        game = this;

        this.gameState = GameState.STARTING;
        this.plugin = plugin;

        this.plugin.getServer().getPluginCommand("ping").setExecutor(new PingCommand(this));
        this.plugin.getServer().getPluginManager().registerEvents(new PlayerListeners(this), plugin);

        this.minPlayers = 2;
        this.maxPlayers = 8;

        this.players = new ArrayList<>();

        new MessagesRunnable(this).runTaskTimer(getPlugin(), 0L, 200L);

        getPlugin().getServer().getWorld("world").setAnimalSpawnLimit(0);
        getPlugin().getServer().getWorld("world").setMonsterSpawnLimit(0);
        getPlugin().getServer().getWorld("world").setWaterAnimalSpawnLimit(0);
        getPlugin().getServer().getWorld("world").setAmbientSpawnLimit(0);

        for (Entity entity : getPlugin().getServer().getWorld("world").getEntities()) {
            if (!(entity instanceof Player)) {
                entity.remove();
            }
        }

        WorldBorder border = getPlugin().getServer().getWorld("world").getWorldBorder();
        border.setSize(300, 300);

        this.gameState = GameState.LOBBY;

        new BukkitRunnable() {
            @Override
            public void run() {
                if (getGameState() != GameState.STARTED) return;
                seconds++;

                if (seconds == 60) {
                    seconds -= 60;
                    minutes++;
                }
            }
        }.runTaskTimer(getPlugin(), 0L, 20L);
    }

    public void stop() {
        setGameState(GameState.ENDING);

        WorldBorder border = getPlugin().getServer().getWorld("world").getWorldBorder();
        border.reset();
    }

    public static Game getGame() {
        return game;
    }

    public UHCMeetup getPlugin() {
        return plugin;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public List<GamePlayer> getPlayers() {
        return players;
    }

    public GamePlayer getPlayer(Player player) {
        for (GamePlayer gamePlayer : getPlayers()) {
            if (gamePlayer.getPlayer().getUniqueId() == player.getUniqueId()) {
                return gamePlayer;
            }
        }
        return null;
    }

    public String getTime() {
        return (minutes <= 9 ? "0" + minutes : minutes) + ":" + (seconds <= 9 ? "0" + seconds : seconds);
    }
}
