package com.github.atomishere.atomutils.services;

/**
 * Simple service interface
 */
public interface Service {
    /**
     * Called when the service starts.
     */
    void start();

    /**
     * Called when the service stops.
     */
    void stop();

    /**
     * Weather the current service is enabled or not.
     *
     * @return status of service.
     */
    boolean isEnabled();

    /**
     * Name of the service.
     *
     * @return service name.
     */
    String getName();
}
