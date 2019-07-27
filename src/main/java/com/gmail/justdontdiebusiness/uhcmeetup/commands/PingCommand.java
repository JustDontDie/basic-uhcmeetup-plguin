package com.gmail.justdontdiebusiness.uhcmeetup.commands;

import com.gmail.justdontdiebusiness.uhcmeetup.game.Game;
import com.gmail.justdontdiebusiness.uhcmeetup.game.player.GamePlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingCommand implements CommandExecutor {

    private Game game;

    public PingCommand(Game game) {
        this.game = game;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("really? -_-");
            return true;
        }
        Player player = (Player) commandSender;
        GamePlayer gamePlayer = game.getPlayer(player);

        player.sendMessage(ChatColor.GREEN + "Your Ping" + ChatColor.WHITE + ": " + gamePlayer.getPing() + "ms");
        return true;
    }
}
