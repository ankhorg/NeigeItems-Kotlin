package pers.neige.neigeitems.event;

import lombok.NonNull;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.item.ItemInfo;

/**
 * 物品到期事件.
 *
 * @property player 持有物品的玩家
 * @property itemStack 到期的物品
 * @property itemInfo 物品信息
 */
public final class ItemExpirationEvent extends CancellableEvent {
    private static final HandlerList handlers = new HandlerList();
    private final @Nullable OfflinePlayer player;
    private final @NonNull ItemStack itemStack;
    private final @NonNull ItemInfo itemInfo;

    /**
     * @param player    持有物品的玩家
     * @param itemStack 到期的物品
     * @param itemInfo  物品信息
     */
    public ItemExpirationEvent(
            @Nullable OfflinePlayer player,
            @NonNull ItemStack itemStack,
            @NonNull ItemInfo itemInfo
    ) {
        this.player = player;
        this.itemStack = itemStack;
        this.itemInfo = itemInfo;
    }

    public static @NonNull HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * 获取持有物品的玩家
     */
    public @Nullable OfflinePlayer getPlayer() {
        return player;
    }

    /**
     * 获取到期的物品
     */
    public @NonNull ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * 获取物品信息
     */
    public @NonNull ItemInfo getItemInfo() {
        return itemInfo;
    }

    @Override
    public @NonNull HandlerList getHandlers() {
        return handlers;
    }
}
