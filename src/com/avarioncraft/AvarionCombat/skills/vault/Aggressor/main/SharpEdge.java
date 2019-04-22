package com.avarioncraft.AvarionCombat.skills.vault.Aggressor.main;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.avarioncraft.AvarionCombat.data.DamagePackage;
import com.avarioncraft.AvarionCombat.skills.Skill;
import com.avarioncraft.AvarionCombat.skills.SkillType;
import com.avarioncraft.AvarionCombat.skills.TriggerType;
import com.avarioncraft.AvarionCombat.util.enums.SkillTreeType;

import net.crytec.api.smartInv.content.SlotPos;

public class SharpEdge extends Skill{

	public SharpEdge(Player caster) {
		super(0L, 20, SkillType.ENHANCE, TriggerType.ENHANCING, SkillTreeType.AGGRESSOR, "Geschärfte Präzision", caster, 2, Brutality.class, 1, Material.GLASS, SlotPos.of(1,0));
		
		super.registerVaribale("<chance>", () -> this.baseChance + "%");
		super.registerVaribale("<hp>", () -> "" + this.hpPercent);
		super.registerVaribale("<chanceHP>", () -> this.hpChance + "%");
		super.registerVaribale("<globalChance>", () -> getCurrentExtraChance() + "%");
		
		super.addDescLine("");
		super.addDescLine("§fErhöht die Chance auf einen brutalen");
		super.addDescLine("§fTreffer um <chance>.");
		super.addDescLine("");
		super.addDescLine("§fWenn deine Lebenspunkte unter <hp> liegen, dann");
		super.addDescLine("§fist die Chance stattdessen <chanceHP> größer.");
		super.addDescLine("");
		
		this.baseChance = 2.5F;
		this.hpPercent = 20.0F;
		this.hpChance = 10F;
	}
	
	private float baseChance;
	private float hpPercent;
	private float hpChance;
	
	public float getCurrentExtraChance() {
		if(super.getLevel() == 0) return 0F;
		return this.hasLowHp() ? this.hpChance : this.baseChance;
	}
	
	private boolean hasLowHp() {
		Player player = super.getCaster().getPlayer();
		double hp = player.getHealth();
		double max = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		return ((100D / max) * this.hpPercent) > hp;
	}

	@Override
	public void modifyDamageGeneration(DamagePackage pack) {}

	@Override
	public void onCast(Object[] parameter, Event triggerEvent) {}

	@Override
	public void inflict(Object[] parameter, LivingEntity target) {}

	@Override
	public void cleanUp() {}

	@Override
	public void onLevelUp() {
		this.baseChance += 1.5F;
		this.hpPercent += 1.5F;
		this.hpChance += 3.5F;
	}
}