package pers.neige.neigeitems.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

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
    @NotNull
    private final String id;
    @NotNull
    private final Player player;
    @NotNull
    private List<ItemStack> itemStacks;

    /**
     * @param id         物品包ID
     * @param player     物品接收者
     * @param itemStacks 待给予物品
     */
    public ItemPackGiveEvent(
            @NotNull String id,
            @NotNull Player player,
            @NotNull List<ItemStack> itemStacks
    ) {
        this.id = id;
        this.player = player;
        this.itemStacks = itemStacks;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * 获取物品包ID
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
    public List<ItemStack> getItemStacks() {
        return itemStacks;
    }

    /**
     * 设置待给予物品
     *
     * @param itemStacks 待给予物品
     */
    public void setItemStacks(
            @NotNull List<ItemStack> itemStacks
    ) {
        this.itemStacks = itemStacks;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }
}