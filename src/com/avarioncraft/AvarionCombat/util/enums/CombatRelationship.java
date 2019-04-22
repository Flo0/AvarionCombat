package com.avarioncraft.AvarionCombat.util.enums;

import org.bukkit.ChatColor;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum CombatRelationship {
	
	FRIENDLY("Freundlich", ChatColor.GREEN), NEUTRAL("Neutral", ChatColor.YELLOW), HOSTILE("Feindlich", ChatColor.RED);
	
	@Getter
	private String displayName;
	@Getter
	private ChatColor getColor;
	
}
