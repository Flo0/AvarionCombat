package com.avarioncraft.AvarionCombat.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.avarioncraft.AvarionCombat.skills.Skill;

import lombok.Getter;

public class SkillCastEvent extends Event implements Cancellable{
	
	private static final HandlerList handlers = new HandlerList();
	
	public SkillCastEvent(Skill skill) {
		this.skill = skill;
	}
	
	@Getter
	private Skill skill;
	
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
