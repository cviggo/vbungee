package org.cviggo.main;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;
import org.cviggo.bungeesuiteextensions.Commands.*;
import org.cviggo.bungeesuiteextensions.ServerManager;
import org.cviggo.commands.BSVersionCommand;
import org.cviggo.commands.MOTDCommand;
import org.cviggo.commands.ReloadCommand;
import org.cviggo.commands.VBCommand;
import org.cviggo.configs.MainConfig;
import org.cviggo.listeners.*;
import org.cviggo.managers.*;
import org.cviggo.vbungee.shared.Logger;
import org.cviggo.vbungee.shared.server.Engine;

import java.sql.SQLException;
import java.util.List;

public class BungeeSuite extends Plugin {
    public static BungeeSuite instance;
    public static ProxyServer proxy;

    Engine engine;
    private Logger logger;

    public void onEnable() {
        instance = this;
        LoggingManager.log(ChatColor.GREEN + "Starting BungeeSuite");
        proxy = ProxyServer.getInstance();
        LoggingManager.log( ChatColor.GREEN + "Initialising Managers" );
        initialiseManagers();
        registerListeners();
        registerCommands();
        reloadServersPlugins();

        logger = new Logger(getLogger(), getDataFolder().getPath());
        engine = new Engine(logger, MainConfig.vBungeeEngineListenPort, MainConfig.vBungeeEngineApiKey, 5000);

        // /warp command
        engine.registerCommandHandler("sendPlayerToWarp", new SendPlayerToWarpCommand());

        // /home command
        engine.registerCommandHandler("sendPlayerToHome", new SendPlayerToHomeCommand());

        // tp commands
        engine.registerCommandHandler("teleportPlayerToPlayer", new TeleportPlayerToPlayerCommand());
        engine.registerCommandHandler("teleportPlayerToLocation", new TeleportPlayerToLocationCommand());

        // spawn / hub commands
        engine.registerCommandHandler("sendPlayerToProxySpawn", new SendPlayerToProxySpawnCommand());

        // portals
        engine.registerCommandHandler("teleportPlayer", new TeleportPlayerCommand());

        // back
        engine.registerCommandHandler("sendPlayerToLastBack", new SendPlayerToLastBackCommand());

        engine.registerCommandHandler("ConsoleCommandRequest", new ConsoleCommandRequestCommand());


        /* servers */
        final List<String> vBungeeServers = MainConfig.vBungeeServers;

        if (vBungeeServers != null){
            for (String vBungeeServer : vBungeeServers) {
                final String host = MainConfig.config.getString("vBungee.servers." + vBungeeServer + ".ip", "localhost");
                final int port = MainConfig.config.getInt("vBungee.servers." + vBungeeServer + ".port", 0);
                final String apiKey = MainConfig.config.getString("vBungee.servers." + vBungeeServer + ".apiKey", "changeme");

                ServerManager.registerServer(vBungeeServer, new org.cviggo.vbungee.shared.server.ServerInfo(host, port, apiKey));
                logger.logInfo("Registered vBungeeServer. Name: " + vBungeeServer + ", Host: " + host + ", port: " + port);
            }
        }
    }

    private void registerCommands() {
        //        proxy.getPluginManager().registerCommand( this, new WhoIsCommand() );
        proxy.getPluginManager().registerCommand( this, new BSVersionCommand() );
        proxy.getPluginManager().registerCommand( this, new MOTDCommand() );
        proxy.getPluginManager().registerCommand( this, new ReloadCommand() );
        proxy.getPluginManager().registerCommand( this, new VBCommand() );
    }

    private void initialiseManagers() {
        if ( SQLManager.initialiseConnections() ) {
            DatabaseTableManager.createDefaultTables();
            AnnouncementManager.loadAnnouncements();
            //            if ( MainConfig.UserSocketPort ) {
            //                SocketManager.startServer();
            //            }
            ChatManager.loadChannels();
            PrefixSuffixManager.loadPrefixes();
            PrefixSuffixManager.loadSuffixes();
            TeleportManager.initialise();
            try {
                WarpsManager.loadWarpLocations();
                PortalManager.loadPortals();
                SpawnManager.loadSpawns();
            } catch ( SQLException e ) {
                e.printStackTrace();
            }
            //test
        } else {
            //            setupSQL();
            LoggingManager.log( ChatColor.DARK_RED + "Your BungeeSuite is unable to connect to your SQL database specified in the config" );
        }
    }

    void registerListeners() {
        this.getProxy().registerChannel( "BSChat" );//in
        this.getProxy().registerChannel( "BungeeSuiteChat" );//out
        this.getProxy().registerChannel( "BSBans" );//in
        this.getProxy().registerChannel( "BungeeSuiteBans" ); //out
        this.getProxy().registerChannel( "BSTeleports" );//in
        this.getProxy().registerChannel( "BungeeSuiteTP" );//out
        this.getProxy().registerChannel( "BSWarps" );//in
        this.getProxy().registerChannel( "BungeeSuiteWarps" );//out
        this.getProxy().registerChannel( "BSHomes" );//in
        this.getProxy().registerChannel( "BungeeSuiteHomes" );//out
        this.getProxy().registerChannel( "BSPortals" );//in
        this.getProxy().registerChannel( "BungeeSuitePorts" );//out
        this.getProxy().registerChannel( "BSSpawns" );//in
        this.getProxy().registerChannel( "BungeeSuiteSpawn" );//out
        proxy.getPluginManager().registerListener( this, new PlayerListener() );
        proxy.getPluginManager().registerListener( this, new ChatListener() );
        proxy.getPluginManager().registerListener( this, new ChatMessageListener() );
        proxy.getPluginManager().registerListener( this, new BansMessageListener() );
        proxy.getPluginManager().registerListener( this, new BansListener() );
        proxy.getPluginManager().registerListener( this, new TeleportsMessageListener() );
        proxy.getPluginManager().registerListener( this, new WarpsMessageListener() );
        proxy.getPluginManager().registerListener( this, new HomesMessageListener() );
        proxy.getPluginManager().registerListener( this, new PortalsMessageListener() );
        proxy.getPluginManager().registerListener( this, new SpawnListener() );
        proxy.getPluginManager().registerListener( this, new SpawnMessageListener() );
    }


    private void reloadServersPlugins() {
        for ( ServerInfo s : ProxyServer.getInstance().getServers().values() ) {
            ChatManager.checkForPlugins( s );
        }
    }

    public void onDisable() {
        SQLManager.closeConnections();
    }
}

