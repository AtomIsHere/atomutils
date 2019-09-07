package com.github.atomishere.atomutils.services;

import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * A class for loading, managing and registering services. Useful for splitting up a plugin into small sections.
 *
 * @author AtomIsHere
 * @param <T> owner of the service manager.
 */
@RequiredArgsConstructor
public class ServiceManager<T extends JavaPlugin> implements Service {
    private final T plugin;
    private final List<Service> services = new ArrayList<>();

    private boolean enabled = false;

    /**
     * Register a simple service instance.
     *
     * @param service service instance.
     * @throws RegistrationFailureException If the same service or a service with the same name is already registered.
     */
    public void registerService(Service service) throws RegistrationFailureException {
        for(Service compare : services) {
            if(compare.getName().equals(service.getName())) {
                throw new RegistrationFailureException("Tried to register a service that was already registered");
            }
        }

        services.add(service);
    }

    /**
     * Register a service using it's class object.
     *
     * @see #registerService(Service)
     * @param serviceClass the service class.
     * @param <S> service type.
     * @return the registered instance.
     */
    public <S extends AtomService<T>> S registerService(Class<S> serviceClass) {
        Constructor<S> con;
        try {
            con = serviceClass.getConstructor(plugin.getClass());
        } catch (NoSuchMethodException e) {
            plugin.getLogger().severe("Invalid constructor in service: " + serviceClass.getSimpleName());
            e.printStackTrace();
            return null;
        }

        S inst;
        try {
            inst = con.newInstance(plugin);
        } catch (IllegalAccessException e) {
            plugin.getLogger().severe("Can not load service " + serviceClass.getSimpleName() + ". Can not access constructor.");
            e.printStackTrace();
            return null;
        } catch (InstantiationException e) {
            plugin.getLogger().severe("Can not load service " + serviceClass.getSimpleName() + ". Service class is abstract.");
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            plugin.getLogger().severe("Can not load service " + serviceClass.getSimpleName() + ". Service's constructor threw an exception.");
            e.getCause().printStackTrace();
            return null;
        } catch (IllegalArgumentException e) {
            plugin.getLogger().severe("Can not load service " + serviceClass.getSimpleName() + ". Illegal arguments.");
            e.printStackTrace();
            return null;
        }

        try {
            registerService(inst);
        } catch (RegistrationFailureException e) {
            plugin.getLogger().severe("Can not load service " + serviceClass.getSimpleName() + ". Service already loaded");
            e.printStackTrace();
            return null;
        }

        return inst;
    }

    @Override
    public void start() {
        for(Service service : services) {
            plugin.getLogger().info("Starting Service: " + service.getName());
            service.start();
        }

        enabled = true;
    }

    @Override
    public void stop() {
        for(Service service : services) {
            plugin.getLogger().info("Stopping Service: " + service.getName());
            service.stop();
        }

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
}
