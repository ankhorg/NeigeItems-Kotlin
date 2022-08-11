package pers.neige.neigeitems.item

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

/**
 * 物品基础配置
 *
 * @property id 物品ID
 * @property file 物品所在文件
 * @constructor 根据ID及文件获取物品配置节点
 */
class ItemConfig(val id: String, val file: File) {
    /**
     * 获取物品原始配置
     */
    val configSection = YamlConfiguration.loadConfiguration(file).getConfigurationSection(id)
}