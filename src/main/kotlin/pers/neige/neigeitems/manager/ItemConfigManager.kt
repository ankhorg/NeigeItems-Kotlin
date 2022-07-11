package pers.neige.neigeitems.manager

import org.bukkit.configuration.file.YamlConfiguration
import pers.neige.neigeitems.item.ItemConfig
import pers.neige.neigeitems.utils.ConfigUtils.getAllFiles
import java.io.File
import java.util.concurrent.ConcurrentHashMap

open class ItemConfigManager {
    // 加载全部物品文件
    val files: ArrayList<File> by lazy { getAllFiles("Items") }
    // 加载全部物品
    val itemConfigs: ConcurrentHashMap<String, ItemConfig> = ConcurrentHashMap<String, ItemConfig>()
    val itemIds = ArrayList<String>()
    init {
        loadItemConfigs()
    }

    private fun loadItemConfigs() {
        for (file: File in files) {
            val config = YamlConfiguration.loadConfiguration(file)
            val ids = config.getKeys(false)
            ids.forEach {
                itemConfigs[it] = ItemConfig(it, file)
                itemIds.add(it)
            }
        }
    }
}