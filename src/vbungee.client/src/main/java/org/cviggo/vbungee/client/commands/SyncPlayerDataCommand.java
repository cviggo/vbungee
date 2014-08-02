package org.cviggo.vbungee.client.commands;

import org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.cviggo.vbungee.client.Plugin;
import org.cviggo.vbungee.shared.client.Client;
import org.cviggo.vbungee.shared.server.ICommandHandler;
import org.simpleframework.http.Query;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

/**
 * Created by viggo on 25-07-2014.
 */
public class SyncPlayerDataCommand implements ICommandHandler {

    private final Plugin plugin;
    private final BukkitScheduler scheduler;

    public SyncPlayerDataCommand(Plugin plugin, BukkitScheduler scheduler) {
        this.plugin = plugin;
        this.scheduler = scheduler;
    }

    @Override
    public void handleCommand(String command, final Query query, final CountDownLatch latch, final Request request, final Response response) throws IOException {

        //plugin.logger.logInfo("Processing command: " + command);

        final String playerName = query.get("playerName");
        final PrintStream body = response.getPrintStream();

        if (playerName != null) {

            /*
             * run in sync to access Bukkit API
             */
            scheduler.runTask(plugin, new Runnable() {
                @Override
                public void run() {
                    final Player player = plugin.getServer().getPlayer(playerName);
                    if (player != null) {

                        // save data on this local server
                        player.saveData();

                    }

                    final String playerDataFolder = plugin.getServer().getWorldContainer().getAbsolutePath().replace("./", "") + "/players/";


                        /*
                        * run player data sync in async to relieve tick processing of IO hiccups etc.
                        */
                    scheduler.runTaskAsynchronously(plugin, new Runnable() {
                        @Override
                        public void run() {
                            if (savePlayerAsync(playerDataFolder, playerName)) {
                                body.println("OK");
                            } else {
                                body.println("ERROR");
                                response.setStatus(Status.getStatus(501));
                            }

                            if (query.containsKey("callback")) {

                                final HashMap<String, String> requestMap = new HashMap<>();

                                Client.parseCallbackParameters(query, requestMap);

                                if (!Client.request(plugin.getBungeeServerInfo(), query.get("callback"), requestMap, null, null)) {
                                    plugin.logger.logWarn("failed to perform callback request: " + query.get("callback"));
                                }
                            }

                            // release latch
                            latch.countDown();
                        }
                    });

                }
            });

        } else {
            body.println("playerName missing");
            response.setStatus(Status.getStatus(501));
            latch.countDown();
        }
    }

    private boolean savePlayerAsync(String playerDataFolder, String playerName) {

        // TODO: omg fix this already!
        // HACK: there are hacks ... and then there is what is below ... dont even know what to call that! ...


        plugin.logger.logInfo("saving player: " + playerName);

        final String playerFileName = playerName + ".dat";

        final File file = new File(playerDataFolder + playerFileName);

        if (file != null && file.exists()) {
            plugin.logger.logInfo("player data found. Total size: " + file.length() + " bytes");

            final String[] serverFolderNames = {"tppi-gliese", "tppi-helios", "tppi-sectorz", "tppi-sigmus", "tppi-resource", "tppi-hub"};

            try {

                for (String serverFolderName : serverFolderNames) {
                    final File destinationFile = new File("/home/mc/" + serverFolderName + "/world/players/" + playerFileName);

                    try {

                        if (file.compareTo(destinationFile) != 0) {
                            plugin.logger.logInfo("saving player file data: " + destinationFile.getAbsolutePath());
                            FileUtils.copyFile(file, destinationFile);
                        }
                    } catch (Throwable t) {
                        plugin.logger.logWarn("Failed to save player file: " + file.getAbsolutePath() + " to " + destinationFile.getAbsolutePath());
                    }
                }

                plugin.logger.logInfo("saved file to all servers");

                return true;

            } catch (Throwable t) {
                plugin.logger.logSevere("Player data sync failed for player " + playerName + " with error: " + t.getMessage());
                return false;
            }

        }

        plugin.logger.logSevere("Source player file did not exist. Cannot continue player data sync.");
        return false;
    }

}
