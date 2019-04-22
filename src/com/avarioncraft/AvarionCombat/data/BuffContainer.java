package com.avarioncraft.AvarionCombat.data;

import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;

import com.avarioncraft.AvarionCombat.buffs.Buff;
import com.avarioncraft.AvarionCombat.buffs.CombatBuff;
import com.avarioncraft.AvarionCombat.buffs.StatBuff;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import lombok.Getter;

public class BuffContainer {
	
	public BuffContainer(Player player) {
		playerBuffs.put(player, this);
	}
	
	private static Map<Player, BuffContainer> playerBuffs = Maps.newHashMap();
	
	public static BuffContainer of(Player player) {
		return playerBuffs.get(player);
	}
	
	public void addBuff(Buff buff) {
		if(buff instanceof StatBuff) {
			this.statBuffs.add((StatBuff) buff);
		}else {
			this.combatBuffs.add((CombatBuff) buff);
		}
	}
	
	@Getter
	private Set<StatBuff> statBuffs = Sets.newHashSet();
	@Getter
	private Set<CombatBuff> combatBuffs = Sets.newHashSet();
	
}
