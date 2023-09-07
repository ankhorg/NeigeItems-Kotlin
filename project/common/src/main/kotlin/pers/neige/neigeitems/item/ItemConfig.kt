package pers.neige.neigeitems.item

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

/**
 * 物品基础配置
 *
 * @property id 物品ID
 * @property file 物品所在文件
 * @param config 文件转换来的YamlConfiguration
 * @constructor 根据ID及文件获取物品配置节点
 */
class ItemConfig(val id: String, val file: File, config: ConfigurationSection = YamlConfiguration.loadConfiguration(file)) {
    /**
     * 获取物品原始配置
     */
    val configSection = config.getConfigurationSection(id)
}