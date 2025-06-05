package pers.neige.neigeitems.event;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BasicEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public BasicEvent() {
        super(!Bukkit.isPrimaryThread());
    }

    public static @NonNull HandlerList getHandlerList() {
        return handlers;
    }

    public boolean call() {
        Bukkit.getPluginManager().callEvent(this);
        return true;
    }

    @Override
    public @NonNull HandlerList getHandlers() {
        return handlers;
    }
}
