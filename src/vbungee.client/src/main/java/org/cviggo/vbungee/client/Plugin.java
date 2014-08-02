package org.cviggo.vbungee.client;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.cviggo.vbungee.client.commands.ConsoleCommand;
import org.cviggo.vbungee.client.commands.SyncPlayerDataCommand;
import org.cviggo.vbungee.shared.Logger;
import org.cviggo.vbungee.shared.Utils;
import org.cviggo.vbungee.shared.client.Client;
import org.cviggo.vbungee.shared.server.Engine;
import org.cviggo.vbungee.shared.server.ServerInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Plugin extends JavaPlugin implements Listener, PluginMessageListener {

    private String apiKey;
    private int serverPort;
    public Logger logger;
    private Engine engine;
    private int commandTimeout;
    private ServerInfo bungeeServerInfo;

    @Override
    public void onEnable() {

        // make sure datafolder is present
        getDataFolder().mkdirs();

        loadSettings();

        logger = new Logger(getLogger(), getDataFolder().toString());
        engine = new Engine(logger, getServerPort(), getApiKey(), commandTimeout);
        engine.registerCommandHandler("SyncPlayerData", new SyncPlayerDataCommand(this, getServer().getScheduler()));
        engine.registerCommandHandler("ConsoleCommand", new ConsoleCommand(this, getServer().getScheduler()));

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
        bungeeServerInfo = new ServerInfo(
            getConfig().getString("bungeeHost"),
            getConfig().getInt("bungeePort"),
            getConfig().getString("bungeeApiKey")
        );
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("vb")) {

            if (args != null && args.length > 0){

                final ArrayList<String> strings = new ArrayList<>(Arrays.asList(args));
                final String command = strings.remove(0);

                if ("cmd".equals(command)){
                    if (strings.size() >= 2){
                        final String scope = strings.remove(0);
                        final String commandToRequest = Utils.join(strings, " ");
                        final HashMap<String, String> map = new HashMap<>();
                        map.put("scope", scope);
                        map.put("commandToRequest", commandToRequest);

                        Client.request(getBungeeServerInfo(), "ConsoleCommandRequest", map);
                    }
                }

                return true;
            }
        }

        return false;
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

    public ServerInfo getBungeeServerInfo() {
        return bungeeServerInfo;
    }
}
