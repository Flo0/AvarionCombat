package com.avarioncraft.AvarionCombat.data;

import java.util.Map;

import com.avarioncraft.AvarionCombat.util.enums.EnchantmentStat;
import com.avarioncraft.AvarionCombat.util.enums.Stat;
import com.google.common.collect.Maps;

import lombok.Getter;

public class StatContainer {
	
	public StatContainer() {
		for(Stat stat : Stat.values()) {
			stats.put(stat, 0.0D);
		}
		for(EnchantmentStat ench : EnchantmentStat.values()) {
			enchantments.put(ench, 0);
		}
	}
	
	@Getter
	private Map<Stat, Double> stats = Maps.newHashMap();
	@Getter
	private Map<EnchantmentStat, Integer> enchantments = Maps.newHashMap();
	
	public void clearStats() {
		for(Stat stat : Stat.values()) {
			stats.put(stat, 0.0D);
		}
		for(EnchantmentStat ench : EnchantmentStat.values()) {
			enchantments.put(ench, 0);
		}
	}
	
}