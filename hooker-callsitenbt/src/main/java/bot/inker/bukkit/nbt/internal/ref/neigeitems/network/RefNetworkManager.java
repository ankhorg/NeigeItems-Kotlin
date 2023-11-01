package bot.inker.bukkit.nbt.internal.ref.neigeitems.network;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/NetworkManager")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/network/Connection")
public class RefNetworkManager extends SimpleChannelInboundHandler<RefPacket<?>> {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/NetworkManager;channel:Lio/netty/channel/Channel;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/network/Connection;channel:Lio/netty/channel/Channel;")
    public Channel channel;

    @Override
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/NetworkManager;channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/server/v1_12_R1/Packet;)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/network/Connection;channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;)V")
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RefPacket<?> refPacket) {

    }
}
