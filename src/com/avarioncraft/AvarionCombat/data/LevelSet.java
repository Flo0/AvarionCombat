package com.avarioncraft.AvarionCombat.data;

import org.bukkit.Bukkit;

import com.avarioncraft.AvarionCombat.events.PlayerCombatExpEvent;
import com.avarioncraft.AvarionCombat.events.PlayerCombatLevelupEvent;

import lombok.Getter;
import net.crytec.shaded.parsii.eval.Parser;
import net.crytec.shaded.parsii.eval.Scope;
import net.crytec.shaded.parsii.eval.Variable;
import net.crytec.shaded.parsii.tokenizer.ParseException;

public class LevelSet {
	
	private static final int MAX_LEVEL = 100;
	private static final String LEVEL_FUNCTION = "((lvl * lvl) * 2) + 50";
	private static final String EXP_FUNCTION = "sqrt((exp - 50) / 2)";
	
	static {
		long value = -1;
		Scope scope = new Scope();
		Variable lvl = scope.getVariable("lvl");
		lvl.setValue(MAX_LEVEL);
		try {
			value = (long) Parser.parse(LEVEL_FUNCTION, scope).evaluate();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		MAX_EXP = value;
	}
	
	private static final long MAX_EXP;
	
	public LevelSet(CombatPlayer cPlayer) {
		this.cPlayer = cPlayer;
		this.level = 0;
		this.exp = 0;
		this.skillPoints = 0;
	}
	
	private final CombatPlayer cPlayer;
	@Getter
	private int level;
	@Getter
	private long exp;
	private long expNextLevel;
	@Getter
	private int skillPoints;
	
	public void serializeExp(long exp) {
		this.exp = exp;
		this.recalcLevel();
		this.addNextLvlExp();
	}
	
	private void recalcLevel() {
		int lvl = 0;
		Scope scope = new Scope();
		Variable exp = scope.getVariable("exp");
		exp.setValue(this.exp);
		try {
			lvl = (int) Math.round(Parser.parse(EXP_FUNCTION, scope).evaluate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.level = lvl;
	}
	
	public void addSkillPoints(int amount) {
		this.skillPoints += amount;
	}
	
	public void removeSkillPoints(int amount) {
		this.skillPoints -= amount;
	}
	
	private void checkForLevelUp() {
		if(this.exp >= this.expNextLevel) {
			this.lvlUp();
		}
	}
	
	public void addExp(long amount) {
		if(this.exp == MAX_EXP) return;
		
		PlayerCombatExpEvent event = new PlayerCombatExpEvent(this.cPlayer.getPlayer(), amount);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) return;
		
		long extra = event.getExp();
		
		if(this.exp + extra >= MAX_EXP) {
			this.exp = MAX_EXP;
			this.checkForLevelUp();
			return;
		}
		
		this.exp += extra;
		this.checkForLevelUp();
	}
	
	public void addLevel(int amount) {
		for(int p = 1; p <= amount; p++) {
			this.lvlUp();
		}
	}
	
	public void lvlUp() {
		if(this.level == MAX_LEVEL) return;
		
		PlayerCombatLevelupEvent event = new PlayerCombatLevelupEvent(this.cPlayer.getPlayer(), this.level + 1);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled()) return;
		
		if(this.level + 1 > MAX_LEVEL) {
			this.level = MAX_LEVEL;
		}else {
			this.level++;
		}
		
		this.skillPoints++;
		
		this.addNextLvlExp();
	}
	
	private void addNextLvlExp() {
		long value = 0;
		Scope scope = new Scope();
		Variable lvl = scope.getVariable("lvl");
		lvl.setValue(this.level);
		try {
			value = (long) Parser.parse(LEVEL_FUNCTION, scope).evaluate();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		this.expNextLevel = value;
	}
}
