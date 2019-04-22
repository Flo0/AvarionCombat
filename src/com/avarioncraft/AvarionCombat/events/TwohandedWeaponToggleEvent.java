package com.avarioncraft.AvarionCombat.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class TwohandedWeaponToggleEvent extends PlayerSwapHandItemsEvent{
	
	public TwohandedWeaponToggleEvent(PlayerSwapHandItemsEvent event) {
		super(event.getPlayer(), event.getMainHandItem(), event.getOffHandItem());
	}
	
	public TwohandedWeaponToggleEvent(Player player, ItemStack mainHandItem, ItemStack offHandItem) {
		super(player, mainHandItem, offHandItem);
	}

	private static final HandlerList handlers = new HandlerList();
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
