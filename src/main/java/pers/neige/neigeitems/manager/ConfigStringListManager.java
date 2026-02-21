package pers.neige.neigeitems.manager;

import lombok.NonNull;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiFunction;
import java.util.logging.Logger;

public class ConfigStringListManager<T> extends AbstractConfigManager<String, T, List<String>> {
    public ConfigStringListManager(
        @NonNull JavaPlugin plugin,
        @NonNull String elementName,
        @NonNull String directory,
        @NonNull BiFunction<String, List<String>, T> converter
    ) {
        super(plugin, elementName, directory, ConfigurationSection::getStringList, String::toString, converter);
    }

    public ConfigStringListManager(
        @NonNull String pluginName,
        @NonNull Logger logger,
        @NonNull String elementName,
        @NonNull String directory,
        @NonNull BiFunction<String, List<String>, T> converter
    ) {
        super(pluginName, logger, elementName, directory, ConfigurationSection::getStringList, String::toString, converter);
    }

    public ConfigStringListManager(
        @NonNull String pluginName,
        @NonNull org.slf4j.Logger logger,
        @NonNull String elementName,
        @NonNull String directory,
        @NonNull BiFunction<String, List<String>, T> converter
    ) {
        super(pluginName, logger, elementName, directory, ConfigurationSection::getStringList, String::toString, converter);
    }

    public ConfigStringListManager(
        @NonNull String pluginName,
        @NonNull String elementName,
        @NonNull String directory,
        @NonNull BiFunction<String, List<String>, T> converter
    ) {
        super(pluginName, LoggerFactory.getLogger(pluginName), elementName, directory, ConfigurationSection::getStringList, String::toString, converter);
    }
}
