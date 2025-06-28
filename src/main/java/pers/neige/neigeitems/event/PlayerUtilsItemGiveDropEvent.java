package pers.neige.neigeitems.event;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * PlayerUtils 内 giveItem/giveItems 方法, 当玩家满背包导致物品需要掉落在地前触发.
 * 擅自修改 event#getItemStack 方法获取到的物品实例将导致各种问题, 比如影响后续给予的所有物品.
 *
 * @property player 物品接收者
 * @property itemStack 待掉落物品
 */
public final class PlayerUtilsItemGiveDropEvent extends CancellableEvent {
    private static final HandlerList handlers = new HandlerList();
    private final @NonNull Player player;
    private @NonNull ItemStack itemStack;

    /**
     * @param player    物品接收者
     * @param itemStack 待掉落物品
     */
    public PlayerUtilsItemGiveDropEvent(
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
     * 获取物品接收者
     */
    public @NonNull Player getPlayer() {
        return player;
    }

    /**
     * 获取待掉落物品
     */
    public @NonNull ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * 设置待掉落物品
     *
     * @param itemStack 待掉落物品
     */
    public void setItemStack(
            @NonNull ItemStack itemStack
    ) {
        this.itemStack = itemStack;
    }

    @Override
    public @NonNull HandlerList getHandlers() {
        return handlers;
    }
}