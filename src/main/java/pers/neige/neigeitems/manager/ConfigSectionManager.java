package pers.neige.neigeitems.manager;

import lombok.NonNull;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.LoggerFactory;

import java.util.function.BiFunction;
import java.util.logging.Logger;

public class ConfigSectionManager<T> extends AbstractConfigManager<String, T, ConfigurationSection> {
    public ConfigSectionManager(
        @NonNull JavaPlugin plugin,
        @NonNull String elementName,
        @NonNull String directory,
        @NonNull BiFunction<String, ConfigurationSection, T> converter
    ) {
        super(plugin, elementName, directory, ConfigurationSection::getConfigurationSection, String::toString, converter);
    }

    public ConfigSectionManager(
        @NonNull String pluginName,
        @NonNull Logger logger,
        @NonNull String elementName,
        @NonNull String directory,
        @NonNull BiFunction<String, ConfigurationSection, T> converter
    ) {
        super(pluginName, logger, elementName, directory, ConfigurationSection::getConfigurationSection, String::toString, converter);
    }

    public ConfigSectionManager(
        @NonNull String pluginName,
        @NonNull org.slf4j.Logger logger,
        @NonNull String elementName,
        @NonNull String directory,
        @NonNull BiFunction<String, ConfigurationSection, T> converter
    ) {
        super(pluginName, logger, elementName, directory, ConfigurationSection::getConfigurationSection, String::toString, converter);
    }

    public ConfigSectionManager(
        @NonNull String pluginName,
        @NonNull String elementName,
        @NonNull String directory,
        @NonNull BiFunction<String, ConfigurationSection, T> converter
    ) {
        super(pluginName, LoggerFactory.getLogger(pluginName), elementName, directory, ConfigurationSection::getConfigurationSection, String::toString, converter);
    }
}
