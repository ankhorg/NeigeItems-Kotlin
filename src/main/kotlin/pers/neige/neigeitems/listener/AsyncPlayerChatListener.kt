package pers.neige.neigeitems.listener

import org.bukkit.event.EventPriority
import org.bukkit.event.player.AsyncPlayerChatEvent
import pers.neige.neigeitems.NeigeItems
import pers.neige.neigeitems.annotation.Listener

object AsyncPlayerChatListener {
    @JvmStatic
    @Listener(eventPriority = EventPriority.LOWEST)
    private fun listener(event: AsyncPlayerChatEvent) {
        val user = NeigeItems.getUserManager().get(event.player.uniqueId) ?: return
        val catcher = user.pollChatCatcher() ?: return
        event.isCancelled = catcher.isCancel
        catcher.future.complete(event.message)
    }
}