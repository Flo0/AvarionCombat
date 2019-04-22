package com.avarioncraft.AvarionCombat.skills.conditions;

import com.avarioncraft.AvarionCombat.data.CombatPlayer;

public interface Condition {
	
	public boolean validate(CombatPlayer combatPlayer);
	
	public String getFailedMessage();
	
	public String displayName();
	
}
