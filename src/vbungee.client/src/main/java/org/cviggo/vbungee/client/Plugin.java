package org.cviggo.vbungee.client;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.cviggo.vbungee.client.commands.SyncPlayerDataCommand;
import org.cviggo.vbungee.shared.Logger;
import org.cviggo.vbungee.shared.server.Engine;

public class Plugin extends JavaPlugin implements Listener, PluginMessageListener {

    private String apiKey;
    private int serverPort;
    public Logger logger;
    private Engine engine;
    private int commandTimeout;

    @Override
    public void onEnable() {

        // make sure datafolder is present
        getDataFolder().mkdirs();

        loadSettings();

        logger = new Logger(getLogger(), getDataFolder().toString());
        engine = new Engine(logger, getServerPort(), getApiKey(), commandTimeout);
        engine.registerCommandHandler("SyncPlayerData", new SyncPlayerDataCommand(this, getServer().getScheduler()));

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(this, this);

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

        getLogger().info("Registered and enabled.");
    }

    @Override
    public void onDisable() {
        if (engine != null){
            engine.stop();
            engine = null;
        }
    }

    void loadSettings() {

        // make sure a config exists
        saveDefaultConfig();


        // save config to persist any new defaults added
        if (true /*saveNewDefaults*/) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        }

        reloadConfig();

        serverPort = getConfig().getInt("serverPort");
        apiKey = getConfig().getString("apiKey");
        commandTimeout = getConfig().getInt("commandTimeout");
    }

    @Override
    public void onPluginMessageReceived(String channel, Player randomInvalidPlayer, byte[] message) {

    }

    public void beginSavePlayer(String playerName) {

    }






    public String getApiKey() {
        return apiKey;
    }

    public int getServerPort() {
        return serverPort;
    }
}
