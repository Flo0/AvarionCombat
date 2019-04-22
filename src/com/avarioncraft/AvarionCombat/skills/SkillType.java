package com.avarioncraft.AvarionCombat.skills;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum SkillType {
	
	SKILLSHOT("Skillshot"),
	SINGLE("Einzelner Gegner"),
	TARGET("Direkt"),
	PASSIVE("Passiv"),
	AOE("AOE"),
	OTHER("Kein Typ"),
	DEFENSIVE("Defensive"),
	FIGHTING_STYLE("Kampfstil"),
	ENHANCE("Verbesserung");
	
	@Getter
	private String displayName;
	
}