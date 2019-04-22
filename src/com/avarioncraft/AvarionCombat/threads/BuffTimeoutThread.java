package com.avarioncraft.AvarionCombat.threads;

import com.avarioncraft.AvarionCombat.buffs.CombatBuff;
import com.avarioncraft.AvarionCombat.buffs.StatBuff;

public class BuffTimeoutThread implements Runnable{

	@Override
	public void run() {
			CombatBuff.combatBuffs.forEach( (cur, buff) -> {
				buff.forEach(b -> {
					if (b.hasTimedOut()) b.timeout();
				});
			});
			
			StatBuff.statBuffs.forEach( (cur, buff) -> {
				buff.forEach(b -> {
					if (b.hasTimedOut()) b.timeout();
				});
			});
	}
}
