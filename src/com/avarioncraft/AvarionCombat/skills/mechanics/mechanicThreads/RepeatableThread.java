package com.avarioncraft.AvarionCombat.skills.mechanics.mechanicThreads;

import org.bukkit.Bukkit;

import com.avarioncraft.AvarionCombat.data.CombatPlayer;
import com.avarioncraft.AvarionCombat.events.PassiveSkillTriggerEvent;
import com.avarioncraft.AvarionCombat.skills.Skill;
import com.avarioncraft.AvarionCombat.skills.TriggerType;

public class RepeatableThread implements Runnable{

	@Override
	public void run() {
		
		CombatPlayer.getAll().forEach(cp->{
			
			Skill skill = cp.getSkillEquip().getSkill(TriggerType.PASSIVE);
			if (skill != null) {
				PassiveSkillTriggerEvent event = new PassiveSkillTriggerEvent(cp.getPlayer(), skill);
				Bukkit.getPluginManager().callEvent(event);
			}
		});
		
	}

}
