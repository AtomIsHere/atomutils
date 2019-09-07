package com.github.atomishere.atomutils.commands;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Dynamically load commands.
 *
 * @param <T> Owner of the command manager.
 */
@RequiredArgsConstructor
public class CommandManager<T extends JavaPlugin> {
    private final T plugin;

    /**
     * Load all commands in a package. (Experimental feature. I heard that the Reflections feature may not work depending on your os)
     *
     * @param pack Package to check.
     * @param prefix will only load commands with this prefix.
     */
    public void loadCommands(Package pack, String prefix) {
        List<ClassLoader> classLoadersList = new LinkedList<>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(pack.getName()))));

        Set<Class<? extends AtomCommand>> classes = reflections.getSubTypesOf(AtomCommand.class);
        for(Class commandClass : classes) {
            if(!commandClass.getSimpleName().startsWith(prefix)) continue;

            Class<? extends AtomCommand<T>> clazz;
            try {
                clazz = (Class<? extends AtomCommand<T>>) commandClass;
            } catch(ClassCastException ex) {
                plugin.getLogger().severe("Tried to register a command that does not belong to the plugin");
                continue;
            }

            loadCommand(clazz, prefix);
        }
    }

    /**
     * Load a command based on its class object.
     *
     * @param clazz command class.
     * @param prefix the prefix of the command.
     */
    public void loadCommand(Class<? extends AtomCommand<T>> clazz, String prefix) {
        Constructor<? extends AtomCommand<T>> con;
        try {
            con = clazz.getConstructor(String.class, plugin.getClass());
        } catch(NoSuchMethodException e) {
            plugin.getLogger().warning("Not loading command " + clazz.getSimpleName().replaceFirst(prefix, "") + ". Invalid Constructor!");
            return;
        }

        String commandName = clazz.getSimpleName().replaceFirst(prefix, "");

        AtomCommand<T> inst;
        try {
            inst = con.newInstance(commandName, plugin);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            return;
        }

        loadCommand(inst);
    }

    /**
     * Load a simple instance of a command.
     *
     * @param command Command instance.
     */
    public void loadCommand(AtomCommand<T> command) {
        CommandParameters params = command.getClass().getAnnotation(CommandParameters.class);
        if(params == null) {
            plugin.getLogger().info("Not loading command " + getClass().getSimpleName() + ". Command class does not have Parameters");
            return;
        }
        CommandPermissions perms = command.getClass().getAnnotation(CommandPermissions.class);
        if(perms == null) {
            plugin.getLogger().info("Not loading command " + getClass().getSimpleName() + ". Command class does not have Permissions");
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
