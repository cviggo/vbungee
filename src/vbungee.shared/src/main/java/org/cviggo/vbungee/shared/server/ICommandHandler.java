package org.cviggo.vbungee.shared.server;

import org.simpleframework.http.Query;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by viggo on 25-07-2014.
 */
public interface ICommandHandler {
    void handleCommand(String command, Query query, CountDownLatch latch, Request request, Response response) throws IOException;
}
