package org.cviggo.managers;

import java.util.logging.Logger;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import org.cviggo.configs.ChatConfig;

public class LoggingManager {
	static ProxyServer proxy = ProxyServer.getInstance();
	static Logger log = proxy.getLogger();

	public static void log(String message) {
		if(ChatConfig.stripChat){
			message = ChatColor.stripColor(message);
			message=message.replaceAll("&[0-9 a-f k-o r]", "");
		}
		log.info(message);
	}

}
