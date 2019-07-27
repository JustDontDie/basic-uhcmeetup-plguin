package com.gmail.justdontdiebusiness.uhcmeetup.game.runnables;

import com.gmail.justdontdiebusiness.uhcmeetup.game.Game;
import com.gmail.justdontdiebusiness.uhcmeetup.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class CountdownRunnable extends BukkitRunnable {

    private Game game;
    private int seconds;

    public CountdownRunnable(Game game) {
        this.game = game;
        this.seconds = -1;
    }

    public void run() {
        game.setGameState(GameState.COUNTDOWN);

        if (seconds % 10 == 0 || (seconds > 0 && seconds <= 5)) {
            Bukkit.broadcastMessage(ChatColor.GREEN + "Game starts in " + seconds + " seconds...");
        }

        if (seconds == 0) {
            Bukkit.broadcastMessage(ChatColor.GREEN + "Game has started! Good luck :)");
            game.setGameState(GameState.STARTED);
            this.cancel();
            return;
        }

        seconds--;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
