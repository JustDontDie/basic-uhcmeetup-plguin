package com.gmail.justdontdiebusiness.uhcmeetup.game.runnables;

import com.gmail.justdontdiebusiness.uhcmeetup.game.Game;
import com.gmail.justdontdiebusiness.uhcmeetup.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class MessagesRunnable extends BukkitRunnable {

    private Game game;

    public MessagesRunnable(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        if (game.getGameState() == GameState.LOBBY) {
            Bukkit.broadcastMessage(ChatColor.RED + "Need " + game.getMinPlayers() + " players to start!");
        }
    }
}
