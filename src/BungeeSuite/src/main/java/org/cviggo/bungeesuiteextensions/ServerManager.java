package org.cviggo.bungeesuiteextensions;

import java.util.HashMap;
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
        return servers.get(name);
    }
}
