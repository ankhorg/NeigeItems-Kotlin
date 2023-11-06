package pers.neige.neigeitems.event;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

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
    @NotNull
    private final LivingEntity entity;
    @NotNull
    private final String internalName;
    @NotNull
    private final String slot;
    @NotNull
    private ItemStack itemStack;

    /**
     * @param entity       怪物实体
     * @param internalName 怪物ID
     * @param slot         槽位(helmet/chestplate/leggings/boots/mainhand/offhand)
     * @param itemStack    待掉落物品
     */
    public MythicEquipEvent(
            @NotNull LivingEntity entity,
            @NotNull String internalName,
            @NotNull String slot,
            @NotNull ItemStack itemStack
    ) {
        this.entity = entity;
        this.internalName = internalName;
        this.slot = slot;
        this.itemStack = itemStack;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * 获取怪物实体
     */
    @NotNull
    public LivingEntity getEntity() {
        return entity;
    }

    /**
     * 获取怪物ID
     */
    @NotNull
    public String getInternalName() {
        return internalName;
    }

    /**
     * 获取槽位(helmet/chestplate/leggings/boots/mainhand/offhand)
     */
    @NotNull
    public String getSlot() {
        return slot;
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

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }
}