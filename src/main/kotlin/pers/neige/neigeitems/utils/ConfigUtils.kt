package pers.neige.neigeitems.utils

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import taboolib.common.platform.function.getDataFolder
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

    // 用于将ConfigurationSection转换为HashMap
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

    // 将ConfigurationSection转换为HashMap
    @JvmStatic
    fun ConfigurationSection.toMap(): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        this.getKeys(false).forEach { key ->
            toMap(this.get(key))?.let { value -> map[key] = value}
        }
        return map
    }

    // 将ConfigurationSection转换为String
    @JvmStatic
    fun ConfigurationSection.saveToString(): String {
        val tempConfigSection = YamlConfiguration()
        tempConfigSection.set(this.name, this)
        return tempConfigSection.saveToString()
    }

    // 将String转换为ConfigurationSection
    @JvmStatic
    fun String.loadFromString(id: String): ConfigurationSection? {
        val tempConfigSection = YamlConfiguration()
        tempConfigSection.loadFromString(this)
        return tempConfigSection.getConfigurationSection(id)
    }
}