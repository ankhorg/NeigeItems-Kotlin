package pers.neige.neigeitems.hook.oraxen

import org.bukkit.inventory.ItemStack

/**
 * Oraxen挂钩
 */
abstract class OraxenHooker {
    /**
     * 获取Oraxen物品, 不存在对应ID的Oraxen物品则返回null
     *
     * @param id Oraxen物品ID
     * @return Oraxen物品(不存在则返回null)
     */
    abstract fun getItemStack(id: String): ItemStack?

    /**
     * 是否存在对应ID的Oraxen物品
     *
     * @param id Oraxen物品ID
     * @return 是否存在
     */
    abstract fun hasItem(id: String): Boolean
}