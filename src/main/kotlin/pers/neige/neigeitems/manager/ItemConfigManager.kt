package pers.neige.neigeitems.manager

import org.bukkit.configuration.file.YamlConfiguration
import pers.neige.neigeitems.item.ItemConfig
import pers.neige.neigeitems.utils.ConfigUtils.getAllFiles
import java.io.File
import java.util.concurrent.ConcurrentHashMap

// 物品加载是分两段进行的, 先加载全部基础配置, 再逐个进行继承和全局节点加载
// 所以要有ItemConfig和ItemConfigManager
open class ItemConfigManager {
    // 全部物品文件
    val files: ArrayList<File> = getAllFiles("Items")
    // 全部物品
    val itemConfigs: ConcurrentHashMap<String, ItemConfig> = ConcurrentHashMap<String, ItemConfig>()
    // 全部物品ID
    val itemIds get() = itemConfigs.keys.toList()

    init {
        // 初始化物品配置
        loadItemConfigs()
    }

    // 初始化物品配置
    private fun loadItemConfigs() {
        for (file: File in files) {
            YamlConfiguration.loadConfiguration(file).getKeys(false).forEach { id ->
                itemConfigs[id] = ItemConfig(id, file)
            }
        }
    }

    // 重载物品配置
    fun reloadItemConfigs() {
        files.clear()
        itemConfigs.clear()
        files.addAll(getAllFiles("Items"))
        loadItemConfigs()
    }
}