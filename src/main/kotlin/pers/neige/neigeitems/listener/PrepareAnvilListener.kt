package pers.neige.neigeitems.listener

import org.bukkit.event.inventory.PrepareAnvilEvent
import pers.neige.neigeitems.annotation.Listener
import pers.neige.neigeitems.manager.ItemManager.addCustomDurability
import pers.neige.neigeitems.utils.ItemUtils.getNbtOrNull

object PrepareAnvilListener {
    @JvmStatic
    @Listener
    fun listener(event: PrepareAnvilEvent) {
        val origin = event.inventory.getItem(0) ?: return
        val originNbt = origin.getNbtOrNull() ?: return
        val result = event.inventory.getItem(2) ?: return
        val resultNbt = result.getNbtOrNull() ?: return
        if (origin.durability == result.durability) return
        val originDurability = originNbt.getDeepIntOrNull("NeigeItems.durability") ?: return
        val originMaxDurability = originNbt.getDeepIntOrNull("NeigeItems.maxDurability") ?: return
        if (originDurability == resultNbt.getDeepIntOrNull("NeigeItems.durability")
            && originMaxDurability == resultNbt.getDeepIntOrNull("NeigeItems.maxDurability")
        ) {
            result.addCustomDurability(origin.durability - result.durability)
            event.result = result
        }
    }
}