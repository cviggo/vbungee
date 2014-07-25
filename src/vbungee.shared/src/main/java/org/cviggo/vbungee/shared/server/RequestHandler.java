package org.cviggo.vbungee.shared.server;

import org.cviggo.vbungee.shared.Logger;
import org.simpleframework.http.Query;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.simpleframework.http.core.Container;

import java.io.PrintStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by viggo on 25-07-2014.
 */
public class RequestHandler implements Container {

    private final Logger logger;
    private final Engine engine;
    private final String apiKey;
    private final int maxCommandTimeoutMsec;
    private final ConcurrentMap<String, ICommandHandler> commands;

    public RequestHandler(Logger logger, Engine engine, String apiKey, int maxCommandTimeoutMsec) {
        this.logger = logger;
        this.engine = engine;
        this.apiKey = apiKey;
        this.maxCommandTimeoutMsec = maxCommandTimeoutMsec;
        commands = new ConcurrentHashMap<String, ICommandHandler>();
    }

    public void registerCommand(String command, ICommandHandler commandHandler) {

        if (command == null || commandHandler == null) {
            return;
        }

        commands.put(command, commandHandler);
    }

    @Override
    public void handle(Request request, Response response) {
        try {

            long time = System.currentTimeMillis();
            response.setValue("Content-Type", "text/html");
            response.setValue("Server", "VBungee");
            response.setDate("Date", time);
            response.setDate("Last-Modified", time);


            final Query query = request.getQuery();
            final String apiKeyReceived = query.containsKey("api-key") ? query.get("api-key") : null;

            if (!apiKey.equals(apiKeyReceived)) {
                logger.logWarn("Invalid api-key: " + apiKeyReceived + " received from: " + request.getClientAddress().toString());
                return;
            }

            final String command = query.get("command");

            if (command == null) {
                return;
            }

            final boolean doExecuteSynchronously = query.getBoolean("doExecuteSynchronously");
            final int maxWaitMsec = query.getInteger("maxWaitMsec");

            final ICommandHandler commandHandler = commands.get(command);

            if (commandHandler != null) {
                CountDownLatch latch = new CountDownLatch(1);

                commandHandler.handleCommand(command, query, latch, request, response);

                if (doExecuteSynchronously) {

                    if (!latch.await(Math.min(maxCommandTimeoutMsec, maxWaitMsec), TimeUnit.MILLISECONDS)) {
                        response.setStatus(Status.getStatus(501));
                        response.close();
                    }
                } else {
                    response.setStatus(Status.OK);
                    response.close();
                }
            }

            PrintStream responseBody = response.getPrintStream();
            responseBody.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
