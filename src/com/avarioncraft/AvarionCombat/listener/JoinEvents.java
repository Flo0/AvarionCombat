package com.avarioncraft.AvarionCombat.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.avarioncraft.AvarionCombat.buffs.CombatBuff;
import com.avarioncraft.AvarionCombat.buffs.StatBuff;
import com.avarioncraft.AvarionCombat.buffs.statBuffs.StartBuff;
import com.avarioncraft.AvarionCombat.core.AvarionCombat;
import com.avarioncraft.AvarionCombat.data.CombatPlayer;
import com.google.common.collect.Sets;

public class JoinEvents implements Listener {
	
	@EventHandler(priority = EventPriority.HIGH)
	public void joinInit(PlayerJoinEvent event) {
		
		if (!CombatPlayer.init(event.getPlayer())) {
			AvarionCombat.getPlugin().getLogger().severe("Failed to initialize player combat stats!");
		}
		
		CombatPlayer cp = CombatPlayer.of(event.getPlayer());
		
		cp.calculateFull();
		
		StatBuff.statBuffs.put(event.getPlayer(), Sets.newHashSet());
		CombatBuff.combatBuffs.put(event.getPlayer(), Sets.newHashSet());
		
		new StartBuff(event.getPlayer());
		
		AvarionCombat.getPlugin().getStorage().loadPlayer(cp);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent event) {
		AvarionCombat.getPlugin().getStorage().savePlayer(CombatPlayer.of(event.getPlayer()));
		CombatPlayer.logout(event.getPlayer());
	}
}