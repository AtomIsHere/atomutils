package com.github.atomishere.atomutils.services;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A more abstract version of the Service interface. Automatically registers events on start and handles isEnabled() and getName().
 *
 * @param <T> owner of the service. Can only be registered with ServiceManager's of the same type.
 */
public abstract class AtomService<T extends JavaPlugin> implements Service, Listener {
    protected final T plugin;

    private boolean enabled = false;

    public AtomService(T plugin) {
        this.plugin = plugin;
    }

    @Override
    public void start() {
        onStart();
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
        enabled = true;
    }

    @Override
    public void stop() {
        onStop();
        enabled = false;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    /**
     * Ran when the service is started
     */
    protected abstract void onStart();

    /**
     * Ran when the service is stopped
     */
    protected abstract void onStop();
}
