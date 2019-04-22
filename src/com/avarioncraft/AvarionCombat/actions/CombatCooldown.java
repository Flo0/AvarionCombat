package com.avarioncraft.AvarionCombat.actions;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.avarioncraft.AvarionCombat.core.AvarionCombat;
import com.google.common.collect.Maps;

public class CombatCooldown {
	
	private static Map<Player, Float> playerCD = Maps.newHashMap();
	
	public static Float of(Player player) {
		return playerCD.get(player);
	}
	
	public static void startCooldownThread() {
		Bukkit.getScheduler().runTaskTimerAsynchronously(AvarionCombat.getPlugin(), ()->{
			reloadCooldonwns();
		}, 5, 1);
	}
	
	private static void reloadCooldonwns() {
		Bukkit.getOnlinePlayers().forEach((player)-> playerCD.put(player, 1F /*FIXME player.getCoo.getCooledAttackStrength(0F)*/));
	}
}