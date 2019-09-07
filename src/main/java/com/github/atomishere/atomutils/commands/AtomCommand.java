package com.github.atomishere.atomutils.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Commands that don't need to be registered within plugins.yml. Permissions are handled for you.
 *
 * @param <T> owner of the command.
 */
public abstract class AtomCommand<T extends JavaPlugin> extends BukkitCommand {
    public static final String NO_PERMISSION_MESSAGE = ChatColor.RED + "You do not have permission";

    protected final T plugin;

    protected String permission;
    protected CommandSource source;

    /**
     * Creates an instance of AtomCommand
     *
     * @param name name of the command.
     * @param plugin owner of the command.
     */
    public AtomCommand(String name, T plugin) {
        super(name);
        this.plugin = plugin;
    }

    /**
     * Sets up the command. This is handled for you in CommandManager/
     *
     * @param params CommandParameters annotation
     * @param perms CommandPermissions annotation
     */
    public void setup(CommandParameters params, CommandPermissions perms) {
        this.description = params.description();
        this.usageMessage = params.usage().replaceFirst("<command>", getName());

        String[] primAl = params.aliases().split(",");

        List<String> aliases = Arrays.stream(primAl).collect(Collectors.toList());
        setAliases(aliases);

        this.permission = plugin.getName().toLowerCase() + "." + perms.permission();
        this.source = perms.source();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if(!hasPermission(sender)) {
            sender.sendMessage(NO_PERMISSION_MESSAGE);
            return true;
        }

        Player player = null;
        if(sender instanceof Player) {
            player = (Player) sender;
        }

        if(!onCommand(sender, player, args)) {
            sender.sendMessage("Usage: " + getUsage());
        }
        return true;
    }

    /**
     * Called when the command is run.
     *
     * @param sender The sender of the command
     * @param playerSender player instance of sender. Null if a player did not send the command/
     * @param args command arguments
     * @return true if usage is correct. False if usage is incorrect (Usage message is handled).
     */
    public abstract boolean onCommand(CommandSender sender, Player playerSender, String[] args);

    private boolean hasPermission(CommandSender sender) {
        switch(source) {
            case PLAYER:
                return (sender instanceof Player) && sender.hasPermission(permission);
            case CONSOLE:
                return !(sender instanceof Player) && sender.hasPermission(permission);
            case BOTH:
                return sender.hasPermission(permission);
        }

        return false;
    }
}
