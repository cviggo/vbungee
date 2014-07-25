package org.cviggo.configs;

import org.cviggo.configlibrary.Config;

import java.io.File;

public class TeleportConfig {
	private static String configpath = File.separator+"plugins"+File.separator+"BungeeSuite"+File.separator+"teleport.yml";
	public static Config teleportConfig = new Config(configpath);
	public static int expireTime = teleportConfig.getInt("TeleportRequestExpireTime",
			10);
}
