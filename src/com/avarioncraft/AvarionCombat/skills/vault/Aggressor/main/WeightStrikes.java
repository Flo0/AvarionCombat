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
import com.avarioncraft.AvarionCombat.util.RoundUtil;
import com.avarioncraft.AvarionCombat.util.enums.SkillTreeType;
import com.avarioncraft.AvarionCombat.util.enums.Stat;

import net.crytec.api.smartInv.content.SlotPos;

public class WeightStrikes extends Skill{

	public WeightStrikes(Player caster) {
		super(0, 20, SkillType.ENHANCE, TriggerType.ENHANCING, SkillTreeType.AGGRESSOR, "Gewichtete Schläge", caster, 2, Brutality.class, 1, Material.ANVIL, SlotPos.of(1,1));
		
		super.registerVaribale("<multi>", () -> "" + RoundUtil.unsafeRound(this.damageMulti * 100, 1) + "%");
		super.registerVaribale("<stat>", () -> "" + Stat.MEELE_DAMAGE.getDisplayName());
		super.registerVaribale("<HP>", () -> "" + this.hpPercent);
		super.registerVaribale("<+multi>", () -> "" + RoundUtil.unsafeRound(this.getGlobalMulti() * 100, 1));
		super.registerVaribale("<+damage>", () -> "" + RoundUtil.unsafeRound(this.getGlobalMulti() * super.getCaster().getStatValue(Stat.MEELE_DAMAGE), 1));
		
		super.addDescLine("");
		super.addDescLine("§fDeine brutalen Schläge verursachen zu-");
		super.addDescLine("§fsätzlich <multi> deines <stat> Schaden.");
		super.addDescLine("");
		super.addDescLine("§fWenn die Lebenspunkte des Ziels unter");
		super.addDescLine("§f<HP>% liegen, dann wird dieser verdoppelt.");
		super.addDescLine("");
		
		this.damageMulti = 0.16F;
		this.hpPercent = 20.0F;
		
	}
	
	private float damageMulti;
	private float hpPercent;
	
	public double getGlobalMulti() {
		if(super.getLevel() == 0) return 0D;
		return (double)this.damageMulti;
	}
	
	public double getExtraDamageMulti(LivingEntity target) {
		if (this.getLevel() == 0) return 0;
		return this.targetLow(target) ? RoundUtil.unsafeRound(this.damageMulti, 3) : RoundUtil.unsafeRound(this.damageMulti, 3) * 2;
	}
	
	private boolean targetLow(LivingEntity target) {
		double hp = target.getHealth();
		double max = target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
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
		this.damageMulti += 0.03;
		this.hpPercent += 2.5;
	}

}
