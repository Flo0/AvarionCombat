package com.avarioncraft.AvarionCombat.skills.mechanics;

import org.bukkit.Location;

public interface CastPoint {
	
	public Location getLocation();
	public void onTick();
	public void onCreate();
	public void onRunout();
	public boolean isDone();
	
}
