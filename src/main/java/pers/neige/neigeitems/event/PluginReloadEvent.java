package pers.neige.neigeitems.event;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * 插件重载事件, /ni reload指令触发
 */
public final class PluginReloadEvent {
    /**
     * 插件重载前触发
     *
     * @property type 重载类型
     */
    public static class Pre extends BasicEvent {
        private static final HandlerList handlers = new HandlerList();
        @NotNull
        private final Type type;

        /**
         * type 取默认值 Type.ALL.
         */
        public Pre() {
            this.type = Type.ALL;
        }

        /**
         * @param type 重载类型
         */
        public Pre(@NotNull Type type) {
            this.type = type;
        }

        /**
         * 获取重载类型
         */
        @NotNull
        public Type getType() {
            return type;
        }

        @Override
        @NotNull
        public HandlerList getHandlers() {
            return handlers;
        }

        @NotNull
        public static HandlerList getHandlerList() {
            return handlers;
        }
    }

    /**
     * 插件重载后触发, 不可取消
     */
    public static class Post extends BasicEvent {
        private static final HandlerList handlers = new HandlerList();
        @NotNull
        private final Type type;

        /**
         * type 取默认值 Type.ALL.
         */
        public Post() {
            this.type = Type.ALL;
        }

        /**
         * @param type 重载类型
         */
        public Post(@NotNull Type type) {
            this.type = type;
        }

        /**
         * 获取重载类型
         */
        @NotNull
        public Type getType() {
            return type;
        }

        @Override
        @NotNull
        public HandlerList getHandlers() {
            return handlers;
        }

        @NotNull
        public static HandlerList getHandlerList() {
            return handlers;
        }
    }

    public enum Type {
        ALL,
        CONFIG,
        ITEM,
        SCRIPT,
        PACK,
        ACTION,
        EDITOR,
        EXPANSION,
    }
}