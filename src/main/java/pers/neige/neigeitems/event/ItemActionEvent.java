package pers.neige.neigeitems.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
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
    @NotNull
    private final Player player;
    @NotNull
    private final ItemStack itemStack;
    @NotNull
    private final ItemInfo itemInfo;
    @NotNull
    private final ItemActionType type;
    @NotNull
    private final ActionTrigger trigger;

    /**
     * @param player    持有物品的玩家
     * @param itemStack 触发动作的物品
     * @param itemInfo  物品信息
     */
    public ItemActionEvent(
            @NotNull Player player,
            @NotNull ItemStack itemStack,
            @NotNull ItemInfo itemInfo,
            @NotNull ItemActionType type,
            @NotNull ActionTrigger trigger
    ) {
        this.player = player;
        this.itemStack = itemStack;
        this.itemInfo = itemInfo;
        this.type = type;
        this.trigger = trigger;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * 获取持有物品的玩家
     */
    @NotNull
    public Player getPlayer() {
        return player;
    }

    /**
     * 获取触发动作的物品
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

    /**
     * 获取动作触发类型
     */
    @NotNull
    public ItemActionType getType() {
        return type;
    }

    /**
     * 获取动作触发器
     */
    @NotNull
    public ActionTrigger getTrigger() {
        return trigger;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }
}
