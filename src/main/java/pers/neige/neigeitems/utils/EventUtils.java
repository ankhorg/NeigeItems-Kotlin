package pers.neige.neigeitems.utils;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class EventUtils {
    /**
     * 注册一个事件监听器.
     * eventPriority 取默认值 EventPriority.NORMAL.
     * ignoreCancelled 取默认值 true.
     *
     * @param eventClass 事件类.
     * @param eventExecutor 事件处理器.
     * @param plugin 注册监听器的插件.
     * @return 对应的 Listener 对象.
     */
    @NotNull
    public static <T extends Event> Listener registerListener(
            @NotNull Class<T> eventClass,
            @NotNull Consumer<T> eventExecutor,
            @NotNull Plugin plugin
    ) {
        return registerListener(eventClass, EventPriority.NORMAL, eventExecutor, plugin, true);
    }

    /**
     * 注册一个事件监听器.
     * ignoreCancelled 取默认值 true.
     *
     * @param eventClass 事件类.
     * @param eventPriority 监听优先级.
     * @param eventExecutor 事件处理器.
     * @param plugin 注册监听器的插件.
     * @return 对应的 Listener 对象.
     */
    @NotNull
    public static <T extends Event> Listener registerListener(
            @NotNull Class<T> eventClass,
            @NotNull EventPriority eventPriority,
            @NotNull Consumer<T> eventExecutor,
            @NotNull Plugin plugin
    ) {
        return registerListener(eventClass, eventPriority, eventExecutor, plugin, true);
    }

    /**
     * 注册一个事件监听器.
     *
     * @param eventClass 事件类.
     * @param eventPriority 监听优先级.
     * @param eventExecutor 事件处理器.
     * @param plugin 注册监听器的插件.
     * @param ignoreCancelled 是否忽略已取消事件.
     * @return 对应的 Listener 对象.
     */
    @NotNull
    public static <T extends Event> Listener registerListener(
            @NotNull Class<T> eventClass,
            @NotNull EventPriority eventPriority,
            @NotNull Consumer<T> eventExecutor,
            @NotNull Plugin plugin,
            boolean ignoreCancelled
    ) {
        Listener listener = new Listener(){};
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
            @NotNull Listener listener
    ) {
        HandlerList.unregisterAll(listener);
    }
}
