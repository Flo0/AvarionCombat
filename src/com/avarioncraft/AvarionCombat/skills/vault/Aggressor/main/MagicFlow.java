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
import com.avarioncraft.AvarionCombat.util.enums.Stat;

import net.crytec.api.smartInv.content.SlotPos;

public class MagicFlow extends Skill{

	public MagicFlow(Player caster) {
		super(0, 20, SkillType.ENHANCE, TriggerType.ENHANCING, SkillTreeType.AGGRESSOR, "Weg der Durchflutung", caster, 2, SejuStyle.class, 1,
				Material.DIAMOND, SlotPos.of(1,5));
		this.magic = 0.5;
		
		super.registerVaribale("<magic>", ()-> "" + RoundUtil.unsafeRound(this.magic, 3) + "%");
		super.registerVaribale("<stat>", () -> Stat.MAGIC_POWER.getDisplayName());
		
		super.addDescLine("");
		super.addDescLine("§fDieser Weg erlaubt es dir durch magsiche");
		super.addDescLine("§fDurchflutung deine <stat> für jeden Punkt");
		super.addDescLine("§fum <magic> zu erhöhen.");
		super.addDescLine("");
		
	}
	
	private double magic;

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
		this.magic += 0.05;
		SejuStyle style = ((SejuStyle)super.getCaster().getSkillEquip().getSkillOfClass(SejuStyle.class));
		style.addExtraMagic(0.05);
	}

}
