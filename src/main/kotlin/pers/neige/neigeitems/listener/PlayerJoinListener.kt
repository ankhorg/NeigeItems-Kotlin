package pers.neige.neigeitems.listener

import io.netty.channel.ChannelHandlerAdapter
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import pers.neige.neigeitems.annotation.Listener
import pers.neige.neigeitems.event.PacketReceiveEvent
import pers.neige.neigeitems.event.PacketSendEvent
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.EntityPlayerUtils

object PlayerJoinListener {
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