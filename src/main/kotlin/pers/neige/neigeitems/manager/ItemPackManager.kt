package pers.neige.neigeitems.manager

import org.bukkit.configuration.file.YamlConfiguration
import pers.neige.neigeitems.item.ItemPack
import pers.neige.neigeitems.utils.ConfigUtils

object ItemPackManager {
    // 所有物品包
    val itemPacks = HashMap<String, ItemPack>()

    init {
        // 加载全部物品包
        loadItemPacks()
    }

    // 加载全部物品包
    private fun loadItemPacks() {
        for (file in ConfigUtils.getAllFiles("ItemPacks")) {
            val config = YamlConfiguration.loadConfiguration(file)
            config.getKeys(false).forEach { id ->
                val configSection = config.getConfigurationSection(id)
                configSection?.let {
                    configSection.getConfigurationSection("FancyDrop")?.let {
                        itemPacks[id] = ItemPack(id, configSection.getStringList("Items"), true, it.getString("offset.x"), it.getString("offset.y"), it.getString("angle.type"))
                    } ?: let {
                        itemPacks[id] = ItemPack(id, configSection.getStringList("Items"), false)
                    }
                }
            }
        }
    }

    // 重载物品包管理器
    fun reload() {
        itemPacks.clear()
        loadItemPacks()
    }
}