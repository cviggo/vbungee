package org.cviggo.vbungee;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class Plugin extends JavaPlugin implements Listener, PluginMessageListener {

    @Override
    public void onEnable() {
        getServer().getPluginManager()
                .registerEvents(new VotifierListener(this), this);
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {

    }
}
