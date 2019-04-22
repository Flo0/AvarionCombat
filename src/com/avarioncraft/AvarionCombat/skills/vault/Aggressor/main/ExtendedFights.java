package com.avarioncraft.AvarionCombat.skills.vault.Aggressor.main;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.avarioncraft.AvarionCombat.data.DamagePackage;
import com.avarioncraft.AvarionCombat.skills.Skill;
import com.avarioncraft.AvarionCombat.skills.SkillType;
import com.avarioncraft.AvarionCombat.skills.TriggerType;
import com.avarioncraft.AvarionCombat.util.enums.SkillTreeType;
import com.avarioncraft.AvarionCombat.util.enums.Stat;

import net.crytec.api.smartInv.content.SlotPos;

public class ExtendedFights extends Skill{

	public ExtendedFights(Player caster) {
		super(0, 20, SkillType.ENHANCE, TriggerType.ENHANCING, SkillTreeType.AGGRESSOR, "Weg der Ausdauer", caster, 2, SejuStyle.class, 1,
				Material.BONE, SlotPos.of(1, 3));
		
		super.registerVaribale("<value>", () -> "" + this.extra);
		super.registerVaribale("<armor>", () -> "" + this.armor);
		
		super.addDescLine("");
		super.addDescLine("§fDieser Weg erweitert die Möglichkeit");
		super.addDescLine("§fdie Kombo um <value> zu steigern.");
		super.addDescLine("");
		super.addDescLine("§fAußerdem erhälst du nun <armor> §6" + Stat.ARMOR.getDisplayName());
		super.addDescLine("§fpro Kombopunkt.");
		super.addDescLine("");
		
		this.armor = 0;
		this.extra = 4;
	}
	
	private double armor;
	private int extra;
	
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
		
		this.extra += 4;
		SejuStyle style = ((SejuStyle)super.getCaster().getSkillEquip().getSkillOfClass(SejuStyle.class));
		style.addMaxCombo(4);
		if(this.getLevel() % 10 == 0 || this.getLevel() == 1) {
			style.addExtraArmor(1);
			this.armor += 1;
		}
	}

}
