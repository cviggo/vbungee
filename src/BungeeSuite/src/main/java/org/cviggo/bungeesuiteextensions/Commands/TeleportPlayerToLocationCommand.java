package org.cviggo.bungeesuiteextensions.Commands;

import org.cviggo.managers.TeleportManager;
import org.cviggo.managers.WarpsManager;
import org.cviggo.vbungee.shared.server.ICommandHandler;
import org.simpleframework.http.Query;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by viggo on 26-07-2014.
 */
public class TeleportPlayerToLocationCommand implements ICommandHandler {
    @Override
    public void handleCommand(String command, Query query, CountDownLatch latch, Request request, Response response) throws IOException {
        TeleportManager.teleportPlayerToLocationInternal(
                query.get("p"),
                query.get("server"),
                query.get("world"),
                Double.parseDouble(query.get("x")),
                Double.parseDouble(query.get("y")),
                Double.parseDouble(query.get("z")),
                Float.parseFloat(query.get("yaw")),
                Float.parseFloat(query.get("pitch"))
        );
        latch.countDown();
    }
}
