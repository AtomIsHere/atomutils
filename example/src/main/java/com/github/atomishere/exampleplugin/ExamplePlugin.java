package com.github.atomishere.exampleplugin;

import com.github.atomishere.atomutils.commands.CommandManager;
import com.github.atomishere.atomutils.services.ServiceManager;
import com.github.atomishere.exampleplugin.commands.TestCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class ExamplePlugin extends JavaPlugin {
    private ServiceManager<ExamplePlugin> serviceManager;
    private CommandManager<ExamplePlugin> commandManager;

    private TestService ts;

    @Override
    public void onEnable() {
        // Plugin startup logic
        serviceManager = new ServiceManager<>(this);
        commandManager = new CommandManager<>(this);

        //Register service
        ts = serviceManager.registerService(TestService.class);

        //Register Commands
        commandManager.loadCommand(TestCommand.class);

        //Start services
        serviceManager.start();

        ts.test();
    }

    @Override
    public void onDisable() {
        //Stop services
        serviceManager.stop();
    }
}
