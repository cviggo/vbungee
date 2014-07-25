package org.cviggo.configs;

import org.cviggo.configlibrary.Config;

import java.io.File;

public class SpawnConfig {
	private static String configpath = File.separator+"plugins"+File.separator+"BungeeSuite"+File.separator+"spawns.yml";
	public static Config spawnConfig = new Config(configpath);
	public static boolean newspawn = spawnConfig.getBoolean("Spawn new players at newspawn",
			false);
	public static boolean forcespawn = spawnConfig.getBoolean("Spawn all players reconnecting players at proxy spawn",
			false);
	
}
