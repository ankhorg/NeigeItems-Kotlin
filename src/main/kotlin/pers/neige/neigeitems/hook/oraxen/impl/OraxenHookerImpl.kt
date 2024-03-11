package pers.neige.neigeitems.hook.easyitem.impl

import io.th0rgal.oraxen.api.OraxenItems
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.hook.easyitem.OraxenHooker

/**
 * Oraxen挂钩
 *
 * @constructor 启用Oraxen挂钩
 */
class OraxenHookerImpl : OraxenHooker() {
    override fun getItemStack(id: String): ItemStack? {
        return OraxenItems.getItemById(id).build()
    }

    override fun hasItem(id: String): Boolean {
        return OraxenItems.exists(id)
    }
}