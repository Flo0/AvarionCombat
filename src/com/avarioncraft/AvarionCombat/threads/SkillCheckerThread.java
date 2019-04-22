package com.avarioncraft.AvarionCombat.threads;

import java.util.Set;

import com.google.common.collect.Sets;

public class SkillCheckerThread implements Runnable{
	
	public static void registerSkill(CheckableSkill skill) {
		SKILLS.add(skill);
	}
	
	public static void unregisterSkill(CheckableSkill skill) {
		SKILLS.remove(skill);
	}
	
	private static final Set<CheckableSkill> SKILLS = Sets.newHashSet();
	
	@Override
	public void run() {
		
		SKILLS.forEach(checkable -> checkable.onCheckup());
		
	}
	
}
