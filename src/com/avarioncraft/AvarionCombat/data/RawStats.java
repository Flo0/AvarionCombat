package com.avarioncraft.AvarionCombat.data;

import java.util.Map;

import javax.annotation.Nullable;

import org.bukkit.entity.Player;

import com.avarioncraft.AvarionCombat.util.enums.EnchantmentStat;
import com.avarioncraft.AvarionCombat.util.enums.Stat;

import lombok.Getter;

public class RawStats {
	
	public RawStats(Map<Stat, Double> stats, Map<EnchantmentStat, Integer> enchantments, @Nullable Player player) {
		this.stats = stats;
		this.enchantments = enchantments;	
		this.player = player;
	}
	
	//Variablen
	@Getter
	private final Map<Stat, Double> stats;
	@Getter
	private final Map<EnchantmentStat, Integer> enchantments;
	@Getter
	private final Player player;
	
	public void add(RawStats newStats) {
		newStats.stats.forEach((st,val)-> this.stats.merge(st, val, Double::sum));
		newStats.enchantments.forEach((st,val)-> this.enchantments.merge(st, val, Integer::sum));
	}
	
}
