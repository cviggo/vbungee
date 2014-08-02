package org.cviggo.vbungee.client.commands;

import org.bukkit.scheduler.BukkitScheduler;
import org.cviggo.vbungee.client.Plugin;
import org.cviggo.vbungee.shared.server.ICommandHandler;
import org.simpleframework.http.Query;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by viggo on 02-08-2014.
 */
public class ConsoleCommand implements ICommandHandler {
    private final Plugin plugin;
    private final BukkitScheduler scheduler;

    public ConsoleCommand(Plugin plugin, BukkitScheduler scheduler) {

        this.plugin = plugin;
        this.scheduler = scheduler;
    }

    @Override
    public void handleCommand(String command, Query query, CountDownLatch latch, Request request, Response response) throws IOException {
        try {
            final String cmd = query.get("cmd");
            plugin.logger.logInfo("received command: " + cmd);
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), cmd);
        }
        catch (Throwable t){
            plugin.logger.logSevere(t);
        }
        finally {
            latch.countDown();
        }
    }
}
