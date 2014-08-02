package org.cviggo.configs;

import org.cviggo.configlibrary.Config;

import java.io.File;
import java.util.List;

public class MainConfig {
    public static String configpath = File.separator + "plugins" + File.separator + "BungeeSuite" + File.separator + "config.yml";
    public static Config config = new Config( configpath );

    /* MySQL Information */
    public static String host = config.getString( "Database.Host", "localhost" );
    public static String database = config.getString( "Database.Database", "minecraft" );
    public static String port = config.getString( "Database.Port", "3306" );
    public static String username = config.getString( "Database.Username", "username" );
    public static String password = config.getString( "Database.Password", "password" );
    public static int threads = config.getInt( "Database.Threads", 5 );
    public static boolean motd = config.getBoolean( "MOTD.Enabled", true );
    public static boolean newPlayerBroadcast = config.getBoolean( "NewPlayerBroadcast", true );
    public static boolean broadcastProxyConnectionMessages = config.getBoolean( "BroadcastProxyConnectionMessages", true );
    public static int playerDisconnectDelay = config.getInt( "PlayerDisconnectDelay", 10 );
    public static int vBungeeEngineListenPort = config.getInt( "vBungee.vBungeeEngineListenPort", 7000 );
    public static String vBungeeEngineApiKey = config.getString( "vBungee.vBungeeEngineApiKey", "changeme" );
    public static List<String> vBungeeServers = config.getSubNodes("vBungee.servers");

    public static void reloadConfig() {
        config = null;
        config = new Config( configpath );
        motd = config.getBoolean( "MOTD.Enabled", true );
        newPlayerBroadcast = config.getBoolean( "NewPlayerBroadcast", true );
        broadcastProxyConnectionMessages = config.getBoolean( "BroadcastProxyConnectionMessages", true );
        playerDisconnectDelay = config.getInt( "PlayerDisconnectDelay", 10 );
    }
}
