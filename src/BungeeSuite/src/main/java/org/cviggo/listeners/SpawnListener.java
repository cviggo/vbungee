package org.cviggo.listeners;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import org.cviggo.configs.SpawnConfig;
import org.cviggo.main.BungeeSuite;
import org.cviggo.managers.PlayerManager;
import org.cviggo.managers.SpawnManager;

public class SpawnListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void sendPlayerToHub(final PostLoginEvent e) throws SQLException {
		if(SpawnConfig.forcespawn && !SpawnManager.newPlayers.contains(e.getPlayer())){
			ProxyServer.getInstance().getScheduler().schedule(BungeeSuite.instance, new Runnable(){

				@Override
				public void run() {
					SpawnManager.sendPlayerToProxySpawn(PlayerManager.getPlayer(e.getPlayer()), false);
				}
				
			}, 300, TimeUnit.MILLISECONDS);

		}
	}
	
}
