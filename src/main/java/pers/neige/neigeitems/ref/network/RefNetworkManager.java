package pers.neige.neigeitems.ref.network;

import io.netty.channel.Channel;
import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/network/Connection", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/NetworkManager", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public class RefNetworkManager
//        extends SimpleChannelInboundHandler<RefPacket<?>>
{
    @HandleBy(reference = "Lnet/minecraft/network/Connection;channel:Lio/netty/channel/Channel;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NetworkManager;channel:Lio/netty/channel/Channel;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public Channel channel;

//    @Override
//    @HandleBy(reference = "Lnet/minecraft/network/Connection;channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
//    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/NetworkManager;channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/server/v1_12_R1/Packet;)V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
//    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RefPacket<?> refPacket) {
//
//    }
}
