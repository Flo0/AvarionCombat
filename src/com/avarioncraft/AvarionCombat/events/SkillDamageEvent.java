package com.avarioncraft.AvarionCombat.events;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.avarioncraft.AvarionCombat.skills.Skill;

import lombok.Getter;

public class SkillDamageEvent extends EntityDamageByEntityEvent{
	
	public SkillDamageEvent(Entity attacker, Entity defender, DamageCause cause, double damage, Skill skill) {
		super(attacker, defender, cause, damage);
		this.skill = skill;
		
	}
	
	@Getter
	private final Skill skill;
	
}
