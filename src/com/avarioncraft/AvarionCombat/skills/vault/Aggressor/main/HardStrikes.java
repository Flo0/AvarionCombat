package com.avarioncraft.AvarionCombat.skills.vault.Aggressor.main;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.avarioncraft.AvarionCombat.data.DamagePackage;
import com.avarioncraft.AvarionCombat.skills.Skill;
import com.avarioncraft.AvarionCombat.skills.SkillType;
import com.avarioncraft.AvarionCombat.skills.TriggerType;
import com.avarioncraft.AvarionCombat.util.RoundUtil;
import com.avarioncraft.AvarionCombat.util.enums.SkillTreeType;

import net.crytec.api.smartInv.content.SlotPos;

public class HardStrikes extends Skill{

	public HardStrikes(Player caster) {
		super(0, 20, SkillType.ENHANCE, TriggerType.ENHANCING, SkillTreeType.AGGRESSOR, "Gewichtete Treffer", caster, 2, SejuStyle.class, 1, Material.ANVIL, SlotPos.of(1,4));
		
		super.registerVaribale("<speed>", () -> "" + RoundUtil.unsafeRound(this.speed, 3) + "%");
		super.registerVaribale("<crit>", () -> "" + RoundUtil.unsafeRound(this.crit, 3) + "%");
		
		super.addDescLine("");
		super.addDescLine("§fDieser Weg steigert langsam dein Lauftempo");
		super.addDescLine("§fund deine kritische Trefferchance.");
		super.addDescLine("");
		super.addDescLine("§f<speed> Lauftempo pro Kombopunkt");
		super.addDescLine("§f<crit> kritische Trefferchance pro Kombopunkt");
		super.addDescLine("");
		
		this.speed = 0.04;
		this.crit = 0.05;
	}
	
	private double crit;
	private double speed;
	
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
		this.speed += 0.04;
		this.crit += 0.01;
		SejuStyle style = ((SejuStyle)super.getCaster().getSkillEquip().getSkillOfClass(SejuStyle.class));
		style.addExtraCrit(0.01);
		style.addExtraSpeed(0.04);
	}

}
