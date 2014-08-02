package org.cviggo.vbungee.shared.server;

/**
 * Created by viggo on 25-07-2014.
 */
public class ServerInfo {
    public String host;
    public int port;
    public String apiKey;

    public ServerInfo(String host, int port, String apiKey) {
        this.host = host;
        this.port = port;
        this.apiKey = apiKey;
    }
}
