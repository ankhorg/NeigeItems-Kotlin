package pers.neige.neigeitems.event;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * PlaceholderAPI 重载完毕后触发的事件.
 */
public final class PapiReloadEvent extends BasicEvent {
    private static final HandlerList handlers = new HandlerList();

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }
}