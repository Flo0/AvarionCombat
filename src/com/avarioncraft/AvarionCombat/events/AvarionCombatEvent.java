package com.avarioncraft.AvarionCombat.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.avarioncraft.AvarionCombat.data.DamagePackage;

import lombok.Getter;

public class AvarionCombatEvent extends Event implements Cancellable{
	
	private static final HandlerList handlers = new HandlerList();

    public AvarionCombatEvent(DamagePackage pack) {
    	this.damagePack = pack;
    }

    @Getter
    private final DamagePackage damagePack;

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
