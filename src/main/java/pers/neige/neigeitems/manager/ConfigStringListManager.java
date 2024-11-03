package pers.neige.neigeitems.manager;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BiFunction;
import java.util.logging.Logger;

public class ConfigStringListManager<T> extends AbstractConfigManager<T, List<String>> {
    public ConfigStringListManager(
            @NotNull JavaPlugin plugin,
            @NotNull String elementName,
            @NotNull String directory,
            @NotNull BiFunction<String, List<String>, T> converter
    ) {
        super(plugin, elementName, directory, ConfigurationSection::getStringList, converter);
    }

    public ConfigStringListManager(
            @NotNull String pluginName,
            @NotNull Logger logger,
            @NotNull String elementName,
            @NotNull String directory,
            @NotNull BiFunction<String, List<String>, T> converter
    ) {
        super(pluginName, logger, elementName, directory, ConfigurationSection::getStringList, converter);
    }
}
