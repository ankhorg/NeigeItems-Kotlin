package pers.neige.neigeitems.utils

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import pers.neige.neigeitems.manager.SectionManager
import taboolib.common.platform.function.getDataFolder
import taboolib.platform.BukkitPlugin
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


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
        return getAllFiles(File(getDataFolder(), File.separator + dir))
    }

    /**
     * 获取文件夹内文件
     *
     * @param file 待获取文件路径
     * @return 对应文件
     */
    @JvmStatic
    fun getFile(file: String): File {
        return File(getDataFolder(), File.separator + file)
    }

    /**
     * 获取文件夹内文件(不存在时返回null)
     *
     * @param file 待获取文件路径
     * @return 对应文件
     */
    @JvmStatic
    fun getFileOrNull(file: String): File? {
        return File(getDataFolder(), File.separator + file).let {
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
        return File(getDataFolder(), File.separator + file).also {
            if (!it.exists()) {
                val parent = it.parentFile
                if (!parent.exists()) parent.mkdirs()
                it.createNewFile()
            }
        }
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
        return getAllFiles(File(plugin.dataFolder, File.separator + dir))
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
        return getAllFiles(File(File(getDataFolder().parent, File.separator + plugin), File.separator + dir))
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
        return File(File(getDataFolder().parent, File.separator + plugin), File.separator + file)
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
        return File(File(getDataFolder().parent, File.separator + plugin), File.separator + file).let {
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
        return File(File(getDataFolder().parent, File.separator + plugin), File.separator + file).also {
            if (!it.exists()) {
                val parent = it.parentFile
                if (!parent.exists()) parent.mkdirs()
                it.createNewFile()
            }
        }
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
    fun ArrayList<File>.getConfigSections(): ArrayList<ConfigurationSection> {
        val list = ArrayList<ConfigurationSection>()
        for (file: File in this) {
            list.addAll(file.getConfigSections())
        }
        return list
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
    fun ArrayList<File>.getContents(): ArrayList<Any> {
        val list = ArrayList<Any>()
        for (file: File in this) {
            list.addAll(file.getContents())
        }
        return list
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
                    toMap(data.get(key))?.let { value -> map[key] = value}
                }
                return map
            }
            is Map<*, *> -> {
                val map = HashMap<String, Any>()
                for ((key, value) in data) {
                    toMap(value)?.let { map[key as String] = it}
                }
                return map
            }
            is List<*> -> {
                val list = ArrayList<Any>()
                for (value in data) {
                    toMap(value)?.let { list.add(it)}
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
            toMap(this.get(key))?.let { value -> map[key] = value}
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
        configSection.getKeys(false).forEach { key ->
            // 用于覆盖的值
            val coverValue = configSection.get(key)
            // 原有值
            val value = this.get(key)
            // 如果二者包含相同键
            if (value != null) {
                // 如果二者均为ConfigurationSection
                if (value is ConfigurationSection
                    && coverValue is ConfigurationSection) {
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
     */
    @JvmStatic
    fun BukkitPlugin.saveResourceNotWarn(resourcePath: String) {
        this.getResource(resourcePath.replace('\\', '/'))?.let { inputStream ->
            val outFile = File(this.dataFolder, resourcePath)
            val lastIndex: Int = resourcePath.lastIndexOf(File.separator)
            val outDir = File(this.dataFolder, resourcePath.substring(0, if (lastIndex >= 0) lastIndex else 0))
            if (!outDir.exists()) {
                outDir.mkdirs()
            }
            if (!outFile.exists()) {
                try {
                    var len: Int
                    val fileOutputStream = FileOutputStream(outFile)
                    val buf = ByteArray(1024)
                    while (inputStream.read(buf).also { len = it } > 0) {
                        (fileOutputStream as OutputStream).write(buf, 0, len)
                    }
                    fileOutputStream.close()
                    inputStream.close()
                } catch (ex: IOException) {}
            }
        }
    }
}