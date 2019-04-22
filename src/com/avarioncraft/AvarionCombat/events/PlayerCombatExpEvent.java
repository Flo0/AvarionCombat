package com.avarioncraft.AvarionCombat.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import lombok.Getter;

public class PlayerCombatExpEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public PlayerCombatExpEvent(Player who, long exp) {
	super(who);
	this.exp = exp;
    }

    @Getter
    private long exp;

    private boolean cancelled = false;

    @Override
    public HandlerList getHandlers() {
	return handlers;
    }

    public static HandlerList getHandlerList() {
	return handlers;
    }

    @Override
    public boolean isCancelled() {
	return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
	this.cancelled = cancelled;
    }

}
