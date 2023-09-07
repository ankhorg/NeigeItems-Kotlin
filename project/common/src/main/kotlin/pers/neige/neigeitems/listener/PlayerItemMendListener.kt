package pers.neige.neigeitems.listener

import org.bukkit.event.player.PlayerItemMendEvent
import pers.neige.neigeitems.item.ItemDurability
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent

object PlayerItemMendListener {
    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun listener(event: PlayerItemMendEvent) {
        ItemDurability.itemMend(event)
    }
}