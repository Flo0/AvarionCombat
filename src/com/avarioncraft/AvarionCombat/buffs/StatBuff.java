package com.avarioncraft.AvarionCombat.buffs;

import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;

import com.avarioncraft.AvarionCombat.data.CombatPlayer;
import com.avarioncraft.AvarionCombat.events.StatModifyEvent;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public abstract class StatBuff extends Buff{
	
	//Constructor
	public StatBuff(Player player, long duration) {
		super(player, duration);
		this.startBuff();
	}
	
	//Statics
	public static Map<Player, Set<StatBuff>> statBuffs = Maps.newHashMap();
	
	public abstract void onModify(StatModifyEvent event);
	
	@Override
	public void startBuff() {
		if(!statBuffs.containsKey(super.getPlayer())) {
			statBuffs.put(super.getPlayer(), Sets.newHashSet());
		}
		statBuffs.get(super.getPlayer()).add(this);
		System.out.println(CombatPlayer.of(super.getPlayer()) == null);
		CombatPlayer.of(super.getPlayer()).calculateFull();
	}
	
	@Override
	public void timeout() {
		statBuffs.get(super.getPlayer()).remove(this);
		CombatPlayer.of(super.getPlayer()).calculateFull();
	}
	
}
