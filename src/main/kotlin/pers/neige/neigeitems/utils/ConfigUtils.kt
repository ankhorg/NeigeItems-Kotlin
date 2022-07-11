package pers.neige.neigeitems.utils

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import pers.neige.neigeitems.utils.ConfigUtils.coverWith
import taboolib.common.platform.function.getDataFolder
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagType
import java.io.File

object ConfigUtils {
    // 获取文件夹内所有文件
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

    // 获取文件夹内所有文件
    @JvmStatic
    fun getAllFiles(dir: String): ArrayList<File> {
        return getAllFiles(File(getDataFolder(), File.separator + dir))
    }

    // 获取文件夹内所有文件
    @JvmStatic
    fun getAllFiles(plugin: Plugin, dir: String): ArrayList<File> {
        return getAllFiles(File(plugin.dataFolder, File.separator + dir))
    }

    // 克隆ConfigurationSection
    @JvmStatic
    fun ConfigurationSection.clone(): ConfigurationSection {
        val tempConfigSection = YamlConfiguration() as ConfigurationSection
        this.getKeys(false).forEach { key ->
            tempConfigSection.set(key, this.get(key))
        }
        return tempConfigSection
    }

    // 获取文件中所有ConfigurationSection
    @JvmStatic
    fun File.getConfigSections(): ArrayList<ConfigurationSection> {
        val list = ArrayList<ConfigurationSection>()
        val config = YamlConfiguration.loadConfiguration(this)
        config.getKeys(false).forEach { key ->
            config.getConfigurationSection(key)?.let { list.add(it) }
        }
        return list
    }

    // 获取文件中所有ConfigurationSection
    @JvmStatic
    fun ArrayList<File>.getConfigSections(): ArrayList<ConfigurationSection> {
        val list = ArrayList<ConfigurationSection>()
        for (file: File in this) {
            list.addAll(file.getConfigSections())
        }
        return list
    }

    // 获取文件中所有ConfigurationSection
    @JvmStatic
    fun File.getContents(): ArrayList<Any> {
        val list = ArrayList<Any>()
        val config = YamlConfiguration.loadConfiguration(this)
        config.getKeys(false).forEach { key ->
            config.get(key)?.let { list.add(it) }
        }
        return list
    }

    // 获取文件中所有ConfigurationSection
    @JvmStatic
    fun ArrayList<File>.getContents(): ArrayList<Any> {
        val list = ArrayList<Any>()
        for (file: File in this) {
            list.addAll(file.getContents())
        }
        return list
    }

    // ConfigurationSection 转 HashMap
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

    // ConfigurationSection 转 HashMap
    @JvmStatic
    fun ConfigurationSection.toMap(): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        this.getKeys(false).forEach { key ->
            toMap(this.get(key))?.let { value -> map[key] = value}
        }
        return map
    }

    // ConfigurationSection 转 String
    @JvmStatic
    fun ConfigurationSection.saveToString(name: String): String {
        val tempConfigSection = YamlConfiguration()
        tempConfigSection.set(name, this)
        return tempConfigSection.saveToString()
    }

    // String 转 ConfigurationSection
    @JvmStatic
    fun String.loadFromString(id: String): ConfigurationSection? {
        val tempConfigSection = YamlConfiguration()
        tempConfigSection.loadFromString(this)
        return tempConfigSection.getConfigurationSection(id)
    }

    // File 转 YamlConfiguration
    @JvmStatic
    fun File.loadConfiguration(): YamlConfiguration {
        return YamlConfiguration.loadConfiguration(this)
    }

    // ConfigurationSection 合并(后者覆盖前者)
    @JvmStatic
    fun ConfigurationSection.coverWith(configSection: ConfigurationSection): ConfigurationSection {
        // 遍历附加NBT
        configSection.getKeys(false).forEach { key ->
            val value = configSection.get(key)
            // 如果二者包含相同键
            val overrideValue = this.get(key)
            if (overrideValue != null) {
                // 如果二者均为ConfigurationSection
                if (overrideValue is ConfigurationSection
                    && value is ConfigurationSection) {
                    // 合并
                    this.set(key, overrideValue.coverWith(value))
                } else {
                    // 覆盖
                    this.set(key, value)
                }
            } else {
                // 添加
                this.set(key, value)
            }
        }
        return this
    }
}