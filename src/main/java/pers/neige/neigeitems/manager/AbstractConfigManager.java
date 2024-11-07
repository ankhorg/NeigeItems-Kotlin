package pers.neige.neigeitems.manager;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.utils.ConfigUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractConfigManager<T, R> extends ConcurrentHashMap<String, T> {
    @NotNull
    protected final String pluginName;
    @NotNull
    protected final Logger logger;
    @NotNull
    protected final BiFunction<ConfigurationSection, String, R> configGetter;
    @NotNull
    protected final BiFunction<String, R, T> converter;
    @NotNull
    protected final String elementName;
    @NotNull
    protected final String directory;
    protected boolean notNullConfig = true;

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
    protected List<File> getFiles() {
        return ConfigUtils.getAllFiles(pluginName, directory);
    }

    @NotNull
    protected Map<String, R> getConfigs() {
        final Map<String, R> result = new HashMap<>();
        getFiles().forEach((file) -> loadFile(result, file));
        return result;
    }

    protected void loadFile(@NotNull Map<String, R> result, @NotNull File file) {
        if (!file.getName().endsWith(".yml")) return;
        try {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            loadConfig(result, config);
        } catch (Throwable throwable) {
            logger.log(Level.WARNING, throwable, () -> "error occurred while loading file: " + file.getName());
        }
    }

    protected void loadConfig(@NotNull Map<String, R> result, @NotNull YamlConfiguration config) {
        String currentKey = "";
        try {
            for (String key : config.getKeys(false)) {
                currentKey = key;
                R value = configGetter.apply(config, key);
                if (value == null && notNullConfig) return;
                result.put(key, value);
            }
        } catch (Throwable throwable) {
            final String finalCurrentKey = currentKey;
            logger.log(Level.WARNING, throwable, () -> "error occurred while loading config, current key: " + finalCurrentKey + ", config content: \n" + config.saveToString());
        }
    }

    public void reload() {
        clear();
        load();
    }

    protected void load() {
        getConfigs().forEach((id, content) -> {
            try {
                T result = converter.apply(id, content);
                if (result == null) return;
                put(id, result);
            } catch (Throwable throwable) {
                logger.log(Level.WARNING, throwable, () -> "error occurred while loading " + elementName + ": " + id);
            }
        });
    }
}
