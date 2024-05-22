package pers.neige.neigeitems.hook.magicgem

import org.bukkit.inventory.ItemStack

/**
 * MagicGem挂钩
 */
abstract class MagicGemHooker {
    /**
     * 获取MagicGem物品, 不存在对应ID的MagicGem物品则返回null
     *
     * @param id MagicGem物品ID
     * @return MagicGem物品(不存在则返回null)
     */
    abstract fun getItemStack(id: String): ItemStack?

    /**
     * 是否存在对应ID的MagicGem物品
     *
     * @param id MagicGem物品ID
     * @return 是否存在
     */
    abstract fun hasItem(id: String): Boolean
}