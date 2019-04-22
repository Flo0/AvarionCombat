package com.avarioncraft.AvarionCombat.skills.conditions;

import com.avarioncraft.AvarionCombat.data.CombatPlayer;

public class TimeCondition implements Condition {
	
	@Override
	public boolean validate(CombatPlayer player) {
		return player.getPlayer().getWorld().isDayTime();
	}

	@Override
	public String getFailedMessage() {
		return "Du kannst diesen Skill nur am Tag ausführen.";
	}

	@Override
	public String displayName() {
		return "Zeitabhängig";
	}

}
