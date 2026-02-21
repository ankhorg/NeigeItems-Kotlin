package pers.neige.neigeitems.event;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * 物品给予事件, /ni give指令触发.
 *
 * @property id 物品ID
 * @property player 物品接收者
 * @property itemStack 待给予物品
 * @property amount 给予数量
 */
public final class ItemGiveEvent extends CancellableEvent {
    private static final HandlerList handlers = new HandlerList();
    private final @NonNull String id;
    private final @NonNull Player player;
    private @NonNull ItemStack itemStack;
    private int amount;

    /**
     * @param id        物品ID
     * @param player    物品接收者
     * @param itemStack 待给予物品
     */
    public ItemGiveEvent(
        @NonNull String id,
        @NonNull Player player,
        @NonNull ItemStack itemStack
    ) {
        this.id = id;
        this.player = player;
        this.itemStack = itemStack;
        this.amount = itemStack.getAmount();
    }

    /**
     * @param id        物品ID
     * @param player    物品接收者
     * @param itemStack 待给予物品
     * @param amount    给予数量
     */
    public ItemGiveEvent(
        @NonNull String id,
        @NonNull Player player,
        @NonNull ItemStack itemStack,
        int amount
    ) {
        this.id = id;
        this.player = player;
        this.itemStack = itemStack;
        this.amount = amount;
    }

    public static @NonNull HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * 获取物品ID
     */
    public @NonNull String getId() {
        return id;
    }

    /**
     * 获取物品接收者
     */
    public @NonNull Player getPlayer() {
        return player;
    }

    /**
     * 获取待给予物品
     */
    public @NonNull ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * 设置待给予物品
     *
     * @param itemStack 待给予物品
     */
    public void setItemStack(
        @NonNull ItemStack itemStack
    ) {
        this.itemStack = itemStack;
    }

    /**
     * 获取给予数量
     */
    public int getAmount() {
        return amount;
    }

    /**
     * 设置给予数量
     *
     * @param amount 给予数量
     */
    public void setAmount(
        int amount
    ) {
        this.amount = amount;
    }

    @Override
    public @NonNull HandlerList getHandlers() {
        return handlers;
    }
}