package com.avarioncraft.AvarionCombat.buffs;

import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public abstract class CombatBuff extends Buff {

	public CombatBuff(Player player, long duration) {
		super(player, duration);
		
		this.startBuff();
	}
	
	// Statics
	public static Map<Player, Set<CombatBuff>> combatBuffs = Maps.newHashMap();
	
	public abstract void onCombat(Event event);
	
	@Override
	public void startBuff() {
		if (!combatBuffs.containsKey(super.getPlayer())) {
			combatBuffs.put(super.getPlayer(), Sets.newHashSet());
		}
		combatBuffs.get(super.getPlayer()).add(this);
	}
	
	@Override
	public void timeout() {
		combatBuffs.get(super.getPlayer()).remove(this);
	}

}
