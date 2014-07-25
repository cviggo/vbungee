package org.cviggo.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.cviggo.configs.Announcements;
import org.cviggo.configs.MainConfig;
import org.cviggo.objects.Messages;

public class ReloadCommand extends Command {

    public ReloadCommand() {
        super( "bsreload" );
    }

    @SuppressWarnings("deprecation")
	@Override
    public void execute( CommandSender sender, String[] args ) {
        if ( !( sender.hasPermission( "bungeesuite.reload" ) || sender.hasPermission( "bungeesuite.admin" ) ) ) {
            if ( sender instanceof ProxiedPlayer ) {
                ProxiedPlayer p = ( ProxiedPlayer ) sender;
                p.chat( "/bsreload" );
            }
        } else {
            Messages.reloadMessages();
            MainConfig.reloadConfig();
            Announcements.reloadAnnouncements();
            sender.sendMessage( "config.yml, announcements.yml and messages.yml reloaded!" );
        }

    }

}
