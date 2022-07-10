package pers.neige.neigeitems.manager

import org.bukkit.configuration.ConfigurationSection
import java.io.File

object ItemManager : ItemConfigManager() {
    val itemAmount = itemIds.size

//    fun getPageAmount(): Int {
//        return Math.ceil(this.itemIds.length/config_NI.listItemAmount)
//    }

//    fun getItem(id) {
//        return items[id]
//    }
//
//    fun getItemStack(id, player, data, sender) {
//        if (this.items.containsKey(id)) {
//            return this.items[id].getItemStack(player, data, sender)
//        }
//    }

    fun getfile(id: String): File? {
        if (itemConfigs.containsKey(id)) {
            return itemConfigs[id]?.file
        }
        return null
    }

    fun getConfig(id: String): ConfigurationSection? {
        if (itemConfigs.containsKey(id)) {
            return itemConfigs[id]?.configSection
        }
        return null
    }
}