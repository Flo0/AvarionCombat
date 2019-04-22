package com.avarioncraft.AvarionCombat.util.enums;

import org.bukkit.ChatColor;

import lombok.Getter;

public enum CombatDirection {
	
	IN("Eingehend", ChatColor.RED),
	OUT("Ausgehend", ChatColor.GREEN);
	
	@Getter
	private String display;
	@Getter
	private ChatColor color;
	
	private CombatDirection(String display, ChatColor color) {
		this.display = display;
		this.color = color;
	}

}
