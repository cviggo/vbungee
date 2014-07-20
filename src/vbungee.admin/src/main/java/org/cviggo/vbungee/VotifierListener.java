package org.cviggo.vbungee;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class VotifierListener implements Listener {

    private final Plugin plugin;

    public VotifierListener(Plugin plugin){

        this.plugin = plugin;
    }

    @EventHandler(priority= EventPriority.NORMAL)
    public void onVotifierEvent(VotifierEvent event) {

        Vote vote = event.getVote();

        final String username = vote.getUsername();

        plugin.getServer().dispatchCommand(
                plugin.getServer().getConsoleSender(),
                "sync console all adjustbonusclaimblocks " + username + " 100"
        );

    }
}
