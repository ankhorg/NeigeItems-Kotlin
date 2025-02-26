package pers.neige.neigeitems.manager;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.NeigeItems;
import pers.neige.neigeitems.manager.logger.ILogger;
import pers.neige.neigeitems.manager.logger.JavaLogger;
import pers.neige.neigeitems.manager.logger.Slf4jLogger;
import pers.neige.neigeitems.utils.ConfigUtils;

import java.io.File;
import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.Logger;

public abstract class AbstractConfigManager<K, V, R> extends ConcurrentHashMap<K, V> {
    @NotNull
    protected final String pluginName;
    @NotNull
    protected final ILogger logger;
    @NotNull
    protected final BiFunction<ConfigurationSection, String, R> configGetter;
    @NotNull
    protected final Function<String, K> keyConverter;
    @NotNull
    protected final BiFunction<K, R, V> converter;
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
            @NotNull Function<String, K> keyConverter,
            @NotNull BiFunction<K, R, V> converter
    ) {
        this.pluginName = plugin.getName();
        this.logger = new JavaLogger(plugin.getLogger());
        this.elementName = elementName;
        this.directory = directory;
        this.configGetter = configGetter;
        this.keyConverter = keyConverter;
        this.converter = converter;
    }

    public AbstractConfigManager(
            @NotNull String pluginName,
            @NotNull Logger logger,
            @NotNull String elementName,
            @NotNull String directory,
            @NotNull BiFunction<ConfigurationSection, String, R> configGetter,
            @NotNull Function<String, K> keyConverter,
            @NotNull BiFunction<K, R, V> converter
    ) {
        this.pluginName = pluginName;
        this.logger = new JavaLogger(logger);
        this.elementName = elementName;
        this.directory = directory;
        this.configGetter = configGetter;
        this.keyConverter = keyConverter;
        this.converter = converter;
    }

    public AbstractConfigManager(
            @NotNull String pluginName,
            @NotNull org.slf4j.Logger logger,
            @NotNull String elementName,
            @NotNull String directory,
            @NotNull BiFunction<ConfigurationSection, String, R> configGetter,
            @NotNull Function<String, K> keyConverter,
            @NotNull BiFunction<K, R, V> converter
    ) {
        this.pluginName = pluginName;
        this.logger = new Slf4jLogger(logger);
        this.elementName = elementName;
        this.directory = directory;
        this.configGetter = configGetter;
        this.keyConverter = keyConverter;
        this.converter = converter;
    }

    @NotNull
    protected List<File> getFiles() {
        File file = new File(new File(NeigeItems.getInstance().getDataFolder().getParentFile(), pluginName), directory);
        if (file.isDirectory()) {
            return ConfigUtils.getAllFiles(file);
        } else {
            return Collections.singletonList(file);
        }
    }

    @NotNull
    protected Map<K, R> getConfigs() {
        final Map<K, R> result = new HashMap<>();
        getFiles().forEach((file) -> loadFile(result, file));
        return result;
    }

    protected void loadFile(@NotNull Map<K, R> result, @NotNull File file) {
        if (!file.getName().endsWith(".yml")) return;
        try {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            loadConfig(result, config);
        } catch (Throwable throwable) {
            logger.warn("error occurred while loading file: " + file.getName(), throwable);
        }
    }

    protected void loadConfig(@NotNull Map<K, R> result, @NotNull YamlConfiguration config) {
        String currentKey = "";
        try {
            for (String rawKey : config.getKeys(false)) {
                currentKey = rawKey;
                R value = configGetter.apply(config, rawKey);
                if (value == null && notNullConfig) return;
                K key = keyConverter.apply(rawKey);
                if (key == null) {
                    throw new InvalidParameterException("convert result of current key is null! current key: " + rawKey);
                }
                result.put(key, value);
            }
        } catch (Throwable throwable) {
            logger.warn("error occurred while loading config, current key: " + currentKey + ", config content: \n" + config.saveToString(), throwable);
        }
    }

    public void reload() {
        clear();
        load();
    }

    protected void load() {
        getConfigs().forEach((id, content) -> {
            try {
                V result = converter.apply(id, content);
                if (result == null) return;
                put(id, result);
            } catch (Throwable throwable) {
                logger.warn("error occurred while loading " + elementName + ": " + id, throwable);
            }
        });
    }
}
