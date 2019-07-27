package com.gmail.justdontdiebusiness.uhcmeetup.game.runnables;

import com.gmail.justdontdiebusiness.uhcmeetup.game.Game;
import com.gmail.justdontdiebusiness.uhcmeetup.game.player.GamePlayer;
import net.minecraft.server.v1_8_R3.ScoreboardObjective;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardRunnable extends BukkitRunnable {

    private Game game;
    private GamePlayer gamePlayer;

    public ScoreboardRunnable(Game game, GamePlayer gamePlayer) {
        this.game = game;
        this.gamePlayer = gamePlayer;
    }
    @Override
    public void run() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("uhcmeetup", "");

        objective.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "UHC" + ChatColor.RED + "" + ChatColor.BOLD + "MEETUP");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        objective.getScore(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "------------------------").setScore(10);
        objective.getScore("").setScore(9);
        objective.getScore(ChatColor.GREEN + "Name" + ChatColor.WHITE + ": " + gamePlayer.getPlayer().getName()).setScore(8);
        objective.getScore(" ").setScore(7);
        objective.getScore(ChatColor.GREEN + "Players Alive" + ChatColor.WHITE + ": " + game.getPlayers().size() + "/" + game.getMaxPlayers()).setScore(6);
        objective.getScore("  ").setScore(5);
        objective.getScore(ChatColor.GREEN + "Kills" + ChatColor.WHITE + ": " + gamePlayer.getKills()).setScore(4);
        objective.getScore("   ").setScore(3);
        objective.getScore(ChatColor.GREEN + "Time" + ChatColor.WHITE + ": " + game.getTime()).setScore(2);
        objective.getScore("    ").setScore(1);
        objective.getScore(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "------------------------ ").setScore(0);

        if (gamePlayer.getPlayer().getScoreboard() != scoreboard) {
            gamePlayer.getPlayer().setScoreboard(scoreboard);
        }
    }
}
