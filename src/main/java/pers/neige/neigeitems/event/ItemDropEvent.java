package pers.neige.neigeitems.event;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
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
    @NotNull
    private final String id;
    @NotNull
    private ItemStack itemStack;
    private int amount;
    @NotNull
    private Location location;
    @Nullable
    private final OfflinePlayer parser;

    /**
     * amount 取默认值 itemStack.getAmount()
     * parser 取默认值 null
     *
     * @param id 物品ID
     * @param itemStack 待掉落物品
     * @param location 掉落位置
     */
    public ItemDropEvent(
            @NotNull String id,
            @NotNull ItemStack itemStack,
            @NotNull Location location
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
     * @param id 物品ID
     * @param itemStack 待掉落物品
     * @param amount 掉落数量
     * @param location 掉落位置
     */
    public ItemDropEvent(
            @NotNull String id,
            @NotNull ItemStack itemStack,
            int amount,
            @NotNull Location location
    ) {
        this.id = id;
        this.itemStack = itemStack;
        this.amount = amount;
        this.location = location;
        this.parser = null;
    }

    /**
     * @param id 物品ID
     * @param itemStack 待掉落物品
     * @param amount 掉落数量
     * @param location 掉落位置
     * @param parser 物品解析对象
     */
    public ItemDropEvent(
            @NotNull String id,
            @NotNull ItemStack itemStack,
            int amount,
            @NotNull Location location,
            @Nullable OfflinePlayer parser
    ) {
        this.id = id;
        this.itemStack = itemStack;
        this.amount = amount;
        this.location = location;
        this.parser = parser;
    }

    /**
     * 获取物品ID
     */
    @NotNull
    public String getId() {
        return id;
    }

    /**
     * 获取待掉落物品
     */
    @NotNull
    public ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * 设置待掉落物品
     *
     * @param itemStack 待掉落物品
     */
    public void setItemStack(
            @NotNull ItemStack itemStack
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
    @NotNull
    public Location getLocation() {
        return location;
    }

    /**
     * 设置掉落位置
     *
     * @param location 掉落位置
     */
    public void setLocation(
            @NotNull Location location
    ) {
        this.location = location;
    }

    /**
     * 获取物品解析对象
     */
    @Nullable
    public OfflinePlayer getParser() {
        return parser;
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