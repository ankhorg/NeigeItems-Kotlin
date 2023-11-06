package pers.neige.neigeitems.event;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 物品包掉落事件, /ni dropPack指令触发.
 *
 * @property id 物品ID
 * @property itemStacks 待掉落物品
 * @property location 掉落位置
 * @property parser 物品解析对象
 */
public final class ItemPackDropEvent extends CancellableEvent {
    private static final HandlerList handlers = new HandlerList();
    @NotNull
    private final String id;
    @Nullable
    private final OfflinePlayer parser;
    @NotNull
    private List<ItemStack> itemStacks;
    @NotNull
    private Location location;

    /**
     * @param id         物品ID
     * @param itemStacks 待掉落物品
     * @param location   掉落位置
     * @param parser     物品解析对象
     */
    public ItemPackDropEvent(
            @NotNull String id,
            @NotNull List<ItemStack> itemStacks,
            @NotNull Location location,
            @Nullable OfflinePlayer parser
    ) {
        this.id = id;
        this.itemStacks = itemStacks;
        this.location = location;
        this.parser = parser;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
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
    public List<ItemStack> getItemStacks() {
        return itemStacks;
    }

    /**
     * 设置待掉落物品
     *
     * @param itemStacks 待掉落物品
     */
    public void setItemStacks(
            @NotNull List<ItemStack> itemStacks
    ) {
        this.itemStacks = itemStacks;
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
}