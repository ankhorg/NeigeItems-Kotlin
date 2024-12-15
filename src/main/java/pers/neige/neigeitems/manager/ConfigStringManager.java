package pers.neige.neigeitems.manager;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.logging.Logger;

public class ConfigStringManager<T> extends AbstractConfigManager<String, T, String> {
    public ConfigStringManager(
            @NotNull JavaPlugin plugin,
            @NotNull String elementName,
            @NotNull String directory,
            @NotNull BiFunction<String, String, T> converter
    ) {
        super(plugin, elementName, directory, ConfigurationSection::getString, String::toString, converter);
    }

    public ConfigStringManager(
            @NotNull String pluginName,
            @NotNull Logger logger,
            @NotNull String elementName,
            @NotNull String directory,
            @NotNull BiFunction<String, String, T> converter
    ) {
        super(pluginName, logger, elementName, directory, ConfigurationSection::getString, String::toString, converter);
    }
}
