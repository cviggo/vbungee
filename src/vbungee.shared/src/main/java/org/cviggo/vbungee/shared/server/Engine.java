package org.cviggo.vbungee.shared.server;

import org.cviggo.vbungee.shared.Logger;
import org.simpleframework.http.core.ContainerServer;
import org.simpleframework.transport.Server;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Created by viggo on 25-07-2014.
 */
public class Engine {
    private final String apiKey;
    private final int commandTimeoutMsec;
    private final Logger logger;
    private final int serverPort;
    RequestHandler requestHandler;
    Server server;
    Connection connection;
    SocketAddress address;

    public Engine(Logger logger, int serverPort, String apiKey, int commandTimeoutMsec) {
        this.logger = logger;
        this.serverPort = serverPort;
        this.apiKey = apiKey;
        this.commandTimeoutMsec = commandTimeoutMsec;
        initServer();
    }


    private void initServer() {

        try {
            requestHandler = new RequestHandler(logger, this, apiKey, commandTimeoutMsec);
            server = new ContainerServer(requestHandler);
            connection = new SocketConnection(server);
            address = new InetSocketAddress(serverPort);
            connection.connect(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerCommandHandler(String command, ICommandHandler commandHandler) {
        requestHandler.registerCommand(command, commandHandler);
    }

    public void stop() {
        try {

            logger.logInfo("Stopping engine.");

            connection.close();
            server.stop();

            logger.logInfo("Engine stopped.");

        } catch (IOException e) {
            logger.logSevere(e);
        }
    }
}
