package com.gmail.justdontdiebusiness.uhcmeetup;

import com.gmail.justdontdiebusiness.uhcmeetup.game.Game;
import org.bukkit.plugin.java.JavaPlugin;

public class UHCMeetup extends JavaPlugin {

    @Override
    public void onEnable() {
        new Game(this);
    }

    @Override
    public void onDisable() {
        Game.getGame().stop();
    }
}
