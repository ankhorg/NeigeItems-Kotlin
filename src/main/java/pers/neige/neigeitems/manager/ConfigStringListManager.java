package pers.neige.neigeitems.manager;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiFunction;
import java.util.logging.Logger;

public class ConfigStringListManager<T> extends AbstractConfigManager<String, T, List<String>> {
    public ConfigStringListManager(
            @NotNull JavaPlugin plugin,
            @NotNull String elementName,
            @NotNull String directory,
            @NotNull BiFunction<String, List<String>, T> converter
    ) {
        super(plugin, elementName, directory, ConfigurationSection::getStringList, String::toString, converter);
    }

    public ConfigStringListManager(
            @NotNull String pluginName,
            @NotNull Logger logger,
            @NotNull String elementName,
            @NotNull String directory,
            @NotNull BiFunction<String, List<String>, T> converter
    ) {
        super(pluginName, logger, elementName, directory, ConfigurationSection::getStringList, String::toString, converter);
    }

    public ConfigStringListManager(
            @NotNull String pluginName,
            @NotNull org.slf4j.Logger logger,
            @NotNull String elementName,
            @NotNull String directory,
            @NotNull BiFunction<String, List<String>, T> converter
    ) {
        super(pluginName, logger, elementName, directory, ConfigurationSection::getStringList, String::toString, converter);
    }

    public ConfigStringListManager(
            @NotNull String pluginName,
            @NotNull String elementName,
            @NotNull String directory,
            @NotNull BiFunction<String, List<String>, T> converter
    ) {
        super(pluginName, LoggerFactory.getLogger(pluginName), elementName, directory, ConfigurationSection::getStringList, String::toString, converter);
    }
}
