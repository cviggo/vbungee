package org.cviggo.listeners;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PlayerHandshakeEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import org.cviggo.bungeesuiteextensions.ServerManager;
import org.cviggo.configs.MainConfig;
import org.cviggo.main.BungeeSuite;
import org.cviggo.managers.PlayerManager;
import org.cviggo.objects.BSPlayer;
import org.cviggo.objects.Messages;
import org.cviggo.vbungee.shared.client.Client;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void playerLogin( PostLoginEvent e ) throws SQLException {
        if ( !PlayerManager.onlinePlayers.containsKey( e.getPlayer().getName() ) ) {
            PlayerManager.loadPlayer( e.getPlayer() );
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void playerLogin( ServerConnectedEvent e ) throws SQLException {
        BSPlayer p = PlayerManager.getPlayer( e.getPlayer() );
        if ( p.firstConnect() ) {
            if ( MainConfig.broadcastProxyConnectionMessages ) {
                PlayerManager.sendBroadcast( Messages.PLAYER_CONNECT_PROXY.replace( "{player}", p.getDisplayingName() ) );
            }
            if ( MainConfig.motd ) {
                PlayerManager.sendMessageToPlayer( e.getPlayer().getName(), Messages.MOTD.replace( "{player}", p.getDisplayingName() ) );
            }
            p.connected();
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void playerLogout( final PlayerDisconnectEvent e ) {

        final String playerName = e.getPlayer().getName();
        final org.cviggo.vbungee.shared.server.ServerInfo serverInfo =
                ServerManager.getServer(e.getPlayer().getServer().getInfo().getName());

        final HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("playerName", playerName);

        BungeeSuite.instance.getProxy().getScheduler().runAsync(BungeeSuite.instance, new Runnable() {
            @Override
            public void run() {
                Client.request(serverInfo, "SyncPlayerData", requestMap);
            }
        });

        //BungeeSuite.instance.getLogger().info("saving: " + playerName + ", server: " + e.getPlayer().getServer().getInfo().getName() + ", " + serverInfo.host + serverInfo.port);

        int dcTime = MainConfig.playerDisconnectDelay;
        final BSPlayer p = PlayerManager.getPlayer( e.getPlayer() );
        if ( dcTime > 0 ) {
            BungeeSuite.proxy.getScheduler().schedule( BungeeSuite.instance, new Runnable() {

                @Override
                public void run() {
                    if ( PlayerManager.isPlayerOnline( p.getName() ) && ProxyServer.getInstance().getPlayer( e.getPlayer().getName() ) == null ) {
                        if ( !PlayerManager.kickedPlayers.contains( e.getPlayer() ) ) {
                            if ( MainConfig.broadcastProxyConnectionMessages ) {
                                PlayerManager.sendBroadcast( Messages.PLAYER_DISCONNECT_PROXY.replace( "{player}", p.getDisplayingName() ) );
                            }
                        } else {
                            PlayerManager.kickedPlayers.remove( e.getPlayer() );
                        }
                        PlayerManager.unloadPlayer( e.getPlayer().getName() );
                    }
                }

            }, MainConfig.playerDisconnectDelay, TimeUnit.SECONDS );
        } else {
            if ( PlayerManager.isPlayerOnline( p.getName() ) && ProxyServer.getInstance().getPlayer( e.getPlayer().getName() ) == null ) {
                if ( !PlayerManager.kickedPlayers.contains( e.getPlayer() ) ) {
                    if ( MainConfig.broadcastProxyConnectionMessages ) {
                        PlayerManager.sendBroadcast( Messages.PLAYER_DISCONNECT_PROXY.replace( "{player}", p.getDisplayingName() ) );
                    }
                } else {
                    PlayerManager.kickedPlayers.remove( e.getPlayer() );
                }
                PlayerManager.unloadPlayer( e.getPlayer().getName() );
            }
        }
    }

    @EventHandler
    public void onPlayerHandshake(final PlayerHandshakeEvent event) {

    }
}
