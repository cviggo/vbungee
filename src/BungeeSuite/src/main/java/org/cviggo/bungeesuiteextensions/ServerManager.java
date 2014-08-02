package org.cviggo.bungeesuiteextensions;

import org.cviggo.vbungee.shared.server.ServerInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by viggo on 25-07-2014.
 */
public class ServerManager {

    private static Map<String, ServerInfo> servers = new HashMap<>();

    public static void registerServer(String name, ServerInfo serverInfo) {
        servers.put(name, serverInfo);
    }

    public static ServerInfo getServer(String name) {

        if (servers.containsKey(name)){
            return servers.get(name);
        }

        return null;
    }

    public static java.util.Collection<ServerInfo> getServers(){
        return servers.values();
    }
}
