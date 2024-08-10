package pers.neige.neigeitems.listener

import org.bukkit.event.player.PlayerQuitEvent
import pers.neige.neigeitems.NeigeItems
import pers.neige.neigeitems.annotation.Listener

object PlayerQuitListener {
    @JvmStatic
    @Listener
    fun listener(event: PlayerQuitEvent) {
        NeigeItems.getUserManager().getOrMake(event.player.uniqueId)
    }

//    @JvmStatic
//    @Listener
//    fun listener(event: PlayerQuitEvent) {
//        val playerChannel = EntityPlayerUtils.getChannel(event.player)
//        if (playerChannel?.pipeline()?.get("neigeitems_packet_handler") != null) {
//            playerChannel.pipeline().remove("neigeitems_packet_handler")
//        }
//    }
}