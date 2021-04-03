package me.imbuzz.dev.api;

import lombok.Getter;
import me.imbuzz.dev.objects.GeneratorType;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class GeneratorPlaceEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    @Getter private GeneratorType generator;

    public GeneratorPlaceEvent(GeneratorType g){
        generator = g;
    }


    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
