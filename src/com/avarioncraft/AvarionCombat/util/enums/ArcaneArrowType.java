package com.avarioncraft.AvarionCombat.util.enums;

import org.bukkit.Particle;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ArcaneArrowType{
	
	ASTRAL("Astral", Particle.CRIT_MAGIC), NATURE("Natur", Particle.VILLAGER_HAPPY), INFERNO("Inferno", Particle.FLAME);
	
	@Getter
	private String displayName;
	@Getter
	public Particle particle;
	
}