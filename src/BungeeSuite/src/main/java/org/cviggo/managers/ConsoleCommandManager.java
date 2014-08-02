package org.cviggo.managers;

import org.cviggo.bungeesuiteextensions.ServerManager;
import org.cviggo.main.BungeeSuite;
import org.cviggo.vbungee.shared.client.Client;
import org.cviggo.vbungee.shared.server.ServerInfo;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by viggo on 02-08-2014.
 */
public class ConsoleCommandManager {
    public static boolean ExecuteCommand(String scope, String command) {

        final Collection<ServerInfo> servers = ServerManager.getServers();

        final HashMap<String, String> map = new HashMap<>();
        map.put("cmd", command);

        if ("all".equals(scope)){
            for (ServerInfo server : servers) {
                Client.request(server, "ConsoleCommand", map);
            }
            return true;
        }

        if ("bungee".equals(scope)){
            return BungeeSuite.instance.getProxy().getPluginManager().dispatchCommand(BungeeSuite.instance.getProxy().getConsole(), command);
        }

        final ServerInfo server = ServerManager.getServer(scope);

        if (server != null){
            Client.request(server, "ConsoleCommand", map);
            return true;
        }

        return false;
    }
}
