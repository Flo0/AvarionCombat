package com.avarioncraft.AvarionCombat.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.avarioncraft.AvarionCombat.util.enums.Stat;
import com.avarioncraft.AvarionCombat.util.enums.StatRegion;

import lombok.Getter;
import lombok.Setter;

public class StatModifyEvent extends Event implements Cancellable{

	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;
	
	public StatModifyEvent(Player player, Stat stat, double value, double current, StatRegion region) {
		this.player = player;
		this.stat = stat;
		this.value = value;
		this.region = region;
		this.currentAmount = current;
	}
	
	@Getter
	private Player player;
	@Getter
	private Stat stat;
	@Getter @Setter
	private double value;
	@Getter
	private StatRegion region;
	private final double currentAmount;
	
	public double getAfterCalc() {
		return currentAmount + value;
	}
	
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