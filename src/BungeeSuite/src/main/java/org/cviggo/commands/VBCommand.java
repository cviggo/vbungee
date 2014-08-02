package org.cviggo.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import org.cviggo.managers.ConsoleCommandManager;
import org.cviggo.vbungee.shared.Utils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by viggo on 02-08-2014.
 */
public class VBCommand extends Command {
    public VBCommand() {
        super("cmd", "vb.admin");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings == null || strings.length < 2) {
            commandSender.sendMessage("usage: cmd <bungee|server|all> <command to run>");
            return;
        }

        final ArrayList<String> stringList = new ArrayList<>(Arrays.asList(strings));


        final String scope = stringList.remove(0);
        final boolean success = ConsoleCommandManager.ExecuteCommand(scope, Utils.join(stringList, " "));
        if (!success) {
            commandSender.sendMessage("Command failed. Maybe it is not registered at the receiving end.");
        }
    }
}
