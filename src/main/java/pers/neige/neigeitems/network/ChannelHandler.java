package pers.neige.neigeitems.network;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.NonNull;
import lombok.val;
import pers.neige.neigeitems.event.PacketReceiveEvent;
import pers.neige.neigeitems.event.PacketSendEvent;

import java.util.UUID;

public class ChannelHandler extends ChannelDuplexHandler {
    private final @NonNull UUID uuid;

    public ChannelHandler(@NonNull UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public void write(ChannelHandlerContext context, Object packet, ChannelPromise promise) throws Exception {
        try {
            if (!new PacketSendEvent(uuid, packet).call()) return;
            val packetClassName = packet.getClass().getSimpleName();
            val handler = PacketHandler.getPacketSendHandlers().get(packetClassName);
            if (handler != null) {
                boolean result = handler.apply(uuid, packet);
                if (!result) return;
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        super.write(context, packet, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object packet) throws Exception {
        if (!new PacketReceiveEvent(uuid, packet).call()) return;
        val packetClassName = packet.getClass().getSimpleName();
        val handler = PacketHandler.getPacketReceiveHandlers().get(packetClassName);
        if (handler != null && !handler.apply(uuid, packet)) return;
        super.channelRead(context, packet);
    }
}
