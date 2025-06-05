package pers.neige.neigeitems.event;

import lombok.NonNull;
import org.bukkit.event.HandlerList;

/**
 * MM怪物信息重载完毕事件, /mm reload后一段时间触发.
 */
public final class MobInfoReloadedEvent extends BasicEvent {
    private static final HandlerList handlers = new HandlerList();

    public static @NonNull HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NonNull HandlerList getHandlers() {
        return handlers;
    }
}