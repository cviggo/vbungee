package org.cviggo.configs;

import org.cviggo.configlibrary.Config;
import org.cviggo.managers.AnnouncementManager;

import java.io.File;

public class Announcements {
    private static String configpath = File.separator + "plugins" + File.separator + "BungeeSuite" + File.separator + "announcements.yml";
    public static Config announcements = new Config( configpath );
    public static boolean announcer = announcements.getBoolean( "Announcements.enabled", true );

    public static void reloadAnnouncements() {
        announcements = null;
        announcements = new Config( configpath );
        announcer = announcements.getBoolean( "Announcements.enabled", true );
        AnnouncementManager.reloadAnnouncements();
    }
}


