package pers.neige.neigeitems.manager

import org.bukkit.configuration.file.YamlConfiguration
import pers.neige.neigeitems.item.ItemPack
import pers.neige.neigeitems.utils.ConfigUtils
import java.util.concurrent.ConcurrentHashMap

/**
 * 物品包管理器
 *
 * @constructor 构建物品包管理器
 */
object ItemPackManager {
    /**
     * 获取所有物品包
     */
    val itemPacks = ConcurrentHashMap<String, ItemPack>()

    /**
     * 全部物品包ID(已排序)
     */
    val itemPackIds get() = itemPacks.keys.toList().sorted()

    /**
     * 全部物品包ID(未排序)
     */
    val itemPackIdsRaw get() = itemPacks.keys.toList()

    init {
        // 加载全部物品包
        loadItemPacks()
    }

    /**
     * 获取物品包
     *
     * @param id 物品包ID
     * @return 物品包(无对应物品包返回null)
     */
    fun getItemPack(id: String): ItemPack? {
        return itemPacks[id]
    }

    /**
     * 加载全部物品包
     */
    private fun loadItemPacks() {
        for (file in ConfigUtils.getAllFiles("ItemPacks")) {
            val config = YamlConfiguration.loadConfiguration(file)
            config.getKeys(false).forEach { id ->
                val configSection = config.getConfigurationSection(id)
                configSection?.let {
                    itemPacks[id] = ItemPack(id, configSection)
                }
            }
        }
    }

    /**
     * 重载物品包管理器
     */
    fun reload() {
        itemPacks.clear()
        loadItemPacks()
    }
}