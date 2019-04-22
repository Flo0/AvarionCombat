package com.avarioncraft.AvarionCombat.util.enums;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public enum CombatType {
	
	PVP,
	PVE,
	EVP,
	EVE,
	ENV;
	
	public static CombatType typeOf(LivingEntity attacker, LivingEntity defender) {
		if(attacker == null) return ENV;
		if(attacker instanceof Player) {
			return (defender instanceof Player) ? PVP : PVE;
		}else {
			return (defender instanceof Player) ? EVP : EVE;
		}
	}
}
