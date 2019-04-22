package com.avarioncraft.AvarionCombat.skills;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.bukkit.entity.Player;

import com.avarioncraft.AvarionCombat.core.AvarionCombat;
import com.google.common.collect.Maps;

import net.crytec.shaded.org.apache.lang3.reflect.ConstructorUtils;

public class SkillHandler {
	private HashMap<String, Class<? extends Skill>> skills = Maps.newHashMap();

	public void register(String id, Class<? extends Skill> skill) {
		skills.put(id, skill);
	}

	public Skill initPlayer(String id, Player player) {
		try {
			if (!skills.containsKey(id)) {
				AvarionCombat.getPlugin().getLogger().severe("Skill nicht registriert! [ " + id + " ]");
				return null;
			}
			
			Skill instance = ConstructorUtils.invokeConstructor(skills.get(id), player);
			return instance;

		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}

}