package pers.neige.neigeitems.event;

import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * 物品掉落事件, /ni drop指令触发.
 *
 * @property id 物品ID
 * @property itemStack 待掉落物品
 * @property amount 掉落数量
 * @property location 掉落位置
 * @property parser 物品解析对象
 */
public final class ItemDropEvent extends CancellableEvent {
    private static final HandlerList handlers = new HandlerList();
    private final @NonNull String id;
    private final @Nullable OfflinePlayer parser;
    private @NonNull ItemStack itemStack;
    private int amount;
    private @NonNull Location location;

    /**
     * amount 取默认值 itemStack.getAmount()
     * parser 取默认值 null
     *
     * @param id        物品ID
     * @param itemStack 待掉落物品
     * @param location  掉落位置
     */
    public ItemDropEvent(
        @NonNull String id,
        @NonNull ItemStack itemStack,
        @NonNull Location location
    ) {
        this.id = id;
        this.itemStack = itemStack;
        this.amount = itemStack.getAmount();
        this.location = location;
        this.parser = null;
    }

    /**
     * parser 取默认值 null
     *
     * @param id        物品ID
     * @param itemStack 待掉落物品
     * @param amount    掉落数量
     * @param location  掉落位置
     */
    public ItemDropEvent(
        @NonNull String id,
        @NonNull ItemStack itemStack,
        int amount,
        @NonNull Location location
    ) {
        this.id = id;
        this.itemStack = itemStack;
        this.amount = amount;
        this.location = location;
        this.parser = null;
    }

    /**
     * @param id        物品ID
     * @param itemStack 待掉落物品
     * @param amount    掉落数量
     * @param location  掉落位置
     * @param parser    物品解析对象
     */
    public ItemDropEvent(
        @NonNull String id,
        @NonNull ItemStack itemStack,
        int amount,
        @NonNull Location location,
        @Nullable OfflinePlayer parser
    ) {
        this.id = id;
        this.itemStack = itemStack;
        this.amount = amount;
        this.location = location;
        this.parser = parser;
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

    /**
     * 获取掉落数量
     */
    public int getAmount() {
        return amount;
    }

    /**
     * 设置掉落数量
     *
     * @param amount 掉落数量
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * 获取掉落位置
     */
    public @NonNull Location getLocation() {
        return location;
    }

    /**
     * 设置掉落位置
     *
     * @param location 掉落位置
     */
    public void setLocation(
        @NonNull Location location
    ) {
        this.location = location;
    }

    /**
     * 获取物品解析对象
     */
    public @Nullable OfflinePlayer getParser() {
        return parser;
    }

    @Override
    public @NonNull HandlerList getHandlers() {
        return handlers;
    }
}