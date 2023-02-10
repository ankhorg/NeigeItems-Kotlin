package pers.neige.neigeitems.hook.vault.impl

import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.hook.vault.EasyItemHooker
import pers.neige.easyitem.manager.ItemManager

/**
 * Vault附属经济挂钩
 *
 * @constructor 启用Vault附属经济挂钩
 */
class EasyItemHookerImpl : EasyItemHooker() {
    override fun getItemStack(id: String): ItemStack? {
        return ItemManager.getItemStack(id)
    }

    override fun hasItem(id: String): Boolean? {
        return ItemManager.hasItem(id)
    }
}