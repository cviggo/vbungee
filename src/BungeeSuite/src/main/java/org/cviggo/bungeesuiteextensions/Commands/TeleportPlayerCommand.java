package org.cviggo.bungeesuiteextensions.Commands;

import org.cviggo.managers.PortalManager;
import org.cviggo.managers.SpawnManager;
import org.cviggo.vbungee.shared.server.ICommandHandler;
import org.simpleframework.http.Query;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by viggo on 26-07-2014.
 */
public class TeleportPlayerCommand implements ICommandHandler {

    @Override
    public void handleCommand(String command, Query query, CountDownLatch latch, Request request, Response response) throws IOException {
        PortalManager.teleportPlayerInternal(query.get("p"), query.get("type"), query.get("dest"), Boolean.parseBoolean(query.get("perm")));
        latch.countDown();
    }
}
