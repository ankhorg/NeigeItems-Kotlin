package pers.neige.neigeitems.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * 服务端向客户端发送物品数据包时触发.
 *
 * @property player    接收数据包的玩家
 * @property itemStack 发送的物品
 */
public final class ItemPacketEvent extends BasicEvent {
    private static final HandlerList handlers = new HandlerList();
    @NotNull
    private final Player player;
    @NotNull
    private final ItemStack itemStack;

    /**
     * @param player    接收数据包的玩家
     * @param itemStack 发送的物品
     */
    public ItemPacketEvent(
            @NotNull Player player,
            @NotNull ItemStack itemStack
    ) {
        this.player = player;
        this.itemStack = itemStack;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * 获取接收数据包的玩家
     */
    @NotNull
    public Player getPlayer() {
        return player;
    }

    /**
     * 获取发送的物品
     */
    @NotNull
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }
}
