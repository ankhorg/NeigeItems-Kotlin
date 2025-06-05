package pers.neige.neigeitems.utils;

import lombok.NonNull;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.NeigeItems;

import java.util.function.Consumer;

public class ListenerUtils {
    /**
     * 注册一个事件监听器.
     * eventPriority 取默认值 EventPriority.NORMAL.
     * plugin 取默认值 NeigeItems.getInstance().
     * ignoreCancelled 取默认值 true.
     *
     * @param eventClass    事件类.
     * @param eventExecutor 事件处理器.
     * @return 对应的 Listener 对象.
     */
    public static @NonNull <T extends Event> Listener registerListener(
            @NonNull Class<T> eventClass,
            @NonNull Consumer<T> eventExecutor
    ) {
        return registerListener(eventClass, EventPriority.NORMAL, NeigeItems.getInstance(), true, eventExecutor);
    }

    /**
     * 注册一个事件监听器.
     * eventPriority 取默认值 EventPriority.NORMAL.
     * ignoreCancelled 取默认值 true.
     *
     * @param eventClass    事件类.
     * @param eventExecutor 事件处理器.
     * @param plugin        注册监听器的插件.
     * @return 对应的 Listener 对象.
     */
    public static @NonNull <T extends Event> Listener registerListener(
            @NonNull Class<T> eventClass,
            @NonNull Plugin plugin,
            @NonNull Consumer<T> eventExecutor
    ) {
        return registerListener(eventClass, EventPriority.NORMAL, plugin, true, eventExecutor);
    }

    /**
     * 注册一个事件监听器.
     * plugin 取默认值 NeigeItems.getInstance().
     * ignoreCancelled 取默认值 true.
     *
     * @param eventClass    事件类.
     * @param eventPriority 监听优先级.
     * @param eventExecutor 事件处理器.
     * @return 对应的 Listener 对象.
     */
    public static @NonNull <T extends Event> Listener registerListener(
            @NonNull Class<T> eventClass,
            @NonNull EventPriority eventPriority,
            @NonNull Consumer<T> eventExecutor
    ) {
        return registerListener(eventClass, eventPriority, NeigeItems.getInstance(), true, eventExecutor);
    }

    /**
     * 注册一个事件监听器.
     * ignoreCancelled 取默认值 true.
     *
     * @param eventClass    事件类.
     * @param eventPriority 监听优先级.
     * @param eventExecutor 事件处理器.
     * @param plugin        注册监听器的插件.
     * @return 对应的 Listener 对象.
     */
    public static @NonNull <T extends Event> Listener registerListener(
            @NonNull Class<T> eventClass,
            @NonNull EventPriority eventPriority,
            @NonNull Plugin plugin,
            @NonNull Consumer<T> eventExecutor
    ) {
        return registerListener(eventClass, eventPriority, plugin, true, eventExecutor);
    }

    /**
     * 注册一个事件监听器.
     * plugin 取默认值 NeigeItems.getInstance().
     *
     * @param eventClass      事件类.
     * @param eventPriority   监听优先级.
     * @param eventExecutor   事件处理器.
     * @param ignoreCancelled 是否忽略已取消事件.
     * @return 对应的 Listener 对象.
     */
    public static @NonNull <T extends Event> Listener registerListener(
            @NonNull Class<T> eventClass,
            @NonNull EventPriority eventPriority,
            boolean ignoreCancelled,
            @NonNull Consumer<T> eventExecutor
    ) {
        return registerListener(eventClass, eventPriority, NeigeItems.getInstance(), ignoreCancelled, eventExecutor);
    }

    /**
     * 注册一个事件监听器.
     *
     * @param eventClass      事件类.
     * @param eventPriority   监听优先级.
     * @param eventExecutor   事件处理器.
     * @param plugin          注册监听器的插件.
     * @param ignoreCancelled 是否忽略已取消事件.
     * @return 对应的 Listener 对象.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Event> @NonNull Listener registerListener(
            @NonNull Class<T> eventClass,
            @NonNull EventPriority eventPriority,
            @NonNull Plugin plugin,
            boolean ignoreCancelled,
            @NonNull Consumer<T> eventExecutor
    ) {
        val listener = new Listener() {
        };
        Bukkit.getPluginManager().registerEvent(
                eventClass,
                listener,
                eventPriority,
                (listener1, event) -> {
                    if (eventClass.isAssignableFrom(event.getClass())) {
                        eventExecutor.accept((T) event);
                    }
                },
                plugin,
                ignoreCancelled
        );
        return listener;
    }

    /**
     * 卸载事件监听器.
     *
     * @param listener 待卸载监听器.
     */
    public static void unregisterListener(
            @Nullable Listener listener
    ) {
        if (listener != null) {
            HandlerList.unregisterAll(listener);
        }
    }
}
