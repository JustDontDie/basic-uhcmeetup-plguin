package com.gmail.justdontdiebusiness.uhcmeetup.game.player;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class GamePlayer {

    private Player player;

    private int kills;
    private boolean dead;

    public GamePlayer(Player player) {
        this.player = player;

        this.kills = 0;
        this.dead = false;
    }

    public Player getPlayer() {
        return player;
    }

    public int getKills() {
        return kills;
    }

    public void addKills() {
        kills += 1;
    }

    public int getPing() {
        return ((CraftPlayer)player).getHandle().ping;
    }

    public boolean isDead() {
        return dead;
    }
}
