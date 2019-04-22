package com.avarioncraft.AvarionCombat.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import com.avarioncraft.AvarionCombat.skills.Skill;

import lombok.Getter;

public class PassiveSkillTriggerEvent extends PlayerEvent implements Cancellable{
	
	private static final HandlerList handlers = new HandlerList();
	
	public PassiveSkillTriggerEvent(Player who, Skill skill) {
		super(who);
		this.skill = skill;
	}
	
	@Getter
    private final Skill skill;
	
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
