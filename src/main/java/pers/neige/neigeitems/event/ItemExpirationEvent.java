package pers.neige.neigeitems.event;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
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
    @Nullable
    private final OfflinePlayer player;
    @NotNull
    private final ItemStack itemStack;
    @NotNull
    private final ItemInfo itemInfo;

    /**
     * @param player 持有物品的玩家
     * @param itemStack 到期的物品
     * @param itemInfo 物品信息
     */
    public ItemExpirationEvent(
            @Nullable OfflinePlayer player,
            @NotNull ItemStack itemStack,
            @NotNull ItemInfo itemInfo
    ) {
        this.player = player;
        this.itemStack = itemStack;
        this.itemInfo = itemInfo;
    }

    /**
     * 获取持有物品的玩家
     */
    @Nullable
    public OfflinePlayer getPlayer() {
        return player;
    }

    /**
     * 获取到期的物品
     */
    @NotNull
    public ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * 获取物品信息
     */
    @NotNull
    public ItemInfo getItemInfo() {
        return itemInfo;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
