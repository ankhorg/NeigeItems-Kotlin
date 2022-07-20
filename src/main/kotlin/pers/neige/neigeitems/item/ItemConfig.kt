package pers.neige.neigeitems.item

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

// 物品加载是分两段进行的, 先加载全部基础配置, 再逐个进行继承和全局节点加载
// 所以要有ItemConfig和ItemConfigManager
class ItemConfig(val id: String, val file: File) {
    val configSection = YamlConfiguration.loadConfiguration(file).getConfigurationSection(id)
}