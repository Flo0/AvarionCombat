package com.avarioncraft.AvarionCombat.util;

import java.util.Map;

import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.avarioncraft.AvarionCombat.data.RawStats;
import com.avarioncraft.AvarionCombat.util.enums.EnchantmentStat;
import com.avarioncraft.AvarionCombat.util.enums.Stat;
import com.google.common.collect.Maps;

import net.crytec.api.nbt.NBTItem;

public class StatFetcher {
	
	public static RawStats getItemStats(ItemStack item, @Nullable Player player) {
		
		NBTItem nbt = new NBTItem(item);
		Map<Stat, Double> stats = Maps.newHashMap();
		Map<EnchantmentStat, Integer> enchantments = Maps.newHashMap();
		
		for(Stat stat : Stat.values()) {
			if(!nbt.hasKey(stat.toString())) continue;
			stats.put(stat, nbt.getDouble(stat.toString()));
		}
		for(EnchantmentStat ench : EnchantmentStat.values()) {
			if(!nbt.hasKey(ench.toString())) continue;
			enchantments.put(ench, nbt.getInteger(ench.toString()));
		}
		
		return new RawStats(stats, enchantments, player);
		
	}
	
}
