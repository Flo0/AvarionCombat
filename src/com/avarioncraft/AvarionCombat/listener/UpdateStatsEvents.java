package com.avarioncraft.AvarionCombat.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedMainHandEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import com.avarioncraft.AvarionCombat.data.CombatPlayer;
import com.avarioncraft.AvarionCombat.util.StatFetcher;
import com.avarioncraft.AvarionCombat.util.enums.StatRegion;
import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;

public class UpdateStatsEvents implements Listener{
	
	@EventHandler
	public void changeArmor(PlayerArmorChangeEvent event) {
		CombatPlayer.of(event.getPlayer()).calculateArmor();
	}
	
	@EventHandler
	public void changeEvent(PlayerItemHeldEvent event) {
		CombatPlayer stats = CombatPlayer.of(event.getPlayer());
		
		ItemStack item = event.getPlayer().getInventory().getItem(event.getNewSlot());
		ItemStack itemOff = event.getPlayer().getInventory().getItemInOffHand();
		
		stats.getRegionContainer(StatRegion.ITEM_HELD).clearStats();
		
		if(item != null) {
			
			stats.addRawStats(StatFetcher.getItemStats(item, event.getPlayer()), StatRegion.ITEM_HELD);
			
		}
		
		if(itemOff != null) {
			if(itemOff.getType() != Material.AIR) {
				
				stats.addRawStats(StatFetcher.getItemStats(itemOff, event.getPlayer()), StatRegion.ITEM_HELD);
				
			}
		}
		
		stats.updateSum();
		
	}
	
	@EventHandler
	public void changeEvent(PlayerChangedMainHandEvent event) {
		CombatPlayer.of(event.getPlayer()).calculateHands();
	}
	
	@EventHandler
	public void changeDeath(PlayerRespawnEvent event) {
		CombatPlayer.of(event.getPlayer()).calculateFull();
	}

}
