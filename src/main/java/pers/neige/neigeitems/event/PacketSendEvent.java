package pers.neige.neigeitems.event;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.item.ItemInfo;

/**
 * 数据包发送事件(未实际启用, 监听无效).
 *
 * @property player 接收数据包的玩家
 * @property packet 发送的数据包
 */
public final class PacketSendEvent extends CancellableEvent {
    private static final HandlerList handlers = new HandlerList();
    @Nullable
    private final Player player;
    @NotNull
    private final Object packet;

    /**
     * @param player 接收数据包的玩家
     * @param packet 发送的数据包
     */
    public PacketSendEvent(
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
     * 获取接收数据包的玩家
     */
    @Nullable
    public Player getPlayer() {
        return player;
    }

    /**
     * 获取发送的数据包
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
