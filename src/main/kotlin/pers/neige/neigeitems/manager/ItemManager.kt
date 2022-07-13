package pers.neige.neigeitems.manager

import org.bukkit.OfflinePlayer
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.item.ItemGenerator
import java.util.concurrent.ConcurrentHashMap

object ItemManager : ItemConfigManager() {
    val items: ConcurrentHashMap<String, ItemGenerator> = ConcurrentHashMap<String, ItemGenerator>()
    val itemAmount get() = itemIds.size

    init {
        loadItems()
    }

    private fun loadItems() {
        for ((id, itemConfig) in itemConfigs) {
            items[id] = ItemGenerator(itemConfig)
        }
    }

    // 重载物品管理器
    fun reload() {
        reloadItemConfigs()
        items.clear()
        loadItems()
    }
//    fun getPageAmount(): Int {
//        return Math.ceil(this.itemIds.length/config_NI.listItemAmount)
//    }

    // 获取物品生成器
    fun getItem(id: String): ItemGenerator? {
        return items[id]
    }

    // 获取物品
    fun getItemStack(id: String, player: OfflinePlayer?, data: String?): ItemStack? {
        return items[id]?.getItemStack(player, data)
    }

    // 获取物品
    fun hasItem(id: String): Boolean {
        return items.containsKey(id)
    }
}