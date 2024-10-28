package pers.neige.neigeitems.network;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.event.PacketReceiveEvent;
import pers.neige.neigeitems.event.PacketSendEvent;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.PacketUtils;

import java.util.UUID;
import java.util.function.BiFunction;

public class ChannelHandler extends ChannelDuplexHandler {
    @NotNull
    private final UUID uuid;

    public ChannelHandler(@NotNull UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public void write(ChannelHandlerContext context, Object packet, ChannelPromise promise) throws Exception {
        try {
            if (!new PacketSendEvent(uuid, packet).call()) return;
            String packetClassName = packet.getClass().getSimpleName();
            BiFunction<UUID, Object, Boolean> handler = PacketHandler.getPacketSendHandlers().get(packetClassName);
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
        String packetClassName = packet.getClass().getSimpleName();
        BiFunction<UUID, Object, Boolean> handler = PacketHandler.getPacketReceiveHandlers().get(packetClassName);
        if (handler != null && !handler.apply(uuid, packet)) return;
        super.channelRead(context, packet);
    }
}
