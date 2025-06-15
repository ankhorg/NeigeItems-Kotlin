package pers.neige.neigeitems.event;

import lombok.NonNull;
import org.bukkit.event.HandlerList;

import java.util.HashMap;
import java.util.Map;

/**
 * 插件重载事件, /ni reload指令触发
 */
public final class PluginReloadEvent {
    public enum Type {
        ALL,
        CONFIG,
        ITEM,
        SCRIPT,
        PACK,
        ACTION,
        EDITOR,
        EXPANSION;

        public static Map<String, Type> lowercaseNameToType = new HashMap<>();

        static {
            for (Type type : values()) {
                lowercaseNameToType.put(type.name().toLowerCase(), type);
            }
        }
    }

    /**
     * 插件重载前触发
     *
     * @property type 重载类型
     */
    public static class Pre extends BasicEvent {
        private static final HandlerList handlers = new HandlerList();
        private final @NonNull Type type;

        /**
         * type 取默认值 Type.ALL.
         */
        public Pre() {
            this.type = Type.ALL;
        }

        /**
         * @param type 重载类型
         */
        public Pre(@NonNull Type type) {
            this.type = type;
        }

        public static @NonNull HandlerList getHandlerList() {
            return handlers;
        }

        /**
         * 获取重载类型
         */
        public @NonNull Type getType() {
            return type;
        }

        @Override
        public @NonNull HandlerList getHandlers() {
            return handlers;
        }
    }

    /**
     * 插件重载后触发, 不可取消
     */
    public static class Post extends BasicEvent {
        private static final HandlerList handlers = new HandlerList();
        private final @NonNull Type type;

        /**
         * type 取默认值 Type.ALL.
         */
        public Post() {
            this.type = Type.ALL;
        }

        /**
         * @param type 重载类型
         */
        public Post(@NonNull Type type) {
            this.type = type;
        }

        public static @NonNull HandlerList getHandlerList() {
            return handlers;
        }

        /**
         * 获取重载类型
         */
        public @NonNull Type getType() {
            return type;
        }

        @Override
        public @NonNull HandlerList getHandlers() {
            return handlers;
        }
    }
}