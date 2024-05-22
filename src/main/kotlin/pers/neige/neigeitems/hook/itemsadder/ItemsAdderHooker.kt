package pers.neige.neigeitems.hook.itemsadder

import org.bukkit.inventory.ItemStack

/**
 * ItemsAdder挂钩
 */
abstract class ItemsAdderHooker {
    /**
     * 获取ItemsAdder物品, 不存在对应ID的ItemsAdder物品则返回null
     *
     * @param id ItemsAdder物品ID
     * @return ItemsAdder物品(不存在则返回null)
     */
    abstract fun getItemStack(id: String): ItemStack?

    /**
     * 是否存在对应ID的ItemsAdder物品
     *
     * @param id ItemsAdder物品ID
     * @return 是否存在
     */
    abstract fun hasItem(id: String): Boolean
}