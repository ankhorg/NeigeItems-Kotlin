package pers.neige.neigeitems.listener

import org.bukkit.event.player.PlayerJoinEvent
import pers.neige.neigeitems.NeigeItems
import pers.neige.neigeitems.annotation.Listener
import pers.neige.neigeitems.task.Updater
import pers.neige.neigeitems.utils.LangUtils.sendLang

object PlayerJoinListener {
    @JvmStatic
    @Listener
    fun listener(event: PlayerJoinEvent) {
        NeigeItems.getUserManager().getOrMake(event.player.uniqueId)
        if (!event.player.isOp) return
        if (Updater.latestVersion == null || Updater.latestVersion == Updater.currentVersion) return
        event.player.sendLang(
            "Messages.findNewVersion", mapOf(
                Pair("{version}", Updater.latestVersion!!)
            )
        )
        event.player.sendLang(
            "Messages.updateLink", mapOf(
                Pair("{link}", "https://github.com/ankhorg/NeigeItems-Kotlin/releases/latest")
            )
        )
    }

//    @JvmStatic
//    @Listener
//    fun listener(event: PlayerJoinEvent) {
//        EntityPlayerUtils.getChannel(event.player)?.pipeline()
//            ?.addBefore("packet_handler", "neigeitems_packet_handler", ChannelHandler(event.player))
//    }
//
//    class ChannelHandler(val player: Player) : ChannelHandlerAdapter() {
//        override fun write(channelHandlerContext: ChannelHandlerContext, packet: Any, channelPromise: ChannelPromise) {
//            if (PacketSendEvent(player, packet).call()) {
//                super.write(channelHandlerContext, packet, channelPromise)
//            }
//        }
//
//        override fun channelRead(channelHandlerContext: ChannelHandlerContext, packet: Any) {
//            if (PacketReceiveEvent(player, packet).call()) {
//                super.channelRead(channelHandlerContext, packet)
//            }
//        }
//    }
}