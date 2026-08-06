package pers.neige.neigeitems.manager;

import lombok.NonNull;
import lombok.val;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.utils.ConfigUtils;

import java.io.File;
import java.security.InvalidParameterException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.Logger;

public abstract class AbstractConfigManager<K, V, R> extends AbstractFileManager<K, V, AbstractConfigManager.RawConfig<R>, AbstractConfigManager.FileConfig> {
    protected final @NonNull BiFunction<ConfigurationSection, String, R> configGetter;
    protected final @NonNull Function<String, K> keyConverter;
    protected final @NonNull BiFunction<K, R, V> converter;
    protected boolean notNullConfig = true;

    public AbstractConfigManager(
        @NonNull JavaPlugin plugin,
        @NonNull String elementName,
        @NonNull String directory,
        @NonNull BiFunction<ConfigurationSection, String, R> configGetter,
        @NonNull Function<String, K> keyConverter,
        @NonNull BiFunction<K, R, V> converter
    ) {
        super(plugin, elementName, directory);
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
        super(pluginName, logger, elementName, directory);
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
        super(pluginName, logger, elementName, directory);
        this.configGetter = configGetter;
        this.keyConverter = keyConverter;
        this.converter = converter;
    }

    @Override
    protected @NonNull FileConfig createFileConfig(@NonNull String path, @NonNull File file) {
        return new FileConfig(path, file);
    }

    @Override
    protected void loadFile(@NonNull File file) {
        if (!file.getName().endsWith(".yml")) return;
        super.loadFile(file);
    }

    /**
     * 解析配置文件获取配置组件
     * 默认逻辑: 通过 getKeys(false) 获取当前配置文件的所有顶级键, 然后通过 configGetter 获取对应内容, 通过 keyConverter 转换键类型
     */
    @Override
    protected void loadRawConfig(@NonNull FileConfig fileConfig) {
        String currentKey = "";
        try {
            for (val rawKey : fileConfig.config.getKeys(false)) {
                currentKey = rawKey;
                val value = configGetter.apply(fileConfig.config, rawKey);
                if (value == null && notNullConfig) return;
                val key = keyConverter.apply(rawKey);
                if (key == null) {
                    throw new InvalidParameterException("convert result of " + elementName + " key is null! current key: " + rawKey);
                }
                val pre = rawConfigs.put(key, new RawConfig<>(fileConfig, value));
                if (pre != null) {
                    throw new InvalidParameterException("duplicate key found: later values override earlier ones! current key: " + rawKey);
                }
            }
        } catch (Throwable throwable) {
            logger.warn("error occurred while loading " + elementName + " raw config, current key: " + currentKey + ", current file: " + fileConfig.getPath() + ", config content: \n" + fileConfig.config.saveToString(), throwable);
        }
    }

    /**
     * 如果 plugin 不为 null, 尝试保存目录下的 example.yml 文件
     */
    public void saveExamples() {
        if (this.plugin == null) return;
        if (ConfigUtils.getFileOrNull(this.plugin, this.directory) == null) {
            ConfigUtils.saveResourceNotWarn(this.plugin, this.directory + File.separator + "example.yml");
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
        saveExamples();
        loadRawConfigs();
        rawConfigs.forEach((id, rawConfig) -> {
            try {
                val result = converter.apply(id, rawConfig.config);
                if (result == null) return;
                put(id, result);
            } catch (Throwable throwable) {
                logger.warn("error occurred while loading " + elementName + ", current id: " + id + ", current path: " + rawConfig.fileConfig.getPath(), throwable);
            }
        });
    }

    public static class FileConfig extends AbstractFileManager.FileConfig {
        private final @NonNull YamlConfiguration config;

        public FileConfig(@NonNull String path, @NonNull File file) {
            super(path, file);
            this.config = YamlConfiguration.loadConfiguration(file);
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
