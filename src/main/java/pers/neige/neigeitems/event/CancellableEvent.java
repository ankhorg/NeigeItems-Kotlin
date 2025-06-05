package pers.neige.neigeitems.event;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class CancellableEvent extends BasicEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    public static @NonNull HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean call() {
        Bukkit.getPluginManager().callEvent(this);
        return !cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public @NonNull HandlerList getHandlers() {
        return handlers;
    }
}
