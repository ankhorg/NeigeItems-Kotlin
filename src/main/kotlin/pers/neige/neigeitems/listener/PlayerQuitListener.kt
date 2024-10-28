package pers.neige.neigeitems.listener

import org.bukkit.event.player.PlayerQuitEvent
import pers.neige.neigeitems.NeigeItems
import pers.neige.neigeitems.annotation.Listener

object PlayerQuitListener {
    @JvmStatic
    @Listener
    private fun listener(event: PlayerQuitEvent) {
        NeigeItems.getUserManager().getOrMake(event.player.uniqueId)
    }
}