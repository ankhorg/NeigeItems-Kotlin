package pers.neige.neigeitems.manager;

import lombok.NonNull;
import lombok.val;
import lombok.var;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.NeigeItems;
import pers.neige.neigeitems.manager.logger.ILogger;
import pers.neige.neigeitems.manager.logger.JavaLogger;
import pers.neige.neigeitems.manager.logger.Slf4jLogger;
import pers.neige.neigeitems.utils.ConfigUtils;

import java.io.File;
import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.Logger;

public abstract class AbstractConfigManager<K, V, R> extends ConcurrentHashMap<K, V> {
    protected final @NonNull String pluginName;
    protected final @NonNull ILogger logger;
    protected final @NonNull BiFunction<ConfigurationSection, String, R> configGetter;
    protected final @NonNull Function<String, K> keyConverter;
    protected final @NonNull BiFunction<K, R, V> converter;
    protected final @NonNull String elementName;
    protected final @NonNull String directory;
    protected final @NonNull ConcurrentHashMap<String, FileConfig> fileConfigs = new ConcurrentHashMap<>();
    protected final @NonNull ConcurrentHashMap<K, RawConfig<R>> rawConfigs = new ConcurrentHashMap<>();
    protected boolean notNullConfig = true;

    public AbstractConfigManager(
            @NonNull JavaPlugin plugin,
            @NonNull String elementName,
            @NonNull String directory,
            @NonNull BiFunction<ConfigurationSection, String, R> configGetter,
            @NonNull Function<String, K> keyConverter,
            @NonNull BiFunction<K, R, V> converter
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
            @NonNull String pluginName,
            @NonNull Logger logger,
            @NonNull String elementName,
            @NonNull String directory,
            @NonNull BiFunction<ConfigurationSection, String, R> configGetter,
            @NonNull Function<String, K> keyConverter,
            @NonNull BiFunction<K, R, V> converter
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
            @NonNull String pluginName,
            @NonNull org.slf4j.Logger logger,
            @NonNull String elementName,
            @NonNull String directory,
            @NonNull BiFunction<ConfigurationSection, String, R> configGetter,
            @NonNull Function<String, K> keyConverter,
            @NonNull BiFunction<K, R, V> converter
    ) {
        this.pluginName = pluginName;
        this.logger = new Slf4jLogger(logger);
        this.elementName = elementName;
        this.directory = directory;
        this.configGetter = configGetter;
        this.keyConverter = keyConverter;
        this.converter = converter;
    }

    public @NonNull ConcurrentHashMap<String, FileConfig> getFileConfigs() {
        return fileConfigs;
    }

    public @NonNull ConcurrentHashMap<K, RawConfig<R>> getRawConfigs() {
        return rawConfigs;
    }

    /**
     * 通过给定的路径加载所有文件对象
     */
    protected @NonNull List<File> getFiles() {
        val file = new File(new File(NeigeItems.getInstance().getDataFolder().getParentFile(), pluginName), directory);
        if (file.isDirectory()) {
            return ConfigUtils.getAllFiles(file);
        } else {
            return Collections.singletonList(file);
        }
    }

    /**
     * 根据 getFiles 方法获取所有文件对象, 解析相对路径, 并进行yml解析, 存储至 fileConfigs 字段
     */
    protected void loadConfigs() {
        fileConfigs.clear();
        val prefix = "plugins" + File.separator + pluginName + File.separator + directory + File.separator;
        for (val file : getFiles()) {
            if (!file.getName().endsWith(".yml")) continue;
            try {
                var path = file.getPath();
                if (path.startsWith(prefix)) {
                    path = path.substring(prefix.length());
                }
                val fileConfig = new FileConfig(path, file);
                fileConfigs.put(path, fileConfig);
            } catch (Throwable throwable) {
                logger.warn("error occurred while loading " + elementName + " file: " + file.getPath(), throwable);
            }
        }
    }

    /**
     * 根据 loadConfigs 方法加载所有yml配置文件, 而后通过 loadRawConfig 方法逐个对配置文件进行部件拆分
     */
    protected void loadRawConfigs() {
        loadConfigs();
        rawConfigs.clear();
        fileConfigs.values().forEach((fileConfig) -> {
            try {
                loadRawConfig(fileConfig);
            } catch (Throwable throwable) {
                logger.warn("error occurred while loading " + elementName + " raw config from file: " + fileConfig.path, throwable);
            }
        });
    }

    /**
     * 解析配置文件获取配置组件
     * 默认逻辑: 通过 getKeys(false) 获取当前配置文件的所有顶级键, 然后通过 configGetter 获取对应内容, 通过 keyConverter 转换键类型
     */
    protected void loadRawConfig(@NonNull FileConfig fileConfig) {
        var currentKey = "";
        try {
            for (val rawKey : fileConfig.config.getKeys(false)) {
                currentKey = rawKey;
                val value = configGetter.apply(fileConfig.config, rawKey);
                if (value == null && notNullConfig) return;
                val key = keyConverter.apply(rawKey);
                if (key == null) {
                    throw new InvalidParameterException("convert result of " + elementName + " key is null! current key: " + rawKey);
                }
                rawConfigs.put(key, new RawConfig<>(fileConfig, value));
            }
        } catch (Throwable throwable) {
            logger.warn("error occurred while loading " + elementName + " raw config, current key: " + currentKey + ", current file: " + fileConfig.path + ", config content: \n" + fileConfig.config.saveToString(), throwable);
        }
    }

    /**
     * clear 后调用 load
     */
    public void reload() {
        clear();
        load();
    }

    /**
     * 根据 loadConfigsParts 方法加载所有配置组件, 而后通过 converter 对组件进行类型转换
     */
    public void load() {
        loadRawConfigs();
        rawConfigs.forEach((id, rawConfig) -> {
            try {
                val result = converter.apply(id, rawConfig.config);
                if (result == null) return;
                put(id, result);
            } catch (Throwable throwable) {
                logger.warn("error occurred while loading " + elementName + ", current id: " + id + ", current path: " + rawConfig.fileConfig.path, throwable);
            }
        });
    }

    public static class FileConfig {
        private final @NonNull String path;
        private final @NonNull File file;
        private final @NonNull YamlConfiguration config;

        public FileConfig(@NonNull String path, @NonNull File file) {
            this.path = path;
            this.file = file;
            this.config = YamlConfiguration.loadConfiguration(file);
        }

        public @NonNull String getPath() {
            return path;
        }

        public @NonNull File getFile() {
            return file;
        }

        public @NonNull YamlConfiguration getConfig() {
            return config;
        }
    }

    public static class RawConfig<R> {
        private final @NonNull FileConfig fileConfig;
        private final @Nullable R config;

        public RawConfig(@NonNull FileConfig fileConfig, @Nullable R config) {
            this.fileConfig = fileConfig;
            this.config = config;
        }

        public @NonNull FileConfig getFileConfig() {
            return fileConfig;
        }

        public @Nullable R getConfig() {
            return config;
        }
    }
}
