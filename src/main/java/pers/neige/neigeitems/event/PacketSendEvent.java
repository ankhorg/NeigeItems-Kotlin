package pers.neige.neigeitems.event;

import lombok.NonNull;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * 数据包发送事件.
 *
 * @property uuid   接收数据包的玩家 UUID
 * @property packet 发送的数据包
 */
public final class PacketSendEvent extends CancellableEvent {
    private static final HandlerList handlers = new HandlerList();
    private final @NonNull UUID uuid;
    private final @NonNull Object packet;

    /**
     * @param uuid   接收数据包的玩家 UUID
     * @param packet 发送的数据包
     */
    public PacketSendEvent(
            @NonNull UUID uuid,
            @NonNull Object packet
    ) {
        this.uuid = uuid;
        this.packet = packet;
    }

    public static @NonNull HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * 获取接收数据包的玩家 UUID
     */
    public @NonNull UUID getUuid() {
        return uuid;
    }

    /**
     * 获取发送的数据包
     */
    public @NonNull Object getPacket() {
        return packet;
    }


    @Override
    public @NonNull HandlerList getHandlers() {
        return handlers;
    }
}
