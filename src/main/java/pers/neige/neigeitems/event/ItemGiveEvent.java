package pers.neige.neigeitems.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

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
    @NotNull
    private final String id;
    @NotNull
    private final Player player;
    @NotNull
    private ItemStack itemStack;
    private int amount;

    /**
     * @param id 物品ID
     * @param player 物品接收者
     * @param itemStack 待给予物品
     */
    public ItemGiveEvent(
            @NotNull String id,
            @NotNull Player player,
            @NotNull ItemStack itemStack
    ) {
        this.id = id;
        this.player = player;
        this.itemStack = itemStack;
        this.amount = itemStack.getAmount();
    }

    /**
     * @param id 物品ID
     * @param player 物品接收者
     * @param itemStack 待给予物品
     * @param amount 给予数量
     */
    public ItemGiveEvent(
            @NotNull String id,
            @NotNull Player player,
            @NotNull ItemStack itemStack,
            int amount
    ) {
        this.id = id;
        this.player = player;
        this.itemStack = itemStack;
        this.amount = amount;
    }

    /**
     * 获取物品ID
     */
    @NotNull
    public String getId() {
        return id;
    }

    /**
     * 获取物品接收者
     */
    @NotNull
    public Player getPlayer() {
        return player;
    }

    /**
     * 获取待给予物品
     */
    @NotNull
    public ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * 设置待给予物品
     *
     * @param itemStack 待给予物品
     */
    public void setItemStack(
            @NotNull ItemStack itemStack
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
    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}