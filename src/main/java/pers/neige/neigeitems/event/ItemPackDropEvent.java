package pers.neige.neigeitems.event;

import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
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
    private final @NonNull String id;
    private final @Nullable OfflinePlayer parser;
    private @NonNull List<ItemStack> itemStacks;
    private @NonNull Location location;

    /**
     * @param id         物品ID
     * @param itemStacks 待掉落物品
     * @param location   掉落位置
     * @param parser     物品解析对象
     */
    public ItemPackDropEvent(
            @NonNull String id,
            @NonNull List<ItemStack> itemStacks,
            @NonNull Location location,
            @Nullable OfflinePlayer parser
    ) {
        this.id = id;
        this.itemStacks = itemStacks;
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
    public @NonNull List<ItemStack> getItemStacks() {
        return itemStacks;
    }

    /**
     * 设置待掉落物品
     *
     * @param itemStacks 待掉落物品
     */
    public void setItemStacks(
            @NonNull List<ItemStack> itemStacks
    ) {
        this.itemStacks = itemStacks;
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