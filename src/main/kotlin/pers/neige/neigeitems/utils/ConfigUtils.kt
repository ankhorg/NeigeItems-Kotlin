package pers.neige.neigeitems.utils

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import pers.neige.neigeitems.item.ItemConfig
import pers.neige.neigeitems.manager.ItemManager
import taboolib.common.platform.function.getDataFolder
import java.io.File

object ConfigUtils {
    // 获取文件夹内所有文件
    @JvmStatic
    fun getAllFiles(dir: File): MutableList<File> {
        val list: MutableList<File> = mutableListOf<File>()
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
    fun getAllFiles(dir: String): MutableList<File> {
        return getAllFiles(File(getDataFolder(), File.separator + dir))
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
    fun loadItemConfigs(file: File): MutableList<ConfigurationSection> {
        val list: MutableList<ConfigurationSection> = mutableListOf<ConfigurationSection>()
        val config = YamlConfiguration.loadConfiguration(file)
        config.getKeys(false).forEach { key ->
            config.getConfigurationSection(key)?.let { configSection ->
                list.add(configSection)
            }
        }
        return list
    }

    // 获取文件中所有ConfigurationSection
    @JvmStatic
    fun loadItemConfigs(files: MutableList<File>): MutableList<ConfigurationSection> {
        val list: MutableList<ConfigurationSection> = mutableListOf<ConfigurationSection>()
        for (file: File in files) {
            list.addAll(loadItemConfigs(file))
        }
        return list
    }
}