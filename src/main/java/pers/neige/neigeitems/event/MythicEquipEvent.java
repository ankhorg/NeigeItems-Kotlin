package pers.neige.neigeitems.event;

import lombok.NonNull;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * MM怪物通过NI设置穿戴装备事件, MM怪物生成时触发
 *
 * @property entity 怪物实体
 * @property internalName 怪物ID
 * @property slot 槽位(helmet/chestplate/leggings/boots/mainhand/offhand)
 * @property itemStack 待掉落物品
 */
public final class MythicEquipEvent extends CancellableEvent {
    private static final HandlerList handlers = new HandlerList();
    private final @NonNull LivingEntity entity;
    private final @NonNull String internalName;
    private final @NonNull String slot;
    private @NonNull ItemStack itemStack;

    /**
     * @param entity       怪物实体
     * @param internalName 怪物ID
     * @param slot         槽位(helmet/chestplate/leggings/boots/mainhand/offhand)
     * @param itemStack    待掉落物品
     */
    public MythicEquipEvent(
        @NonNull LivingEntity entity,
        @NonNull String internalName,
        @NonNull String slot,
        @NonNull ItemStack itemStack
    ) {
        this.entity = entity;
        this.internalName = internalName;
        this.slot = slot;
        this.itemStack = itemStack;
    }

    public static @NonNull HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * 获取怪物实体
     */
    public @NonNull LivingEntity getEntity() {
        return entity;
    }

    /**
     * 获取怪物ID
     */
    public @NonNull String getInternalName() {
        return internalName;
    }

    /**
     * 获取槽位(helmet/chestplate/leggings/boots/mainhand/offhand)
     */
    public @NonNull String getSlot() {
        return slot;
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

    @Override
    public @NonNull HandlerList getHandlers() {
        return handlers;
    }
}