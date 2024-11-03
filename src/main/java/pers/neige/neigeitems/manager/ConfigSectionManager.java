package pers.neige.neigeitems.manager;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.logging.Logger;

public class ConfigSectionManager<T> extends AbstractConfigManager<T, ConfigurationSection> {
    public ConfigSectionManager(
            @NotNull JavaPlugin plugin,
            @NotNull String elementName,
            @NotNull String directory,
            @NotNull BiFunction<String, ConfigurationSection, T> converter
    ) {
        super(plugin, elementName, directory, ConfigurationSection::getConfigurationSection, converter);
    }

    public ConfigSectionManager(
            @NotNull String pluginName,
            @NotNull Logger logger,
            @NotNull String elementName,
            @NotNull String directory,
            @NotNull BiFunction<String, ConfigurationSection, T> converter
    ) {
        super(pluginName, logger, elementName, directory, ConfigurationSection::getConfigurationSection, converter);
    }
}
