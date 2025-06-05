package pers.neige.neigeitems.event;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * 物品包给予事件, /ni givePack指令触发.
 *
 * @property id 物品包ID
 * @property player 物品接收者
 * @property itemStacks 待给予物品
 */
public final class ItemPackGiveEvent extends CancellableEvent {
    private static final HandlerList handlers = new HandlerList();
    private final @NonNull String id;
    private final @NonNull Player player;
    private @NonNull List<ItemStack> itemStacks;

    /**
     * @param id         物品包ID
     * @param player     物品接收者
     * @param itemStacks 待给予物品
     */
    public ItemPackGiveEvent(
            @NonNull String id,
            @NonNull Player player,
            @NonNull List<ItemStack> itemStacks
    ) {
        this.id = id;
        this.player = player;
        this.itemStacks = itemStacks;
    }

    public static @NonNull HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * 获取物品包ID
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
    public @NonNull List<ItemStack> getItemStacks() {
        return itemStacks;
    }

    /**
     * 设置待给予物品
     *
     * @param itemStacks 待给予物品
     */
    public void setItemStacks(
            @NonNull List<ItemStack> itemStacks
    ) {
        this.itemStacks = itemStacks;
    }

    @Override
    public @NonNull HandlerList getHandlers() {
        return handlers;
    }
}