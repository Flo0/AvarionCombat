package com.avarioncraft.AvarionCombat.skills.conditions;

import java.util.Set;

import org.bukkit.block.Biome;

import com.avarioncraft.AvarionCombat.data.CombatPlayer;

public class BiomeCondition implements Condition{
	
	public BiomeCondition(Set<Biome> validBiomes) {
		this.validBiomes = validBiomes;
	}
	
	private final Set<Biome> validBiomes;
	
	@Override
	public boolean validate(CombatPlayer combatPlayer) {
		if(this.validBiomes.contains(combatPlayer.getPlayer().getLocation().getBlock().getBiome())) {
			return true;
		}
		return false;
	}

	@Override
	public String getFailedMessage() {
		return "Du befindest dich nicht im richtigen Biom.";
	}

	@Override
	public String displayName() {
		return "Biomabhängig";
	}

}
