package pers.neige.neigeitems.event;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * 服务端向客户端发送物品数据包时触发.
 *
 * @property player    接收数据包的玩家
 * @property itemStack 发送的物品
 */
public final class ItemPacketEvent extends BasicEvent {
    private static final HandlerList handlers = new HandlerList();
    private final @NonNull Player player;
    private final @NonNull ItemStack itemStack;

    /**
     * @param player    接收数据包的玩家
     * @param itemStack 发送的物品
     */
    public ItemPacketEvent(
        @NonNull Player player,
        @NonNull ItemStack itemStack
    ) {
        this.player = player;
        this.itemStack = itemStack;
    }

    public static @NonNull HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * 获取接收数据包的玩家
     */
    public @NonNull Player getPlayer() {
        return player;
    }

    /**
     * 获取发送的物品
     */
    public @NonNull ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public @NonNull HandlerList getHandlers() {
        return handlers;
    }
}
