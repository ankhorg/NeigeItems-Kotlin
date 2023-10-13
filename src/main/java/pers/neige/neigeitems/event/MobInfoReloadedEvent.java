package pers.neige.neigeitems.event;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * MM怪物信息重载完毕事件, /mm reload后一段时间触发.
 */
public final class MobInfoReloadedEvent extends BasicEvent {
    private static final HandlerList handlers = new HandlerList();

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