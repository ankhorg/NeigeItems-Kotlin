package pers.neige.neigeitems.manager

import org.bukkit.configuration.file.YamlConfiguration
import pers.neige.neigeitems.item.ItemConfig
import pers.neige.neigeitems.utils.ConfigUtils.getAllFiles
import java.io.File
import java.util.concurrent.ConcurrentHashMap

open class ItemConfigManager {
    // 加载全部物品文件
    val files: ArrayList<File> = getAllFiles("Items")
    // 加载全部物品
    val itemConfigs: ConcurrentHashMap<String, ItemConfig> = ConcurrentHashMap<String, ItemConfig>()
    val itemIds = ArrayList<String>()

    init {
        loadItemConfigs()
    }

    private fun loadItemConfigs() {
        for (file: File in files) {
            YamlConfiguration.loadConfiguration(file).getKeys(false).forEach { id ->
                itemConfigs[id] = ItemConfig(id, file)
                itemIds.add(id)
            }
        }
    }

    fun reloadItemConfigs() {
        files.clear()
        itemConfigs.clear()
        itemIds.clear()
        files.addAll(getAllFiles("Items"))
        loadItemConfigs()
    }
}