package pers.neige.neigeitems.utils

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import pers.neige.neigeitems.NeigeItems
import pers.neige.neigeitems.manager.SectionManager
import pers.neige.neigeitems.utils.FileUtils.createDirectory
import pers.neige.neigeitems.utils.FileUtils.createFile
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.nio.charset.StandardCharsets


/**
 * 配置文件相关工具类
 */
object ConfigUtils {
    /**
     * 获取文件夹内所有文件
     *
     * @param dir 待获取文件夹
     * @return 文件夹内所有文件
     */
    @JvmStatic
    fun getAllFiles(dir: File): ArrayList<File> {
        val list = ArrayList<File>()
        val files = dir.listFiles() ?: arrayOf<File>()
        for (file: File in files) {
            if (file.isDirectory) {
                list.addAll(getAllFiles(file))
            } else {
                list.add(file)
            }
        }
        return list
    }

    /**
     * 获取文件夹内所有文件
     *
     * @param dir 待获取文件夹路径
     * @return 文件夹内所有文件
     */
    @JvmStatic
    fun getAllFiles(dir: String): ArrayList<File> {
        return getAllFiles(File(NeigeItems.getInstance().dataFolder, dir))
    }

    /**
     * 获取文件夹内文件
     *
     * @param file 待获取文件路径
     * @return 对应文件
     */
    @JvmStatic
    fun getFile(file: String): File {
        return File(NeigeItems.getInstance().dataFolder, file)
    }

    /**
     * 获取文件夹内文件(不存在时返回null)
     *
     * @param file 待获取文件路径
     * @return 对应文件
     */
    @JvmStatic
    fun getFileOrNull(file: String): File? {
        return File(NeigeItems.getInstance().dataFolder, file).let {
            if (!it.exists()) null
            else it
        }
    }

    /**
     * 获取文件夹内文件(不存在时创建文件)
     *
     * @param file 待获取文件路径
     * @return 对应文件
     */
    @JvmStatic
    fun getFileOrCreate(file: String): File {
        return File(NeigeItems.getInstance().dataFolder, file).createFile()
    }

    /**
     * 获取文件夹内文件
     *
     * @param plugin 待获取文件归属插件
     * @param file 待获取文件路径
     * @return 对应文件
     */
    @JvmStatic
    fun getFile(plugin: Plugin, file: String): File {
        return File(plugin.dataFolder, file)
    }

    /**
     * 获取文件夹内文件(不存在时返回null)
     *
     * @param plugin 待获取文件归属插件
     * @param file 待获取文件路径
     * @return 对应文件
     */
    @JvmStatic
    fun getFileOrNull(plugin: Plugin, file: String): File? {
        return File(plugin.dataFolder, file).let {
            if (!it.exists()) null
            else it
        }
    }

    /**
     * 获取文件夹内文件(不存在时创建文件)
     *
     * @param plugin 待获取文件归属插件
     * @param file 待获取文件路径
     * @return 对应文件
     */
    @JvmStatic
    fun getFileOrCreate(plugin: Plugin, file: String): File {
        return File(plugin.dataFolder, file).createFile()
    }

    /**
     * 获取文件夹内所有文件
     *
     * @param plugin 待获取文件夹归属插件
     * @param dir 待获取文件夹路径
     * @return 文件夹内所有文件
     */
    @JvmStatic
    fun getAllFiles(plugin: Plugin, dir: String): ArrayList<File> {
        return getAllFiles(File(plugin.dataFolder, dir))
    }

    /**
     * 获取文件夹内所有文件
     *
     * @param plugin 待获取文件夹归属插件
     * @param dir 待获取文件夹路径
     * @return 文件夹内所有文件
     */
    @JvmStatic
    fun getAllFiles(plugin: String, dir: String): ArrayList<File> {
        return getAllFiles(File(File(NeigeItems.getInstance().dataFolder.parent, plugin), dir))
    }

    /**
     * 获取文件夹内文件
     *
     * @param plugin 待获取文件夹归属插件
     * @param file 待获取文件路径
     * @return 对应文件
     */
    @JvmStatic
    fun getFile(plugin: String, file: String): File {
        return File(File(NeigeItems.getInstance().dataFolder.parent, plugin), file)
    }

    /**
     * 获取文件夹内文件(不存在时返回null)
     *
     * @param plugin 待获取文件夹归属插件
     * @param file 待获取文件路径
     * @return 对应文件
     */
    @JvmStatic
    fun getFileOrNull(plugin: String, file: String): File? {
        return File(File(NeigeItems.getInstance().dataFolder.parent, plugin), file).let {
            if (!it.exists()) null
            else it
        }
    }

    /**
     * 获取文件夹内文件(不存在时创建文件)
     *
     * @param plugin 待获取文件夹归属插件
     * @param file 待获取文件路径
     * @return 对应文件
     */
    @JvmStatic
    fun getFileOrCreate(plugin: String, file: String): File {
        return File(File(NeigeItems.getInstance().dataFolder.parent, plugin), file).createFile()
    }

    /**
     * 深复制ConfigurationSection
     *
     * @return 对应ConfigurationSection的克隆
     */
    @JvmStatic
    fun ConfigurationSection.clone(): ConfigurationSection {
        val tempConfigSection = YamlConfiguration() as ConfigurationSection
        this.getKeys(false).forEach { key ->
            when (val value = this.get(key)) {
                is ConfigurationSection -> tempConfigSection.set(key, value.clone())
                is List<*> -> tempConfigSection.set(key, value.clone())
                else -> tempConfigSection.set(key, value)
            }
        }
        return tempConfigSection
    }

    /**
     * 深复制List
     *
     * @return 对应List的克隆
     */
    @JvmStatic
    fun List<*>.clone(): List<*> {
        return arrayListOf<Any?>().also { list ->
            forEach { value ->
                when (value) {
                    is ConfigurationSection -> list.add(value.clone())
                    is List<*> -> list.add(value.clone())
                    is Map<*, *> -> list.add(value.clone())
                    else -> list.add(value)
                }
            }
        }
    }

    /**
     * 深复制Map
     *
     * @return 对应Map的克隆
     */
    @JvmStatic
    fun Map<*, *>.clone(): Map<*, *> {
        return hashMapOf<Any?, Any?>().also { map ->
            forEach { (key, value) ->
                when (value) {
                    is ConfigurationSection -> map[key] = value.clone()
                    is List<*> -> map[key] = value.clone()
                    is Map<*, *> -> map[key] = value.clone()
                    else -> map[key] = value
                }
            }
        }
    }

    /**
     * 获取文件中所有ConfigurationSection
     *
     * @return 文件中所有ConfigurationSection
     */
    @JvmStatic
    fun File.getConfigSections(): ArrayList<ConfigurationSection> {
        val list = ArrayList<ConfigurationSection>()
        val config = YamlConfiguration.loadConfiguration(this)
        config.getKeys(false).forEach { key ->
            config.getConfigurationSection(key)?.let { list.add(it) }
        }
        return list
    }

    /**
     * 获取所有文件中所有ConfigurationSection
     *
     * @return 文件中所有ConfigurationSection
     */
    @JvmStatic
    fun List<File>.getConfigSections(): ArrayList<ConfigurationSection> {
        val list = ArrayList<ConfigurationSection>()
        for (file: File in this) {
            list.addAll(file.getConfigSections())
        }
        return list
    }

    /**
     * 获取文件中所有ConfigurationSection
     *
     * @return 文件中所有ConfigurationSection
     */
    @JvmStatic
    fun File.getConfigSectionMap(): MutableMap<String, ConfigurationSection> {
        val config = YamlConfiguration.loadConfiguration(this)
        return config.getConfigSectionMap()
    }

    /**
     * 获取所有文件中所有ConfigurationSection
     *
     * @return 文件中所有ConfigurationSection
     */
    @JvmStatic
    fun List<File>.getConfigSectionMap(): MutableMap<String, ConfigurationSection> {
        val map = HashMap<String, ConfigurationSection>()
        for (file: File in this) {
            map.putAll(file.getConfigSectionMap())
        }
        return map
    }

    /**
     * 获取文件中所有ConfigurationSection
     *
     * @return 文件中所有ConfigurationSection
     */
    @JvmStatic
    fun ConfigurationSection.getConfigSectionMap(): MutableMap<String, ConfigurationSection> {
        val map = HashMap<String, ConfigurationSection>()
        this.getKeys(false).forEach { key ->
            this.getConfigurationSection(key)?.let { map[key] = it }
        }
        return map
    }

    @JvmStatic
    fun File.getConfigStringMap(): MutableMap<String, String> {
        val config = YamlConfiguration.loadConfiguration(this)
        return config.getConfigStringMap()
    }

    @JvmStatic
    fun ConfigurationSection.getConfigStringMap(): MutableMap<String, String> {
        val map = HashMap<String, String>()
        this.getKeys(false).forEach { key ->
            this.getString(key)?.let { map[key] = it }
        }
        return map
    }

    /**
     * 获取文件中所有ConfigurationSection
     *
     * @return 文件中所有ConfigurationSection
     */
    @JvmStatic
    fun File.getMap(): MutableMap<String, Any?> {
        val map = HashMap<String, Any?>()
        val config = YamlConfiguration.loadConfiguration(this)
        config.getKeys(false).forEach { key ->
            map[key] = config.get(key)
        }
        return map
    }

    /**
     * 获取所有文件中所有ConfigurationSection
     *
     * @return 文件中所有ConfigurationSection
     */
    @JvmStatic
    fun List<File>.getMap(): MutableMap<String, Any?> {
        val map = HashMap<String, Any?>()
        for (file: File in this) {
            map.putAll(file.getMap())
        }
        return map
    }

    /**
     * 获取文件中所有顶级节点内容
     *
     * @return 文件中所有顶级节点内容
     */
    @JvmStatic
    fun File.getContents(): ArrayList<Any> {
        val list = ArrayList<Any>()
        val config = YamlConfiguration.loadConfiguration(this)
        config.getKeys(false).forEach { key ->
            config.get(key)?.let { list.add(it) }
        }
        return list
    }

    /**
     * 获取文件中所有顶级节点内容
     *
     * @return 文件中所有顶级节点内容
     */
    @JvmStatic
    fun List<File>.getContents(): ArrayList<Any> {
        val list = ArrayList<Any>()
        for (file: File in this) {
            list.addAll(file.getContents())
        }
        return list
    }

    /**
     * 获取文件中所有顶级节点内容
     *
     * @return 文件中所有顶级节点内容
     */
    @JvmStatic
    fun File.getContentMap(): MutableMap<String, Any> {
        val map = HashMap<String, Any>()
        val config = YamlConfiguration.loadConfiguration(this)
        config.getKeys(false).forEach { key ->
            config.get(key)?.let { map[key] = it }
        }
        return map
    }

    /**
     * 获取文件中所有顶级节点内容
     *
     * @return 文件中所有顶级节点内容
     */
    @JvmStatic
    fun List<File>.getContentMap(): MutableMap<String, Any> {
        val map = HashMap<String, Any>()
        for (file: File in this) {
            map.putAll(file.getContentMap())
        }
        return map
    }

    /**
     * 用于 ConfigurationSection 转 HashMap
     * ConfigurationSection 中可能包含 Map, List, ConfigurationSection 及任意值
     * 所有值的处理都放在这个方法里循环调用了,
     * 所以参数和返回值都是Any
     *
     * @param data 待转换内容
     * @return 转换结果
     */
    @JvmStatic
    fun toMap(data: Any?): Any? {
        when (data) {
            is ConfigurationSection -> {
                val map = HashMap<String, Any>()
                data.getKeys(false).forEach { key ->
                    toMap(data.get(key))?.let { value -> map[key] = value }
                }
                return map
            }

            is Map<*, *> -> {
                val map = HashMap<String, Any>()
                for ((key, value) in data) {
                    toMap(value)?.let { map[key as String] = it }
                }
                return map
            }

            is List<*> -> {
                val list = ArrayList<Any>()
                for (value in data) {
                    toMap(value)?.let { list.add(it) }
                }
                return list
            }

            else -> {
                return data
            }
        }
    }

    /**
     * ConfigurationSection 转 HashMap
     * @return 转换结果
     */
    @JvmStatic
    fun ConfigurationSection.toMap(): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        this.getKeys(false).forEach { key ->
            toMap(this.get(key))?.let { value -> map[key] = value }
        }
        return map
    }

    /**
     * ConfigurationSection 转 单层级HashMap<String, String>
     * @return 转换结果
     */
    @JvmStatic
    fun ConfigurationSection.toStringMap(): HashMap<String, String> {
        val map = HashMap<String, String>()
        this.getKeys(true).forEach { key ->
            val current = this.get(key)
            if (current is String) map[key] = current
        }
        return map
    }

    /**
     * ConfigurationSection 转 String
     * @param id 转换后呈现的节点ID, 一般可以为this.name(针对MemorySection)
     * @return 转换结果
     */
    @JvmStatic
    fun ConfigurationSection.saveToString(id: String): String {
        val tempConfigSection = YamlConfiguration()
        tempConfigSection.set(id, this)
        return tempConfigSection.saveToString()
    }

    /**
     * String 转 ConfigurationSection
     * @return 转换结果
     */
    @JvmStatic
    fun String.loadFromString(): ConfigurationSection {
        val result = YamlConfiguration()
        result.loadFromString(this)
        return result
    }

    /**
     * String 转 ConfigurationSection
     * @param id 转换前使用的节点ID
     * @return 转换结果
     */
    @JvmStatic
    fun String.loadFromString(id: String): ConfigurationSection? {
        val tempConfigSection = YamlConfiguration()
        tempConfigSection.loadFromString(this)
        return tempConfigSection.getConfigurationSection(id)
    }

    /**
     * File 转 YamlConfiguration
     * @return 转换结果
     */
    @JvmStatic
    fun File.loadConfiguration(): YamlConfiguration {
        return YamlConfiguration.loadConfiguration(this)
    }

    /**
     * ConfigurationSection 合并(后者覆盖前者, 在前者上操作)
     *
     * @param configSection 用于合并覆盖
     * @return 合并结果
     */
    @JvmStatic
    fun ConfigurationSection.coverWith(configSection: ConfigurationSection): ConfigurationSection {
        // 遍历所有键
        for (key in configSection.getKeys(false)) {
            // 用于覆盖的值
            val coverValue = configSection.get(key)
            // 原有值
            val value = this.get(key)
            // 如果二者包含相同键
            if (value != null) {
                // 如果二者均为ConfigurationSection
                if (value is ConfigurationSection && coverValue is ConfigurationSection) {
                    // 合并
                    this.set(key, value.coverWith(coverValue))
                } else {
                    // 覆盖
                    this.set(key, coverValue)
                }
            } else {
                // 添加
                this.set(key, coverValue)
            }
        }
        return this
    }

    /**
     * 用于补全config, 前者为当前config, 后者为默认config.
     * 当默认config中的某个key不存在于当前config时, 将默认值补入当前config.
     *
     * @param config 当前config
     * @param origin 默认config
     * @return 是否存在补全行为
     */
    @JvmStatic
    fun mergeIfAbsent(config: ConfigurationSection, origin: ConfigurationSection): Boolean {
        var changed = false
        for (key in origin.getKeys(true)) {
            if (!config.contains(key)) {
                config.set(key, origin.get(key))
                changed = true
            } else {
                val completeValue = origin.get(key)
                if (completeValue is ConfigurationSection && config.get(key) !is ConfigurationSection) {
                    config.set(key, completeValue)
                    changed = true
                }
            }
        }
        return changed
    }

    /**
     * 用于生成或补全插件config.
     * 当不存在config文件时, 将生成默认config文件.
     * 当存在config文件且默认config中的某个key不存在于当前config时, 默认值将补入当前config.
     */
    @JvmStatic
    fun JavaPlugin.loadConfig() {
        this.loadConfig(true)
    }

    /**
     * 用于生成或加载插件config.
     * 当不存在config文件时, 将生成默认config文件.
     * 当 fixConfig 为 true 时, 如果存在config文件且默认config中的某个key不存在于当前config, 默认值将补入当前config.
     * 当 fixConfig 为 false 时, 将直接加载config.
     *
     * @param fixConfig 是否根据默认config进行补全
     */
    @JvmStatic
    fun JavaPlugin.loadConfig(fixConfig: Boolean) {
        val origin: FileConfiguration = getResource("config.yml")?.use { input ->
            InputStreamReader(input, StandardCharsets.UTF_8).use { reader ->
                YamlConfiguration.loadConfiguration(reader)
            }
        } ?: YamlConfiguration()
        val configFile = getFileOrNull(this, "config.yml")
        if (configFile == null) {
            saveDefaultConfig()
        } else if (fixConfig) {
            val config = configFile.loadConfiguration()
            if (mergeIfAbsent(config, origin)) {
                config.save(configFile)
            }
        }
        reloadConfig()
    }

    /**
     * 全局节点加载
     *
     * @param configSection
     * @param remove 加载完成后是否把globalsections节点删除掉
     * @return 操作后配置
     */
    @JvmStatic
    fun loadGlobalSections(configSection: ConfigurationSection, remove: Boolean = true): ConfigurationSection {
        // 如果调用了全局节点
        if (configSection.contains("globalsections")) {
            // 获取全局节点ID
            val globalSectionIds = configSection.getStringList("globalsections")
            // 针对每个试图调用的全局节点
            globalSectionIds.forEach {
                when (val values = SectionManager.globalSectionMap[it]) {
                    // 对于节点调用
                    null -> {
                        SectionManager.globalSections[it]?.let { value ->
                            configSection.set("sections.$it", value)
                        }
                    }
                    // 对于节点文件调用
                    else -> {
                        values.getKeys(false).forEach {
                            configSection.set("sections.$it", values.get(it))
                        }
                    }
                }
            }
            if (remove) configSection.set("globalsections", null)
        }
        return configSection
    }

    /**
     * 保存默认文件(不进行替换)
     *
     * @param resourcePath 文件路径
     */
    @JvmStatic
    fun JavaPlugin.saveResourceNotWarn(resourcePath: String) {
        this.saveResourceNotWarn(resourcePath, File(this.dataFolder, resourcePath))
    }

    /**
     * 保存默认文件(不进行替换)
     *
     * @param resourcePath 文件路径
     * @param outFile 输出路径
     */
    @JvmStatic
    fun JavaPlugin.saveResourceNotWarn(resourcePath: String, outFile: File) {
        this.getResource(resourcePath.replace('\\', '/'))?.use { inputStream ->
            outFile.parentFile.createDirectory()
            if (outFile.exists()) return
            FileOutputStream(outFile).use { fileOutputStream ->
                var len: Int
                val buf = ByteArray(1024)
                while (inputStream.read(buf).also { len = it } > 0) {
                    (fileOutputStream as OutputStream).write(buf, 0, len)
                }
            }
        }
    }
}