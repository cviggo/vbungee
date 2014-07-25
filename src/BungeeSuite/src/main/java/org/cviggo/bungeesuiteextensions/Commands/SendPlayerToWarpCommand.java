package org.cviggo.bungeesuiteextensions.Commands;

import org.cviggo.vbungee.shared.server.ICommandHandler;
import org.simpleframework.http.Query;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by viggo on 25-07-2014.
 */
public class SendPlayerToWarpCommand implements ICommandHandler {
    @Override
    public void handleCommand(String command, Query query, CountDownLatch latch, Request request, Response response) throws IOException {
        //WarpsManager.sendPlayerToWarpInternal(query.get("sender"), query.get("player"), query.get("warp"), query.getBoolean("permission"), query.getBoolean("bypass"));
        latch.countDown();
    }
}
