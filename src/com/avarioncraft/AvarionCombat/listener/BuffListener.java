package com.avarioncraft.AvarionCombat.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.avarioncraft.AvarionCombat.buffs.CombatBuff;
import com.avarioncraft.AvarionCombat.buffs.StatBuff;
import com.avarioncraft.AvarionCombat.events.AvarionCombatEvent;
import com.avarioncraft.AvarionCombat.events.StatModifyEvent;

public class BuffListener implements Listener{
	
	@EventHandler
	public void StatBuffListener(StatModifyEvent event) {
		if(StatBuff.statBuffs.get(event.getPlayer()) == null) return;
		for(StatBuff buff : StatBuff.statBuffs.get(event.getPlayer())) {
			buff.onModify(event);
		}
	}
	
	@EventHandler
	public void CombatBuffListener(AvarionCombatEvent event) {
		
		if(event.getDamagePack().isHumanAttacker()) {
			for(CombatBuff buff : CombatBuff.combatBuffs.get(event.getDamagePack().getAttacker().get())) {
				buff.onCombat(event);
			}
		}
		
		if(event.getDamagePack().isHumanDefender()) {
			for(CombatBuff buff : CombatBuff.combatBuffs.get((Player) event.getDamagePack().getDefender())) {
				buff.onCombat(event);
			}
		}
	}
	
}
