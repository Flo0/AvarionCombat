package com.avarioncraft.AvarionCombat.skills.conditions;

import com.avarioncraft.AvarionCombat.data.CombatPlayer;

public class WeatherCondition implements Condition{
	
	public WeatherCondition(boolean trueOnRain) {
		this.isRainValid = trueOnRain;
	}
	
	private final boolean isRainValid;
	
	@Override
	public boolean validate(CombatPlayer combatPlayer) {
		
		if(combatPlayer.getPlayer().getWorld().isThundering() == this.isRainValid) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public String getFailedMessage() {
		return "Dieser Skill kann nur bei bestimmtem Wetter";
	}

	@Override
	public String displayName() {
		return "Wetterabhängig";
	}
	
}
