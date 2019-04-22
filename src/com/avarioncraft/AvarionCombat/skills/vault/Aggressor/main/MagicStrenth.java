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

public class MagicStrenth extends Skill{

	public MagicStrenth(Player caster) {
		super(0, 20, SkillType.ENHANCE, TriggerType.ENHANCING, SkillTreeType.AGGRESSOR, "Magische Äqivalenz", caster, 2, Brutality.class, 1, Material.ENDER_EYE, SlotPos.of(1, 2));
		
		super.registerVaribale("<multi>", () -> RoundUtil.unsafeRound((double)this.getMulti() * 100, 1) + "%");
		super.registerVaribale("<+damage>", () -> "" + this.getDamage());
		super.registerVaribale("<stat>", () -> Stat.MAGIC_POWER.getDisplayName());
		
		super.addDescLine("");
		super.addDescLine("§fDeine brutalen Treffer verursachen zusätzlich <multi>");
		super.addDescLine("§fdeiner <stat> Schaden.");
		super.addDescLine("");
		
		this.multi = 0.35F;
	}

	private float multi;

	public float getMulti() {
		return this.multi;
	}
	
	public double getDamage() {
		if(super.getLevel() == 0) return 0D;
		return RoundUtil.unsafeRound(super.getCaster().getStatValue(Stat.MAGIC_POWER) * this.multi, 1);
	}
	
	@Override
	public void modifyDamageGeneration(DamagePackage pack) {}

	@Override
	public void onCast(Object[] parameter, Event triggerEvent) {
		
		
		
	}

	@Override
	public void inflict(Object[] parameter, LivingEntity target) {}

	@Override
	public void cleanUp() {}

	@Override
	public void onLevelUp() {
		this.multi += 0.055;
	}
	
}
