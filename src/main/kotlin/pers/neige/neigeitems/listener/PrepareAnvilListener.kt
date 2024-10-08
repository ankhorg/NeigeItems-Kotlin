package pers.neige.neigeitems.listener

import org.bukkit.event.inventory.PrepareAnvilEvent
import pers.neige.neigeitems.annotation.Listener
import pers.neige.neigeitems.manager.ItemManager.addCustomDurability
import pers.neige.neigeitems.utils.ItemUtils.getDamage
import pers.neige.neigeitems.utils.ItemUtils.getNbtOrNull

object PrepareAnvilListener {
    @JvmStatic
    @Listener
    private fun listener(event: PrepareAnvilEvent) {
        val origin = event.inventory.getItem(0) ?: return
        val originNbt = origin.getNbtOrNull() ?: return
        val result = event.result ?: return
        val resultNbt = result.getNbtOrNull() ?: return
        if (origin.getDamage() == result.getDamage()) return
        val originDurability = originNbt.getDeepIntOrNull("NeigeItems.durability") ?: return
        val originMaxDurability = originNbt.getDeepIntOrNull("NeigeItems.maxDurability") ?: return
        if (originDurability == resultNbt.getDeepIntOrNull("NeigeItems.durability")
            && originMaxDurability == resultNbt.getDeepIntOrNull("NeigeItems.maxDurability")
        ) {
            result.addCustomDurability(origin.getDamage() - result.getDamage())
            event.result = result
        }
    }
}