package com.avarioncraft.AvarionCombat.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.avarioncraft.AvarionCombat.util.enums.EnchantmentStat;
import com.avarioncraft.AvarionCombat.util.enums.StatRegion;

import lombok.Getter;
import lombok.Setter;

public class EnchantmentModifyEvent extends Event implements Cancellable{

	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;
	
	public EnchantmentModifyEvent(Player player, EnchantmentStat stat, int value, StatRegion region) {
		this.player = player;
		this.stat = stat;
		this.value = value;
		this.region = region;
	}
	
	@Getter
	private Player player;
	@Getter
	private EnchantmentStat stat;
	@Getter @Setter
	private int value;
	@Getter
	private StatRegion region;
	
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