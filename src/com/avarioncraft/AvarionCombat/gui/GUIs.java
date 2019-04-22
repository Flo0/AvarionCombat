package com.avarioncraft.AvarionCombat.gui;

import com.avarioncraft.AvarionCombat.skills.TriggerType;

import net.crytec.api.smartInv.SmartInventory;

public class GUIs {
	
	public static final SmartInventory S_CombatMainGUI = SmartInventory.builder().provider(new CombatMain()).title("Kampf Stats").size(5, 9).build();
	public static final SmartInventory S_BuffGUI = SmartInventory.builder().provider(new BuffGUI()).title("Buffs").size(3, 9).build();
	public static final SmartInventory S_StatBuffGUI = SmartInventory.builder().provider(new StatBuffGUI()).title("Stat Buffs").size(5, 9).build();
	public static final SmartInventory S_CombatBuffGUI = SmartInventory.builder().provider(new StatBuffGUI()).title("Kampf Buffs").size(5, 9).build();
	
	public static final SmartInventory S_EquippedSkillsGUI = SmartInventory.builder().provider(new EquippedSkillsGui()).title("Skills wählen").size(5, 9).build();
	public static final SmartInventory S_SkillMainGUI = SmartInventory.builder().provider(new SkillMainGUI()).title("Skill Menü").size(4, 9).build();
	
	public static final SmartInventory S_SkillChooser_SWING = SmartInventory.builder().provider(new SkillChooserGUI(TriggerType.SWING)).title("Skills wählen").size(6, 9).build();
	public static final SmartInventory S_SkillChooser_ATTACK_MEELE = SmartInventory.builder().provider(new SkillChooserGUI(TriggerType.ATTACK_MEELE)).title("Skills wählen").size(6, 9).build();
	public static final SmartInventory S_SkillChooser_ATTACK_RANGED = SmartInventory.builder().provider(new SkillChooserGUI(TriggerType.ATTACK_RANGED)).title("Skills wählen").size(6, 9).build();
	public static final SmartInventory S_SkillChooser_ATTACK_SKILL = SmartInventory.builder().provider(new SkillChooserGUI(TriggerType.ATTACK_SKILL)).title("Skills wählen").size(6, 9).build();
	public static final SmartInventory S_SkillChooser_BLOCK_BREAK = SmartInventory.builder().provider(new SkillChooserGUI(TriggerType.BLOCK_BREAK)).title("Skills wählen").size(6, 9).build();
	public static final SmartInventory S_SkillChooser_CAST_OTHER_SKILL = SmartInventory.builder().provider(new SkillChooserGUI(TriggerType.CAST_OTHER_SKILL)).title("Skills wählen").size(6, 9).build();
	public static final SmartInventory S_SkillChooser_DEATH = SmartInventory.builder().provider(new SkillChooserGUI(TriggerType.DEATH)).title("Skills wählen").size(6, 9).build();
	public static final SmartInventory S_SkillChooser_DEFEND_MAGIC = SmartInventory.builder().provider(new SkillChooserGUI(TriggerType.DEFEND_MAGIC)).title("Skills wählen").size(6, 9).build();
	public static final SmartInventory S_SkillChooser_DEFEND_MEELE = SmartInventory.builder().provider(new SkillChooserGUI(TriggerType.DEFEND_MEELE)).title("Skills wählen").size(6, 9).build();
	public static final SmartInventory S_SkillChooser_DEFEND_RANGED = SmartInventory.builder().provider(new SkillChooserGUI(TriggerType.DEFEND_RANGED)).title("Skills wählen").size(6, 9).build();
	public static final SmartInventory S_SkillChooser_PASSIVE = SmartInventory.builder().provider(new SkillChooserGUI(TriggerType.PASSIVE)).title("Skills wählen").size(6, 9).build();
	public static final SmartInventory S_SkillChooser_SNEAK_CAST = SmartInventory.builder().provider(new SkillChooserGUI(TriggerType.SNEAK_CAST)).title("Skills wählen").size(6, 9).build();
	public static final SmartInventory S_SkillChooser_SPELL_CAST = SmartInventory.builder().provider(new SkillChooserGUI(TriggerType.SPELL_CAST)).title("Skills wählen").size(6, 9).build();
	
}
