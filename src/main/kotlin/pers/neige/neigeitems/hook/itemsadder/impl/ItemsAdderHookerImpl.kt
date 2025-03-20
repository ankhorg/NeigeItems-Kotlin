package pers.neige.neigeitems.hook.itemsadder.impl

import dev.lone.itemsadder.api.CustomStack
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.hook.itemsadder.ItemsAdderHooker

/**
 * ItemsAdder挂钩
 *
 * @constructor 启用ItemsAdder挂钩
 */
class ItemsAdderHookerImpl : ItemsAdderHooker() {
    override fun getItemStack(id: String): ItemStack? {
        return CustomStack.getInstance(id)?.itemStack
    }

    override fun hasItem(id: String): Boolean {
        return CustomStack.isInRegistry(id)
    }
}