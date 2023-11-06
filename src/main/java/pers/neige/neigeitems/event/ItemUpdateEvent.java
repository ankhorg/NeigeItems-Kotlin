package pers.neige.neigeitems.event;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
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
        @Nullable
        private final OfflinePlayer player;
        @NotNull
        private final ItemStack oldItem;
        @Nullable
        private final Map<String, String> data;
        @NotNull
        private final ItemGenerator item;

        /**
         * @param player  持有物品的玩家
         * @param oldItem 待更新物品
         * @param data    旧物品内部的指向数据
         * @param item    根据旧物品的物品ID获得的NI物品生成器
         */
        public PreGenerate(
                @Nullable OfflinePlayer player,
                @NotNull ItemStack oldItem,
                @Nullable Map<String, String> data,
                @NotNull ItemGenerator item
        ) {
            this.player = player;
            this.oldItem = oldItem;
            this.data = data;
            this.item = item;
        }

        @NotNull
        public static HandlerList getHandlerList() {
            return handlers;
        }

        /**
         * 获取持有物品的玩家
         */
        @Nullable
        public OfflinePlayer getPlayer() {
            return player;
        }

        /**
         * 获取待更新物品
         */
        @NotNull
        public ItemStack getOldItem() {
            return oldItem;
        }

        /**
         * 获取旧物品内部的指向数据
         */
        @Nullable
        public Map<String, String> getData() {
            return data;
        }

        /**
         * 获取根据旧物品的物品ID获得的NI物品生成器
         */
        @NotNull
        public ItemGenerator getItem() {
            return item;
        }

        @Override
        @NotNull
        public HandlerList getHandlers() {
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
        @Nullable
        private final OfflinePlayer player;
        @NotNull
        private final ItemStack oldItem;
        @NotNull
        private final ItemStack newItem;

        /**
         * @param player  持有物品的玩家
         * @param oldItem 待更新物品
         * @param newItem 待覆盖物品
         */
        public PostGenerate(
                @Nullable OfflinePlayer player,
                @NotNull ItemStack oldItem,
                @NotNull ItemStack newItem
        ) {
            this.player = player;
            this.oldItem = oldItem;
            this.newItem = newItem;
        }

        @NotNull
        public static HandlerList getHandlerList() {
            return handlers;
        }

        /**
         * 获取持有物品的玩家
         */
        @Nullable
        public OfflinePlayer getPlayer() {
            return player;
        }

        /**
         * 获取待更新物品
         */
        @NotNull
        public ItemStack getOldItem() {
            return oldItem;
        }

        /**
         * 获取待覆盖物品
         */
        @NotNull
        public ItemStack getNewItem() {
            return newItem;
        }

        @Override
        @NotNull
        public HandlerList getHandlers() {
            return handlers;
        }
    }
}