package pers.neige.neigeitems.hook.easyitem.impl

import org.bukkit.inventory.ItemStack
import pers.neige.easyitem.manager.ItemManager
import pers.neige.neigeitems.hook.easyitem.EasyItemHooker

/**
 * EasyItem挂钩
 *
 * @constructor 启用EasyItem挂钩
 */
class EasyItemHookerImpl : EasyItemHooker() {
    override fun getItemStack(id: String): ItemStack? {
        return ItemManager.getItemStack(id)
    }

    override fun hasItem(id: String): Boolean {
        return ItemManager.hasItem(id)
    }
}