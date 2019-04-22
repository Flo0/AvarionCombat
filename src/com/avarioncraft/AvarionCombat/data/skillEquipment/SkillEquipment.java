package com.avarioncraft.AvarionCombat.data.skillEquipment;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.avarioncraft.AvarionCombat.data.CombatPlayer;
import com.avarioncraft.AvarionCombat.skills.Skill;
import com.avarioncraft.AvarionCombat.skills.SkillRegistration;
import com.avarioncraft.AvarionCombat.skills.TriggerType;
import com.avarioncraft.AvarionCombat.util.enums.SkillTreeType;
import com.google.common.collect.Maps;

import lombok.Getter;

public class SkillEquipment {
	
	public SkillEquipment(CombatPlayer cPlayer) {
		this.cPlayer = cPlayer;
		this.skillSet = Maps.newHashMap();
		this.playerSkills = Maps.newHashMap();
		
		for(TriggerType tt : TriggerType.values()) {
			
			this.playerSkills.put(tt, null);
			
		}
		
	}
	
	public void initSkills() {
		for(SkillRegistration reg : SkillRegistration.values()) {
			this.skillSet.put(reg.getClazz(), reg.getNewInstance(cPlayer.getPlayer()));
		}
	}
	
	public Set<Skill> getSkillsForTree(SkillTreeType type){
		return this.skillSet.values().stream().filter(skill -> skill.getTreeType().equals(type)).collect(Collectors.toSet());
	}
	
	@Getter
	private final CombatPlayer cPlayer;
	
	public Skill getSkill(TriggerType type){
		return this.playerSkills.get(type);
	}
	
	//Currently equipped Skills
	@Getter
	private Map<TriggerType, Skill> playerSkills = Maps.newHashMap();
	
	@Getter
	private final Map<Class<? extends Skill>, Skill> skillSet;
	
	@SuppressWarnings("unchecked")
	public <T extends Skill> T getSkillOfClass(Class<? extends Skill> clazz) {
		return (T) this.skillSet.getOrDefault(clazz, null);
	}
	
	public void addSkill(Skill skill) {
		this.playerSkills.put(skill.getTriggerType(), skill);
	}
	
	public void removeSkill(Skill skill) {
		this.playerSkills.put(skill.getTriggerType(), null);
	}
	
	public void clearTrigger(TriggerType type) {
		this.playerSkills.put(type, null);
	}
	
}
