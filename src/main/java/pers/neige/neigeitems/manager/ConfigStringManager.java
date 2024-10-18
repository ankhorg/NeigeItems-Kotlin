package pers.neige.neigeitems.manager;

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

public class ConfigStringManager<T> extends ConcurrentHashMap<String, T> {
    @NotNull
    private final String pluginName;
    private final Logger logger;
    @NotNull
    private final BiFunction<String, String, T> converter;
    @NotNull
    private final String elementName;
    @NotNull
    private final String directory;

    public ConfigStringManager(
            @NotNull JavaPlugin plugin,
            @NotNull String elementName,
            @NotNull String directory,
            @NotNull BiFunction<String, String, T> converter
    ) {
        this.pluginName = plugin.getName();
        this.logger = plugin.getLogger();
        this.elementName = elementName;
        this.directory = directory;
        this.converter = converter;
    }

    public ConfigStringManager(
            @NotNull String pluginName,
            @NotNull Logger logger,
            @NotNull String elementName,
            @NotNull String directory,
            @NotNull BiFunction<String, String, T> converter
    ) {
        this.pluginName = pluginName;
        this.logger = logger;
        this.elementName = elementName;
        this.directory = directory;
        this.converter = converter;
    }

    @NotNull
    protected Map<String, String> getConfigs() {
        Map<String, String> result = new HashMap<>();
        for (File file : ConfigUtils.getAllFiles(pluginName, directory)) {
            if (!file.getName().endsWith(".yml")) continue;
            result.putAll(ConfigUtils.getConfigStringMap(file));
        }
        return result;
    }

    public void reload() {
        clear();
        load();
    }

    protected void load() {
        for (Entry<String, String> entry : getConfigs().entrySet()) {
            String id = entry.getKey();
            String text = entry.getValue();
            try {
                put(id, converter.apply(id, text));
            } catch (Throwable throwable) {
                logger.log(Level.WARNING, throwable, () -> "error occurred while loading " + elementName + ": " + id);
            }
        }
    }
}
