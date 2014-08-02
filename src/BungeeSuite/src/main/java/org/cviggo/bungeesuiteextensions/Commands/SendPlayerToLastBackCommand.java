package org.cviggo.bungeesuiteextensions.Commands;

import org.cviggo.managers.PortalManager;
import org.cviggo.managers.TeleportManager;
import org.cviggo.vbungee.shared.server.ICommandHandler;
import org.simpleframework.http.Query;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class SendPlayerToLastBackCommand implements ICommandHandler {
    @Override
    public void handleCommand(String command, Query query, CountDownLatch latch, Request request, Response response) throws IOException {
        TeleportManager.sendPlayerToLastBackInternal(
                query.get("player"),
                Boolean.parseBoolean(query.get("death")),
                Boolean.parseBoolean(query.get("teleport"))
        );
        latch.countDown();
    }
}
