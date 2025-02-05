package pers.neige.neigeitems.listener

import org.bukkit.event.player.PlayerQuitEvent
import pers.neige.neigeitems.annotation.Listener
import pers.neige.neigeitems.manager.UserManager

object PlayerQuitListener {
    @JvmStatic
    @Listener
    private fun listener(event: PlayerQuitEvent) {
        UserManager.INSTANCE.remove(event.player.uniqueId)
    }
}