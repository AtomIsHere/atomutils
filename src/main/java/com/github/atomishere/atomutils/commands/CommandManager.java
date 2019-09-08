package com.github.atomishere.atomutils.commands;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Dynamically load commands.
 *
 * @param <T> Owner of the command manager.
 */
@RequiredArgsConstructor
public class CommandManager<T extends JavaPlugin> {
    private final T plugin;

    /**
     * Load a command based on its class object. (Requires class to be annotated with CommandParameters and CommandPermissions)
     *
     * @param clazz command class.
     */
    public void loadCommand(Class<? extends AtomCommand<T>> clazz) {
        CommandParameters params = clazz.getAnnotation(CommandParameters.class);
        if(params == null) {
            plugin.getLogger().warning("Not loading command " + clazz.getSimpleName() + ". Class does not have CommandParameters");
            return;
        }

        Constructor<? extends AtomCommand<T>> con;
        try {
            con = clazz.getConstructor(String.class, plugin.getClass());
        } catch(NoSuchMethodException e) {
            plugin.getLogger().warning("Not loading command " + params.commandName() + ". Invalid Constructor!");
            return;
        }

        AtomCommand<T> inst;
        try {
            inst = con.newInstance(params.commandName(), plugin);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            return;
        }

        loadCommand(inst);
    }

    /**
     * Load a simple instance of a command. (Requires class to be annotated with CommandParameters and CommandPermissions)
     *
     * @param command Command instance.
     */
    public void loadCommand(AtomCommand<T> command) {
        CommandParameters params = command.getClass().getAnnotation(CommandParameters.class);
        if(params == null) {
            plugin.getLogger().info("Not loading command " + getClass().getSimpleName() + ". Class does not have CommandParameters");
            return;
        }
        CommandPermissions perms = command.getClass().getAnnotation(CommandPermissions.class);
        if(perms == null) {
            plugin.getLogger().info("Not loading command " + getClass().getSimpleName() + ". Class does not have CommandPermissions");
            return;
        }

        command.setup(params, perms);

        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            commandMap.register(command.getName(), command);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
