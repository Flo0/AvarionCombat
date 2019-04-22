package com.avarioncraft.AvarionCombat.skills;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.entity.Player;

import com.avarioncraft.AvarionCombat.skills.vault.Aggressor.main.BleedingEdge;
import com.avarioncraft.AvarionCombat.skills.vault.Aggressor.main.Brutality;
import com.avarioncraft.AvarionCombat.skills.vault.Aggressor.main.ExtendedFights;
import com.avarioncraft.AvarionCombat.skills.vault.Aggressor.main.HardStrikes;
import com.avarioncraft.AvarionCombat.skills.vault.Aggressor.main.MagicFlow;
import com.avarioncraft.AvarionCombat.skills.vault.Aggressor.main.MagicStrenth;
import com.avarioncraft.AvarionCombat.skills.vault.Aggressor.main.SejuStyle;
import com.avarioncraft.AvarionCombat.skills.vault.Aggressor.main.SharpEdge;
import com.avarioncraft.AvarionCombat.skills.vault.Aggressor.main.WeightStrikes;
import com.avarioncraft.AvarionCombat.skills.vault.Auxilior.main.HealShot;
import com.avarioncraft.AvarionCombat.skills.vault.Auxilior.main.HealingWave;
import com.avarioncraft.AvarionCombat.skills.vault.Auxilior.main.RestoringAura;
import com.avarioncraft.AvarionCombat.skills.vault.Imperior.main.MagicSkillshot;
import com.avarioncraft.AvarionCombat.skills.vault.Vescerator.main.ArcaneArrows;
import com.avarioncraft.AvarionCombat.skills.vault.Vescerator.main.ImpactArrows;
import com.avarioncraft.AvarionCombat.skills.vault.Vescerator.main.Overdrawing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.crytec.shaded.org.apache.lang3.reflect.ConstructorUtils;

@AllArgsConstructor
public enum SkillRegistration {
	
	RESTORING_AURA("Wiederherstellende Aura", RestoringAura.class, false),
	
	BRUTALITY("brutality", Brutality.class, false),
	SHARP_EDGE("sharpEdge", SharpEdge.class, true),
	MAGIC_STRENGTH("magicstrength", MagicStrenth.class, true),
	WEIGTH_STRIKES("weightstrikes", WeightStrikes.class, true),
	EXTENDED_FIGHTS("extendedfights", ExtendedFights.class, true),
	HARD_STRIKES("hardstrikes", HardStrikes.class, true),
	MAGIC_FLOW("magicflow", MagicFlow.class, true),
	HEALSHOT("healshot", HealShot.class, false),
	HEALING_WAVE("healwave", HealingWave.class, false),
	SEJU_STYLE("sejustyle", SejuStyle.class, false),
	OVERDRAWING("overdrawing", Overdrawing.class, false),
	ELEMENTAL_ARROWS("elementalarrows", ArcaneArrows.class, false),
	IMPACT_ARROWS("impactArrows", ImpactArrows.class, false),
	MAGIC_SKILLSHOT("magicprojectile", MagicSkillshot.class, false),
	BLLEDING_EDGE("bleedingedge", BleedingEdge.class, false);
	
	@Getter
	private String id;
	@Getter
	private Class<? extends Skill> clazz;
	@Getter
	private boolean child;
	
	public Skill getNewInstance(Player player) {
		
		Skill skill = null;
		
		try {
			skill = ConstructorUtils.invokeConstructor(this.clazz, player );
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException| InstantiationException e) {
			e.printStackTrace();
		}		
		return skill;
	}	
}