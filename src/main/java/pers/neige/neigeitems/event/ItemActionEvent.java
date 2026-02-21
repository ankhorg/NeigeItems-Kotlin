package pers.neige.neigeitems.event;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import pers.neige.neigeitems.item.ItemInfo;
import pers.neige.neigeitems.item.action.ActionTrigger;
import pers.neige.neigeitems.item.action.ItemActionType;

/**
 * 物品动作触发事件, 将在确定不在冷却时间后, 准备检测物品消耗前触发.
 *
 * @property player 持有物品的玩家
 * @property itemStack 触发动作的物品
 * @property itemInfo 物品信息
 * @property type 动作触发类型
 * @property trigger 动作触发器
 */
public final class ItemActionEvent extends CancellableEvent {
    private static final HandlerList handlers = new HandlerList();
    private final @NonNull Player player;
    private final @NonNull ItemStack itemStack;
    private final @NonNull ItemInfo itemInfo;
    private final @NonNull ItemActionType type;
    private final @NonNull ActionTrigger trigger;

    /**
     * @param player    持有物品的玩家
     * @param itemStack 触发动作的物品
     * @param itemInfo  物品信息
     */
    public ItemActionEvent(
        @NonNull Player player,
        @NonNull ItemStack itemStack,
        @NonNull ItemInfo itemInfo,
        @NonNull ItemActionType type,
        @NonNull ActionTrigger trigger
    ) {
        this.player = player;
        this.itemStack = itemStack;
        this.itemInfo = itemInfo;
        this.type = type;
        this.trigger = trigger;
    }

    public static @NonNull HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * 获取持有物品的玩家
     */
    public @NonNull Player getPlayer() {
        return player;
    }

    /**
     * 获取触发动作的物品
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

    /**
     * 获取动作触发类型
     */
    public @NonNull ItemActionType getType() {
        return type;
    }

    /**
     * 获取动作触发器
     */
    public @NonNull ActionTrigger getTrigger() {
        return trigger;
    }

    @Override
    public @NonNull HandlerList getHandlers() {
        return handlers;
    }
}
