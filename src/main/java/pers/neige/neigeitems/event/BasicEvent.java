package pers.neige.neigeitems.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class BasicEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public BasicEvent() {
        super(!Bukkit.isPrimaryThread());
    }

    public boolean call() {
        Bukkit.getPluginManager().callEvent(this);
        return true;
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
