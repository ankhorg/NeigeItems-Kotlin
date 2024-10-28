package pers.neige.neigeitems.event;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * 数据包发送事件.
 *
 * @property uuid   接收数据包的玩家 UUID
 * @property packet 发送的数据包
 */
public final class PacketSendEvent extends CancellableEvent {
    private static final HandlerList handlers = new HandlerList();
    @NotNull
    private final UUID uuid;
    @NotNull
    private final Object packet;

    /**
     * @param uuid   接收数据包的玩家 UUID
     * @param packet 发送的数据包
     */
    public PacketSendEvent(
            @NotNull UUID uuid,
            @NotNull Object packet
    ) {
        this.uuid = uuid;
        this.packet = packet;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * 获取接收数据包的玩家 UUID
     */
    @NotNull
    public UUID getUuid() {
        return uuid;
    }

    /**
     * 获取发送的数据包
     */
    @NotNull
    public Object getPacket() {
        return packet;
    }


    @Override
    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }
}
