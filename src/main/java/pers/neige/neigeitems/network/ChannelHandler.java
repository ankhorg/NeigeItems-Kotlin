package pers.neige.neigeitems.network;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.NonNull;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pers.neige.neigeitems.NeigeItems;
import pers.neige.neigeitems.event.PacketReceiveEvent;
import pers.neige.neigeitems.event.PacketSendEvent;
import pers.neige.neigeitems.utils.SchedulerUtils;

import java.util.UUID;
import java.util.concurrent.RejectedExecutionException;

public class ChannelHandler extends ChannelDuplexHandler {
    private final @NonNull UUID uuid;

    public ChannelHandler(@NonNull UUID uuid) {
        this.uuid = uuid;
    }

    private boolean handleSend(@NonNull Object packet) {
        if (!new PacketSendEvent(uuid, packet).call()) return false;
        val packetClassName = packet.getClass().getSimpleName();
        val handler = PacketHandler.getPacketSendHandlers().get(packetClassName);
        return handler == null || handler.apply(uuid, packet);
    }

    private boolean handleReceive(@NonNull Object packet) {
        if (!new PacketReceiveEvent(uuid, packet).call()) return false;
        val packetClassName = packet.getClass().getSimpleName();
        val handler = PacketHandler.getPacketReceiveHandlers().get(packetClassName);
        return handler == null || handler.apply(uuid, packet);
    }

    @Override
    public void write(ChannelHandlerContext context, Object packet, ChannelPromise promise) throws Exception {
        if (SchedulerUtils.isFoliaServer()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                super.write(context, packet, promise);
                return;
            }
            try {
                SchedulerUtils.executeOnEntity(
                    NeigeItems.getInstance(),
                    player,
                    () -> {
                        boolean allowed;
                        try {
                            allowed = handleSend(packet);
                        } catch (Throwable error) {
                            try {
                                context.executor().execute(() -> promise.tryFailure(error));
                            } catch (Throwable dispatchError) {
                                promise.tryFailure(dispatchError);
                            }
                            return;
                        }
                        try {
                            context.executor().execute(() -> finishWrite(context, packet, promise, allowed));
                        } catch (RejectedExecutionException error) {
                            promise.tryFailure(error);
                        } catch (Throwable error) {
                            promise.tryFailure(error);
                        }
                    },
                    () -> promise.tryFailure(new IllegalStateException("Player region retired"))
                );
            } catch (Throwable throwable) {
                promise.tryFailure(throwable);
            }
            return;
        }
        try {
            if (!handleSend(packet)) return;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        super.write(context, packet, promise);
    }

    private void finishWrite(
        ChannelHandlerContext context,
        Object packet,
        ChannelPromise promise,
        boolean allowed
    ) {
        if (!allowed) {
            promise.setSuccess();
            return;
        }
        try {
            context.write(packet, promise);
        } catch (Throwable throwable) {
            promise.tryFailure(throwable);
        }
    }

    private void continueRead(@NonNull ChannelHandlerContext context, @NonNull Object packet, boolean allowed) {
        if (!allowed) return;
        try {
            context.fireChannelRead(packet);
        } catch (Throwable error) {
            context.fireExceptionCaught(error);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object packet) throws Exception {
        if (SchedulerUtils.isFoliaServer()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                super.channelRead(context, packet);
                return;
            }
            try {
                SchedulerUtils.executeOnEntity(
                    NeigeItems.getInstance(),
                    player,
                    () -> {
                        boolean allowed = true;
                        try {
                            allowed = handleReceive(packet);
                        } catch (Throwable error) {
                            error.printStackTrace();
                        }
                        boolean result = allowed;
                        try {
                            context.executor().execute(() -> continueRead(context, packet, result));
                        } catch (Throwable error) {
                            context.fireExceptionCaught(error);
                        }
                    },
                    () -> {
                        try {
                            context.executor().execute(() -> continueRead(context, packet, true));
                        } catch (Throwable error) {
                            context.fireExceptionCaught(error);
                        }
                    }
                );
            } catch (Throwable error) {
                context.fireExceptionCaught(error);
            }
            return;
        }
        try {
            if (!handleReceive(packet)) return;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        super.channelRead(context, packet);
    }
}
