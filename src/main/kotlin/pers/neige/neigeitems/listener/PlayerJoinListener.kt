package pers.neige.neigeitems.listener

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