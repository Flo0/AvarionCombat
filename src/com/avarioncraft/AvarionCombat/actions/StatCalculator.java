package com.avarioncraft.AvarionCombat.actions;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.avarioncraft.AvarionCombat.data.CombatPlayer;
import com.avarioncraft.AvarionCombat.util.StatFetcher;
import com.avarioncraft.AvarionCombat.util.enums.StatRegion;

public class StatCalculator {
	
	public StatCalculator(CombatPlayer stats) {
		this.stats = stats;
	}
	
	public StatCalculator(Player player) {
		this.stats = CombatPlayer.of(player);
	}
	
	private CombatPlayer stats;
	
	public void updateArmor(Player player) {
		
		stats.getRegionContainer(StatRegion.ARMOR).clearStats();
		
		for(ItemStack stack : player.getInventory().getArmorContents()) {
			if (stack == null) continue;
			stats.addRawStats(StatFetcher.getItemStats(stack, player), StatRegion.ARMOR);
		}
		
		stats.updateSum();
		
	}
	
	public void updateMainSlots(Player player) {
		
		ItemStack[] mainSlots = {player.getInventory().getItemInMainHand(), player.getInventory().getItemInOffHand()};
		
		stats.getRegionContainer(StatRegion.ITEM_HELD).clearStats();
		
		for(ItemStack stack : mainSlots) {
			if (stack == null) continue;
			stats.addRawStats(StatFetcher.getItemStats(stack, player), StatRegion.ITEM_HELD);
		}
		
		stats.updateSum();
	}
	
	public void updateRPG(Player player) {
		
		stats.getRegionContainer(StatRegion.RPG_INVENTORY).clearStats();
		
		stats.updateSum();
		
	}
	
	public void updateAll(Player player) {
		this.updateArmor(player);
		this.updateMainSlots(player);
		this.updateRPG(player);
	}
	
}
