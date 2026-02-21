package pers.neige.neigeitems.event;

import lombok.NonNull;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * MM怪物掉落NI装备事件, MM怪物死亡时异步触发.
 */
public final class MythicDropEvent {
    /**
     * MM怪物掉落NI装备事件, MM怪物死亡时异步触发.
     * 加载好配置, 还没生成物品时触发.
     *
     * @property internalName 怪物ID
     * @property entity 怪物实体(你可能需要通过编辑它身上的装备来改变掉落装备)
     * @property killer 怪物击杀者
     * @property drops NeigeItems.Drop
     * @property fishDrops NeigeItems.FishDrop
     * @property dropPacks NeigeItems.DropPacks
     * @property offsetXString NeigeItems.FancyDrop.offset.x(可能在后续被物品包配置覆盖)
     * @property offsetYString NeigeItems.FancyDrop.offset.y(可能在后续被物品包配置覆盖)
     * @property angleType NeigeItems.FancyDrop.offset.angle.type(可能在后续被物品包配置覆盖)
     */
    public static class ConfigLoaded extends CancellableEvent {
        private static final HandlerList handlers = new HandlerList();
        private final @NonNull String internalName;
        private final @NonNull LivingEntity entity;
        private final @Nullable LivingEntity killer;
        private @Nullable List<String> drops;
        private @Nullable List<String> fishDrops;
        private @Nullable List<String> dropPacks;
        private @Nullable String offsetXString;
        private @Nullable String offsetYString;
        private @Nullable String angleType;

        /**
         * @param internalName  怪物ID
         * @param entity        怪物实体(你可能需要通过编辑它身上的装备来改变掉落装备)
         * @param killer        怪物击杀者
         * @param drops         NeigeItems.Drop
         * @param fishDrops     NeigeItems.FishDrop
         * @param dropPacks     NeigeItems.DropPacks
         * @param offsetXString NeigeItems.FancyDrop.offset.x(可能在后续被物品包配置覆盖)
         * @param offsetYString NeigeItems.FancyDrop.offset.y(可能在后续被物品包配置覆盖)
         * @param angleType     NeigeItems.FancyDrop.offset.angle.type(可能在后续被物品包配置覆盖)
         */
        public ConfigLoaded(
            @NonNull String internalName,
            @NonNull LivingEntity entity,
            @Nullable LivingEntity killer,
            @Nullable List<String> drops,
            @Nullable List<String> fishDrops,
            @Nullable List<String> dropPacks,
            @Nullable String offsetXString,
            @Nullable String offsetYString,
            @Nullable String angleType
        ) {
            this.internalName = internalName;
            this.entity = entity;
            this.killer = killer;
            this.drops = drops;
            this.fishDrops = fishDrops;
            this.dropPacks = dropPacks;
            this.offsetXString = offsetXString;
            this.offsetYString = offsetYString;
            this.angleType = angleType;
        }

        public static @NonNull HandlerList getHandlerList() {
            return handlers;
        }

        /**
         * 获取怪物ID
         */
        public @NonNull String getInternalName() {
            return internalName;
        }

        /**
         * 获取怪物实体(你可能需要通过编辑它身上的装备来改变掉落装备)
         */
        public @NonNull LivingEntity getEntity() {
            return entity;
        }

        /**
         * 获取怪物击杀者
         */
        public @Nullable LivingEntity getKiller() {
            return killer;
        }

        /**
         * 获取 NeigeItems.Drop.
         */
        public @Nullable List<String> getDrops() {
            return drops;
        }

        /**
         * 设置 NeigeItems.Drop.
         *
         * @param drops NeigeItems.Drop.
         */
        public void setDrops(
            @Nullable List<String> drops
        ) {
            this.drops = drops;
        }

        /**
         * 获取 NeigeItems.FishDrop.
         */
        public @Nullable List<String> getFishDrops() {
            return fishDrops;
        }

        /**
         * 设置 NeigeItems.FishDrop.
         *
         * @param fishDrops NeigeItems.FishDrop.
         */
        public void setFishDrops(
            @Nullable List<String> fishDrops
        ) {
            this.fishDrops = fishDrops;
        }

        /**
         * 获取 NeigeItems.DropPacks.
         */
        public @Nullable List<String> getDropPacks() {
            return dropPacks;
        }

        /**
         * 设置 NeigeItems.DropPacks.
         *
         * @param dropPacks NeigeItems.DropPacks.
         */
        public void setDropPacks(
            @Nullable List<String> dropPacks
        ) {
            this.dropPacks = dropPacks;
        }

        /**
         * 获取 NeigeItems.FancyDrop.offset.x(可能在后续被物品包配置覆盖)
         */
        public @Nullable String getOffsetXString() {
            return offsetXString;
        }

        /**
         * 设置 NeigeItems.FancyDrop.offset.x.
         *
         * @param offsetXString NeigeItems.FancyDrop.offset.x.
         */
        public void setOffsetXString(
            @Nullable String offsetXString
        ) {
            this.offsetXString = offsetXString;
        }

        /**
         * 获取 NeigeItems.FancyDrop.offset.y(可能在后续被物品包配置覆盖)
         */
        public @Nullable String getOffsetYString() {
            return offsetYString;
        }

        /**
         * 设置 NeigeItems.FancyDrop.offset.y.
         *
         * @param offsetYString NeigeItems.FancyDrop.offset.y.
         */
        public void setOffsetYString(
            @Nullable String offsetYString
        ) {
            this.offsetYString = offsetYString;
        }

        /**
         * 获取 NeigeItems.FancyDrop.offset.angle.type(可能在后续被物品包配置覆盖)
         */
        public @Nullable String getAngleType() {
            return angleType;
        }

        /**
         * 设置 NeigeItems.FancyDrop.offset.angle.type.
         *
         * @param angleType NeigeItems.FancyDrop.offset.angle.type.
         */
        public void setAngleType(
            @Nullable String angleType
        ) {
            this.angleType = angleType;
        }

        @Override
        public @NonNull HandlerList getHandlers() {
            return handlers;
        }
    }

    /**
     * MM怪物掉落NI装备事件, MM怪物死亡时异步触发.
     * 生成物品后, 准备掉落前触发.
     *
     * @property internalName 怪物ID
     * @property entity 怪物实体
     * @property player 怪物击杀者
     * @property dropItems 待掉落物品
     * @property fishDropItems 拟渔获掉落物品(不存在击杀者时并入dropItems)
     * @property offsetXString 多彩掉落横向偏移
     * @property offsetYString 多彩掉落纵向偏移
     * @property angleType 多彩掉落喷射模式
     */
    public static class Drop extends CancellableEvent {
        private static final HandlerList handlers = new HandlerList();
        private final @NonNull String internalName;
        private final @NonNull LivingEntity entity;
        private final @Nullable Player player;
        private @NonNull List<ItemStack> dropItems;
        private @Nullable List<ItemStack> fishDropItems;
        private @Nullable String offsetXString;
        private @Nullable String offsetYString;
        private @Nullable String angleType;

        /**
         * @param internalName  怪物ID
         * @param entity        怪物实体
         * @param player        怪物击杀者
         * @param dropItems     待掉落物品
         * @param fishDropItems 拟渔获掉落物品(不存在击杀者时并入dropItems)
         * @param offsetXString 多彩掉落横向偏移
         * @param offsetYString 多彩掉落纵向偏移
         * @param angleType     多彩掉落喷射模式
         */
        public Drop(
            @NonNull String internalName,
            @NonNull LivingEntity entity,
            @Nullable Player player,
            @NonNull List<ItemStack> dropItems,
            @Nullable List<ItemStack> fishDropItems,
            @Nullable String offsetXString,
            @Nullable String offsetYString,
            @Nullable String angleType
        ) {
            this.internalName = internalName;
            this.entity = entity;
            this.player = player;
            this.dropItems = dropItems;
            this.fishDropItems = fishDropItems;
            this.offsetXString = offsetXString;
            this.offsetYString = offsetYString;
            this.angleType = angleType;
        }

        public static @NonNull HandlerList getHandlerList() {
            return handlers;
        }

        /**
         * 获取怪物ID
         */
        public @NonNull String getInternalName() {
            return internalName;
        }

        /**
         * 获取怪物实体
         */
        public @NonNull LivingEntity getEntity() {
            return entity;
        }

        /**
         * 获取怪物击杀者
         */
        public @Nullable Player getPlayer() {
            return player;
        }

        /**
         * 获取待掉落物品
         */
        public @NonNull List<ItemStack> getDropItems() {
            return dropItems;
        }

        /**
         * 设置待掉落物品
         *
         * @param dropItems 待掉落物品
         */
        public void setDropItems(
            @NonNull List<ItemStack> dropItems
        ) {
            this.dropItems = dropItems;
        }

        /**
         * 获取拟渔获掉落物品(不存在击杀者时并入dropItems)
         */
        public @Nullable List<ItemStack> getFishDropItems() {
            return fishDropItems;
        }

        /**
         * 设置拟渔获掉落物品
         *
         * @param fishDropItems 拟渔获掉落物品
         */
        public void setFishDropItems(
            @Nullable List<ItemStack> fishDropItems
        ) {
            this.fishDropItems = fishDropItems;
        }

        /**
         * 获取多彩掉落横向偏移
         */
        public @Nullable String getOffsetXString() {
            return offsetXString;
        }

        /**
         * 设置多彩掉落横向偏移
         *
         * @param offsetXString 多彩掉落横向偏移
         */
        public void setOffsetXString(
            @Nullable String offsetXString
        ) {
            this.offsetXString = offsetXString;
        }

        /**
         * 获取多彩掉落纵向偏移
         */
        public @Nullable String getOffsetYString() {
            return offsetYString;
        }

        /**
         * 设置多彩掉落纵向偏移
         *
         * @param offsetYString 多彩掉落纵向偏移
         */
        public void setOffsetYString(
            @Nullable String offsetYString
        ) {
            this.offsetYString = offsetYString;
        }

        /**
         * 获取多彩掉落喷射模式
         */
        public @Nullable String getAngleType() {
            return angleType;
        }

        /**
         * 设置多彩掉落喷射模式
         *
         * @param angleType 多彩掉落喷射模式
         */
        public void setAngleType(
            @Nullable String angleType
        ) {
            this.angleType = angleType;
        }

        @Override
        public @NonNull HandlerList getHandlers() {
            return handlers;
        }
    }
}