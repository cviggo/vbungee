package org.cviggo.bungeesuiteextensions.Commands;

import org.cviggo.main.BungeeSuite;
import org.cviggo.managers.ConsoleCommandManager;
import org.cviggo.vbungee.shared.server.ICommandHandler;
import org.simpleframework.http.Query;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by viggo on 02-08-2014.
 */
public class ConsoleCommandRequestCommand implements ICommandHandler {
    @Override
    public void handleCommand(String command, Query query, CountDownLatch latch, Request request, Response response) throws IOException {
        try {
            final String scope = query.get("scope");
            final String commandToRequest = query.get("commandToRequest");
            final boolean success = ConsoleCommandManager.ExecuteCommand(scope, commandToRequest);
            BungeeSuite.instance.getLogger().info("ConsoleCommandManager executed command: " + commandToRequest + ", success: " + success);
        }catch (Throwable t){
            BungeeSuite.instance.getLogger().severe(t.getMessage());
        }finally {
            latch.countDown();
        }
    }
}
