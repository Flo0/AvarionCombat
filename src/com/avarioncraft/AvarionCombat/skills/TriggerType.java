package com.avarioncraft.AvarionCombat.skills;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum TriggerType {
	
	SWING("Schwingen"),
	SNEAK_CAST("Schleich-Interaktion"),
	SPELL_CAST("Zauberstab-Interaktion"),
	ATTACK_MEELE("Nahkampf-Angriff"),
	ATTACK_RANGED("Fernkampf-Angriff"),
	ATTACK_SKILL("Magischer-Angriff"),
	DEFEND_MEELE("Nahkampf-Verteidigen"),
	DEFEND_RANGED("Fernkampf-Verteidigen"),
	DEFEND_MAGIC("Magie-Verteidigen"),
	PASSIVE("Passiv"),
	BLOCK_BREAK("Block zerstören"),
	DEATH("Tod"),
	CAST_OTHER_SKILL("Während Kanalisieren"),
	ENHANCING("Skill-Verbesserung");
	
	@Getter
	private String displayName;
	
}
