package pers.neige.neigeitems.manager;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
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
    protected final @NotNull String pluginName;
    protected final @NotNull ILogger logger;
    protected final @NotNull BiFunction<ConfigurationSection, String, R> configGetter;
    protected final @NotNull Function<String, K> keyConverter;
    protected final @NotNull BiFunction<K, R, V> converter;
    protected final @NotNull String elementName;
    protected final @NotNull String directory;
    protected boolean notNullConfig = true;
    protected final @NotNull ConcurrentHashMap<String, FileConfig> fileConfigs = new ConcurrentHashMap<>();
    protected final @NotNull ConcurrentHashMap<K, RawConfig<R>> rawConfigs = new ConcurrentHashMap<>();

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

    public @NotNull ConcurrentHashMap<String, FileConfig> getFileConfigs() {
        return fileConfigs;
    }

    public @NotNull ConcurrentHashMap<K, RawConfig<R>> getRawConfigs() {
        return rawConfigs;
    }

    /**
     * 通过给定的路径加载所有文件对象
     */
    @NotNull
    protected List<File> getFiles() {
        File file = new File(new File(NeigeItems.getInstance().getDataFolder().getParentFile(), pluginName), directory);
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
        String prefix = "plugins" + File.separator + pluginName + File.separator + directory + File.separator;
        for (File file : getFiles()) {
            if (!file.getName().endsWith(".yml")) continue;
            try {
                String path = file.getPath();
                if (path.startsWith(prefix)) {
                    path = path.substring(prefix.length());
                }
                FileConfig fileConfig = new FileConfig(path, file);
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
    protected void loadRawConfig(@NotNull FileConfig fileConfig) {
        String currentKey = "";
        try {
            for (String rawKey : fileConfig.config.getKeys(false)) {
                currentKey = rawKey;
                R value = configGetter.apply(fileConfig.config, rawKey);
                if (value == null && notNullConfig) return;
                K key = keyConverter.apply(rawKey);
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
                V result = converter.apply(id, rawConfig.config);
                if (result == null) return;
                put(id, result);
            } catch (Throwable throwable) {
                logger.warn("error occurred while loading " + elementName + ", current id: " + id + ", current path: " + rawConfig.fileConfig.path, throwable);
            }
        });
    }

    public static class FileConfig {
        private final @NotNull String path;
        private final @NotNull File file;
        private final @NotNull YamlConfiguration config;

        public FileConfig(@NotNull String path, @NotNull File file) {
            this.path = path;
            this.file = file;
            this.config = YamlConfiguration.loadConfiguration(file);
        }

        public @NotNull String getPath() {
            return path;
        }

        public @NotNull File getFile() {
            return file;
        }

        public @NotNull YamlConfiguration getConfig() {
            return config;
        }
    }

    public static class RawConfig<R> {
        private final @NotNull FileConfig fileConfig;
        private final @Nullable R config;

        public RawConfig(@NotNull FileConfig fileConfig, @Nullable R config) {
            this.fileConfig = fileConfig;
            this.config = config;
        }

        public @NotNull FileConfig getFileConfig() {
            return fileConfig;
        }

        public @Nullable R getConfig() {
            return config;
        }
    }
}
