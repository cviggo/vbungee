package org.cviggo.listeners;

import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.cviggo.managers.LoggingManager;
import org.cviggo.managers.PlayerManager;
import org.cviggo.managers.WarpsManager;
import org.cviggo.objects.Location;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.sql.SQLException;

public class WarpsMessageListener implements Listener {

    @EventHandler
    public void receivePluginMessage( PluginMessageEvent event ) throws IOException, SQLException {
        if ( event.isCancelled() ) {
            return;
        }
        if ( !event.getTag().equalsIgnoreCase( "BSWarps" ) ) {
            return;
        }
        if ( !( event.getSender() instanceof Server ) )
            return;
        event.setCancelled( true );

        DataInputStream in = new DataInputStream( new ByteArrayInputStream( event.getData() ) );

        String task = in.readUTF();

        if ( task.equals( "WarpPlayer" ) ) {
            WarpsManager.sendPlayerToWarp(in.readUTF(), in.readUTF(), in.readUTF(), in.readBoolean(), in.readBoolean());
            return;
        }

        if ( task.equals( "GetWarpsList" ) ) {
            WarpsManager.getWarpsList( in.readUTF(), in.readBoolean(), in.readBoolean(), in.readBoolean(), in.readBoolean() );
            return;
        }

        if ( task.equals( "SetWarp" ) ) {
            WarpsManager.setWarp( PlayerManager.getPlayer(in.readUTF()), in.readUTF(), new Location( ( ( Server ) event.getSender() ).getInfo().getName(), in.readUTF(), in.readDouble(), in.readDouble(), in.readDouble(), in.readFloat(), in.readFloat() ), in.readBoolean(), in.readBoolean() );
            return;
        }

        if ( task.equals( "DeleteWarp" ) ) {
            WarpsManager.deleteWarp( PlayerManager.getPlayer( in.readUTF() ), in.readUTF() );
            return;
        }

        if ( task.equals( "SendVersion" ) ) {
            LoggingManager.log(in.readUTF());
        }

    }

}
