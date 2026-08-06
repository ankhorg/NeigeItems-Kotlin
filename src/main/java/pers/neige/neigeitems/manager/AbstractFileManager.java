package pers.neige.neigeitems.manager;

import lombok.NonNull;
import lombok.val;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.NeigeItems;
import pers.neige.neigeitems.manager.logger.ILogger;
import pers.neige.neigeitems.manager.logger.JavaLogger;
import pers.neige.neigeitems.manager.logger.Slf4jLogger;
import pers.neige.neigeitems.utils.ConfigUtils;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public abstract class AbstractFileManager<K, V, P, F extends AbstractFileManager.FileConfig> extends ConcurrentHashMap<K, V> {
    protected final @Nullable JavaPlugin plugin;
    protected final @NonNull String pluginName;
    protected final @NonNull ILogger logger;
    protected final @NonNull String elementName;
    protected final @NonNull String directory;
    protected final @NonNull ConcurrentHashMap<String, F> fileConfigs = new ConcurrentHashMap<>();
    protected final @NonNull ConcurrentHashMap<K, P> rawConfigs = new ConcurrentHashMap<>();

    public AbstractFileManager(
        @NonNull JavaPlugin plugin,
        @NonNull String elementName,
        @NonNull String directory
    ) {
        this.plugin = plugin;
        this.pluginName = plugin.getName();
        this.logger = new JavaLogger(plugin.getLogger());
        this.elementName = elementName;
        this.directory = directory;
    }

    public AbstractFileManager(
        @NonNull String pluginName,
        @NonNull Logger logger,
        @NonNull String elementName,
        @NonNull String directory
    ) {
        this.plugin = null;
        this.pluginName = pluginName;
        this.logger = new JavaLogger(logger);
        this.elementName = elementName;
        this.directory = directory;
    }

    public AbstractFileManager(
        @NonNull String pluginName,
        @NonNull org.slf4j.Logger logger,
        @NonNull String elementName,
        @NonNull String directory
    ) {
        this.plugin = null;
        this.pluginName = pluginName;
        this.logger = new Slf4jLogger(logger);
        this.elementName = elementName;
        this.directory = directory;
    }

    public @NonNull ConcurrentHashMap<String, F> getFileConfigs() {
        return fileConfigs;
    }

    public @NonNull ConcurrentHashMap<K, P> getRawConfigs() {
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
     * 创建文件配置对象
     */
    protected abstract @NonNull F createFileConfig(
        @NonNull String path,
        @NonNull File file
    );

    /**
     * 根据 getFiles 方法获取所有文件对象, 解析相对路径, 并存储至 fileConfigs 字段
     */
    protected void loadConfigs() {
        fileConfigs.clear();
        for (val file : getFiles()) {
            loadFile(file);
        }
    }

    /**
     * 加载并处理所有文件
     */
    protected void loadRawConfigs() {
        loadConfigs();
        rawConfigs.clear();
        fileConfigs.values().forEach((fileConfig) -> {
            try {
                loadRawConfig(fileConfig);
            } catch (Throwable throwable) {
                logger.warn("error occurred while processing " + elementName + " file: " + fileConfig.getPath(), throwable);
            }
        });
    }

    /**
     * 处理单个文件的解析结果
     */
    protected abstract void loadRawConfig(@NonNull F fileConfig);

    /**
     * 加载单个文件
     */
    protected void loadFile(@NonNull File file) {
        val prefix = "plugins" + File.separator + pluginName + File.separator + directory + File.separator;
        try {
            String path = file.getPath();
            if (path.startsWith(prefix)) {
                path = path.substring(prefix.length());
            }
            val fileConfig = createFileConfig(path, file);
            fileConfigs.put(path, fileConfig);
        } catch (Throwable throwable) {
            logger.warn("error occurred while loading " + elementName + " file: " + file.getPath(), throwable);
        }
    }

    public static class FileConfig {
        private final @NonNull String path;
        private final @NonNull File file;

        public FileConfig(@NonNull String path, @NonNull File file) {
            this.path = path;
            this.file = file;
        }

        public @NonNull String getPath() {
            return path;
        }

        public @NonNull File getFile() {
            return file;
        }
    }
}
