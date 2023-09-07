package pers.neige.neigeitems.hook.easyitem

import org.bukkit.inventory.ItemStack

/**
 * EasyItem挂钩
 */
abstract class EasyItemHooker {
    /**
     * 获取EI物品, 不存在对应ID的EI物品则返回null
     *
     * @param id EI物品ID
     * @return EI物品(不存在则返回null)
     */
    abstract fun getItemStack(id: String): ItemStack?

    /**
     * 是否存在对应ID的EI物品
     *
     * @param id EI物品ID
     * @return 是否存在
     */
    abstract fun hasItem(id: String): Boolean
}