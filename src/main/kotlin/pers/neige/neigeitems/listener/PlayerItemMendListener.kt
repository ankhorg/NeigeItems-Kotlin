package pers.neige.neigeitems.listener

import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerItemMendEvent
import pers.neige.neigeitems.annotation.Listener
import pers.neige.neigeitems.item.ItemDurability

object PlayerItemMendListener {
    @JvmStatic
    @Listener(eventPriority = EventPriority.LOWEST)
    fun listener(event: PlayerItemMendEvent) {
        ItemDurability.itemMend(event)
    }
}