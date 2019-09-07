package com.github.atomishere.atomutils.utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

/**
 * A utils class for helping with saving configs.
 */
public class ConfigUtils {
    /**
     * Loads a config out of a plugins data folder.
     *
     * @see #saveConfig(FileConfiguration, String, Plugin)
     * @param configName name of the config file
     * @param plugin plugin instance.
     * @return loaded configuration.
     */
    public static FileConfiguration getConifg(String configName, Plugin plugin) {
        File customConfigFile = new File(plugin.getDataFolder(), configName);
        if(!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            plugin.saveResource(configName, false);
        }

        YamlConfiguration customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
            return null;
        }

        return customConfig;
    }

    /**
     * Saves a config to a plugins data folder.
     *
     * @see #getConifg(String, Plugin)
     * @param config config to save.
     * @param file name of file to save it to.
     * @param plugin plugin instance.
     * @throws IOException if it could not write to file.
     */
    public static void saveConfig(FileConfiguration config, String file, Plugin plugin) throws IOException {
        File configFile = new File(plugin.getDataFolder(), file);
        if(!configFile.exists()) {
            if(!configFile.createNewFile()) return;
        }

        config.save(configFile);
    }
}
