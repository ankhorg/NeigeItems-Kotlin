package pers.neige.neigeitems.event;

import lombok.NonNull;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.item.ItemGenerator;

import java.util.Map;

/**
 * 物品更新事件
 */
public final class ItemUpdateEvent {
    /**
     * 物品更新事件(生成新物品前触发).
     *
     * @property player 持有物品的玩家
     * @property oldItem 待更新物品
     * @property data 旧物品内部的指向数据
     * @property item 根据旧物品的物品ID获得的NI物品生成器
     */
    public static class PreGenerate extends CancellableEvent {
        private static final HandlerList handlers = new HandlerList();
        private final @Nullable OfflinePlayer player;
        private final @NonNull ItemStack oldItem;
        private final @NonNull Map<String, String> data;
        private final @NonNull ItemGenerator item;

        /**
         * @param player  持有物品的玩家
         * @param oldItem 待更新物品
         * @param data    旧物品内部的指向数据
         * @param item    根据旧物品的物品ID获得的NI物品生成器
         */
        public PreGenerate(
                @Nullable OfflinePlayer player,
                @NonNull ItemStack oldItem,
                @NonNull Map<String, String> data,
                @NonNull ItemGenerator item
        ) {
            this.player = player;
            this.oldItem = oldItem;
            this.data = data;
            this.item = item;
        }

        public static @NonNull HandlerList getHandlerList() {
            return handlers;
        }

        /**
         * 获取持有物品的玩家
         */
        public @Nullable OfflinePlayer getPlayer() {
            return player;
        }

        /**
         * 获取待更新物品
         */
        public @NonNull ItemStack getOldItem() {
            return oldItem;
        }

        /**
         * 获取旧物品内部的指向数据
         */
        public @NonNull Map<String, String> getData() {
            return data;
        }

        /**
         * 获取根据旧物品的物品ID获得的NI物品生成器
         */
        public @NonNull ItemGenerator getItem() {
            return item;
        }

        @Override
        public @NonNull HandlerList getHandlers() {
            return handlers;
        }
    }

    /**
     * 物品更新事件(生成新物品后触发).
     *
     * @property player 持有物品的玩家
     * @property oldItem 待更新物品
     * @property newItem 待覆盖物品
     */
    public static class PostGenerate extends CancellableEvent {
        private static final HandlerList handlers = new HandlerList();
        private final @Nullable OfflinePlayer player;
        private final @NonNull ItemStack oldItem;
        private final @NonNull ItemStack newItem;

        /**
         * @param player  持有物品的玩家
         * @param oldItem 待更新物品
         * @param newItem 待覆盖物品
         */
        public PostGenerate(
                @Nullable OfflinePlayer player,
                @NonNull ItemStack oldItem,
                @NonNull ItemStack newItem
        ) {
            this.player = player;
            this.oldItem = oldItem;
            this.newItem = newItem;
        }

        public static @NonNull HandlerList getHandlerList() {
            return handlers;
        }

        /**
         * 获取持有物品的玩家
         */
        public @Nullable OfflinePlayer getPlayer() {
            return player;
        }

        /**
         * 获取待更新物品
         */
        public @NonNull ItemStack getOldItem() {
            return oldItem;
        }

        /**
         * 获取待覆盖物品
         */
        public @NonNull ItemStack getNewItem() {
            return newItem;
        }

        @Override
        public @NonNull HandlerList getHandlers() {
            return handlers;
        }
    }
}