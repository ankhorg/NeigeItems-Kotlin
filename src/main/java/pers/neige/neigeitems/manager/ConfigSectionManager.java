package pers.neige.neigeitems.manager;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.utils.ConfigUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigSectionManager<T> extends ConcurrentHashMap<String, T> {
    @NotNull
    private final String pluginName;
    private final Logger logger;
    @NotNull
    private final BiFunction<String, ConfigurationSection, T> converter;
    @NotNull
    private final String elementName;
    @NotNull
    private final String directory;

    public ConfigSectionManager(
            @NotNull JavaPlugin plugin,
            @NotNull String elementName,
            @NotNull String directory,
            @NotNull BiFunction<String, ConfigurationSection, T> converter
    ) {
        this.pluginName = plugin.getName();
        this.logger = plugin.getLogger();
        this.elementName = elementName;
        this.directory = directory;
        this.converter = converter;
    }

    public ConfigSectionManager(
            @NotNull String pluginName,
            @NotNull Logger logger,
            @NotNull String elementName,
            @NotNull String directory,
            @NotNull BiFunction<String, ConfigurationSection, T> converter
    ) {
        this.pluginName = pluginName;
        this.logger = logger;
        this.elementName = elementName;
        this.directory = directory;
        this.converter = converter;
    }

    @NotNull
    protected Map<String, ConfigurationSection> getConfigs() {
        Map<String, ConfigurationSection> result = new HashMap<>();
        for (File file : ConfigUtils.getAllFiles(pluginName, directory)) {
            if (!file.getName().endsWith(".yml")) continue;
            result.putAll(ConfigUtils.getConfigSectionMap(file));
        }
        return result;
    }

    public void reload() {
        clear();
        load();
    }

    protected void load() {
        for (Entry<String, ConfigurationSection> entry : getConfigs().entrySet()) {
            String id = entry.getKey();
            ConfigurationSection config = entry.getValue();
            try {
                put(id, converter.apply(id, config));
            } catch (Throwable throwable) {
                logger.log(Level.WARNING, throwable, () -> "error occurred while loading " + elementName + ": " + id);
            }
        }
    }
}