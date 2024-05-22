package pers.neige.neigeitems.hook.magicgem.impl

import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.hook.magicgem.MagicGemHooker
import pku.yim.magicgem.gem.GemManager

/**
 * MagicGem挂钩
 *
 * @constructor 启用MagicGem挂钩
 */
class MagicGemHookerImpl : MagicGemHooker() {
    override fun getItemStack(id: String): ItemStack? {
        return GemManager.getGemByName(id)?.realGem
    }

    override fun hasItem(id: String): Boolean {
        return GemManager.getGemByName(id) != null
    }
}