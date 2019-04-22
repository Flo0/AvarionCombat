package com.avarioncraft.AvarionCombat.buffs;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.Setter;

public abstract class Buff {
	
	public Buff(Player player, long duration) {
		this.player = player;
		this. occurrence = System.currentTimeMillis();
		if(duration > 0) this.duration = duration;
	}
	
	@Getter
	private final Player player;
	@Getter
	private final long occurrence;
	@Getter @Setter
	private long duration = -1L;
	@Getter
	private Material icon;
	@Getter @Setter
	private String displayName;
	@Getter
	private List<String> description = Lists.newArrayList();
	
	public void addDescLine(String line) {
		this.description.add(line);
	}
	
	public void setIconMaterial(Material icon) {
		this.icon = icon;
	}
	
	public boolean hasTimedOut() {
		
		if(duration < 0) return false;
		
		if(System.currentTimeMillis() > occurrence + duration) {
			return true;
		}
		return false;
	}
	
	public abstract void startBuff();
	public abstract void timeout();
	
}
