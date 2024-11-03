package pers.neige.neigeitems.manager;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
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

public abstract class AbstractConfigManager<T, R> extends ConcurrentHashMap<String, T> {
    @NotNull
    private final String pluginName;
    private final Logger logger;
    @NotNull
    private final BiFunction<ConfigurationSection, String, R> configGetter;
    @NotNull
    private final BiFunction<String, R, T> converter;
    @NotNull
    private final String elementName;
    @NotNull
    private final String directory;

    public AbstractConfigManager(
            @NotNull JavaPlugin plugin,
            @NotNull String elementName,
            @NotNull String directory,
            @NotNull BiFunction<ConfigurationSection, String, R> configGetter,
            @NotNull BiFunction<String, R, T> converter
    ) {
        this.pluginName = plugin.getName();
        this.logger = plugin.getLogger();
        this.elementName = elementName;
        this.directory = directory;
        this.configGetter = configGetter;
        this.converter = converter;
    }

    public AbstractConfigManager(
            @NotNull String pluginName,
            @NotNull Logger logger,
            @NotNull String elementName,
            @NotNull String directory,
            @NotNull BiFunction<ConfigurationSection, String, R> configGetter,
            @NotNull BiFunction<String, R, T> converter
    ) {
        this.pluginName = pluginName;
        this.logger = logger;
        this.elementName = elementName;
        this.directory = directory;
        this.configGetter = configGetter;
        this.converter = converter;
    }

    @NotNull
    protected Map<String, R> getConfigs() {
        Map<String, R> result = new HashMap<>();
        for (File file : ConfigUtils.getAllFiles(pluginName, directory)) {
            if (!file.getName().endsWith(".yml")) continue;
            try {
                YamlConfiguration config = new YamlConfiguration();
                config.load(file);
                for (String key : config.getKeys(false)) {
                    R value = configGetter.apply(config, key);
                    if (value == null) continue;
                    result.put(key, value);
                }
            } catch (Throwable throwable) {
                logger.log(Level.WARNING, throwable, () -> "error occurred while loading file: " + file.getName());
            }
        }
        return result;
    }

    public void reload() {
        clear();
        load();
    }

    protected void load() {
        for (Entry<String, R> entry : getConfigs().entrySet()) {
            String id = entry.getKey();
            R list = entry.getValue();
            try {
                put(id, converter.apply(id, list));
            } catch (Throwable throwable) {
                logger.log(Level.WARNING, throwable, () -> "error occurred while loading " + elementName + ": " + id);
            }
        }
    }
}
