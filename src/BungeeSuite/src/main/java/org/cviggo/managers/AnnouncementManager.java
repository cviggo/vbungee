package org.cviggo.managers;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import org.cviggo.configs.Announcements;
import org.cviggo.main.BungeeSuite;
import org.cviggo.tasks.GlobalAnnouncements;
import org.cviggo.tasks.ServerAnnouncements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AnnouncementManager {
    public static ArrayList<ScheduledTask> announcementTasks = new ArrayList<>();
    static ProxyServer proxy = ProxyServer.getInstance();

    public static void loadAnnouncements() {
        //create defaults
        setDefaults();
        // load global announcements
        if ( Announcements.announcer ) {
            List<String> global = Announcements.announcements.getListString( "Announcements.Global.Messages", new ArrayList<String>() );
            if ( !global.isEmpty() ) {
                int interval = Announcements.announcements.getInt( "Announcements.Global.Interval", 0 );
                if ( interval > 0 ) {
                    GlobalAnnouncements g = new GlobalAnnouncements();
                    for ( String messages : global ) {
                        g.addAnnouncement( messages );
                    }
                    ScheduledTask t = proxy.getScheduler().schedule( BungeeSuite.instance, g, interval, interval, TimeUnit.SECONDS );
                    announcementTasks.add( t );
                }
            }
            //load server announcements
            for ( String server : proxy.getServers().keySet() ) {
                List<String> servermes = Announcements.announcements.getListString( "Announcements." + server + ".Messages", new ArrayList<String>() );
                if ( !servermes.isEmpty() ) {
                    int interval = Announcements.announcements.getInt( "Announcements." + server + ".Interval", 0 );
                    if ( interval > 0 ) {
                        ServerAnnouncements s = new ServerAnnouncements( proxy.getServerInfo( server ) );
                        for ( String messages : servermes ) {
                            s.addAnnouncement( messages );
                        }
                        ScheduledTask t = proxy.getScheduler().schedule( BungeeSuite.instance, s, interval, interval, TimeUnit.SECONDS );
                        announcementTasks.add( t );
                    }
                }
            }
        }
    }

    private static void setDefaults() {
        List<String> check = Announcements.announcements.getSubNodes( "Announcements" );
        if ( !check.contains( "Global" ) ) {
            Announcements.announcements.setInt( "Announcements.Global.Interval", 300 );
            List<String> l = new ArrayList<String>();
            l.add( "&4Welcome to the server!" );
            l.add( "&aDon't forget to check out our website" );
            Announcements.announcements.setListString( "Announcements.Global.Messages", l );
        }
        for ( String server : proxy.getServers().keySet() ) {
            if ( !check.contains( server ) ) {
                Announcements.announcements.setInt( "Announcements." + server + ".Interval", 150 );
                List<String> l = new ArrayList<String>();
                l.add( "&4Welcome to the " + server + " server!" );
                l.add( "&aDon't forget to check out our website" );
                Announcements.announcements.setListString( "Announcements." + server + ".Messages", l );
            }
        }
    }

    public static void reloadAnnouncements() {
        for ( ScheduledTask task : announcementTasks ) {
            task.cancel();
        }
        announcementTasks.clear();
        loadAnnouncements();
    }
}
