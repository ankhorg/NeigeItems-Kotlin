package pers.neige.neigeitems.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 数据包接收事件(未实际启用, 监听无效).
 *
 * @property player 发送数据包的玩家
 * @property packet 接收的数据包
 */
public final class PacketReceiveEvent extends CancellableEvent {
    private static final HandlerList handlers = new HandlerList();
    @Nullable
    private final Player player;
    @NotNull
    private final Object packet;

    /**
     * @param player 发送数据包的玩家
     * @param packet 接收的数据包
     */
    public PacketReceiveEvent(
            @Nullable Player player,
            @NotNull Object packet
    ) {
        this.player = player;
        this.packet = packet;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * 获取发送数据包的玩家
     */
    @Nullable
    public Player getPlayer() {
        return player;
    }

    /**
     * 获取接收的数据包
     */
    @Nullable
    public Object getPacket() {
        return packet;
    }


    @Override
    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }
}
