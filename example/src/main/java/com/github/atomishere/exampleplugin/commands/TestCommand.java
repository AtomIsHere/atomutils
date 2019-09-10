package com.github.atomishere.exampleplugin.commands;

import com.github.atomishere.atomutils.commands.AtomCommand;
import com.github.atomishere.atomutils.commands.CommandParameters;
import com.github.atomishere.atomutils.commands.CommandPermissions;
import com.github.atomishere.atomutils.commands.CommandSource;
import com.github.atomishere.exampleplugin.ExamplePlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(commandName = "testCommand", description = "Test the command system", usage = "/<command>", aliases = "tc,testcmd,test")
@CommandPermissions(permission = "testcommand", source = CommandSource.BOTH)
public class TestCommand extends AtomCommand<ExamplePlugin> {
    /**
     * Creates an instance of AtomCommand
     *
     * @param name   name of the command.
     * @param plugin owner of the command.
     */
    public TestCommand(String name, ExamplePlugin plugin) {
        super(name, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Player playerSender, String[] args) {
        sender.sendMessage(ChatColor.GREEN + "It worked!");
        return false; //Test usage message
    }
}
