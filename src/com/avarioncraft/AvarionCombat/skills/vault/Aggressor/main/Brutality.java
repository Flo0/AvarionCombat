package com.avarioncraft.AvarionCombat.skills.vault.Aggressor.main;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.avarioncraft.AvarionCombat.data.DamagePackage;
import com.avarioncraft.AvarionCombat.events.SkillDamageEvent;
import com.avarioncraft.AvarionCombat.skills.Skill;
import com.avarioncraft.AvarionCombat.skills.SkillType;
import com.avarioncraft.AvarionCombat.skills.TriggerType;
import com.avarioncraft.AvarionCombat.util.enums.SkillTreeType;
import com.avarioncraft.AvarionCombat.util.enums.Stat;

import lombok.Getter;
import lombok.Setter;
import net.crytec.api.smartInv.content.SlotPos;

public class Brutality extends Skill{
	
	public Brutality(Player caster) {
		super(0, 1, SkillType.SINGLE, TriggerType.ATTACK_MEELE, SkillTreeType.AGGRESSOR, "Brutale Schläge", caster, 1, null, 1, Material.IRON_AXE, SlotPos.of(0,1));
		this.chance = 20.0F;
		this.damageMultiplier = 0.5F;
		
		super.registerVaribale("<damage>", () -> "" + this.getBaseDamage());
		super.registerVaribale("<multi>", () -> "" + this.damageMultiplier * 100 + "%");
		super.registerVaribale("<chance>", () -> "" + this.chance + "%");
		super.registerVaribale("<extraChance>", () -> super.getCaster().getSkillEquip().getSkillOfClass(SharpEdge.class).getValueOf("<globalChance>"));
		super.registerVaribale("<extraMulti>", () -> super.getCaster().getSkillEquip().getSkillOfClass(WeightStrikes.class).getValueOf("<+multi>") + "%");
		super.registerVaribale("<extraDamage>", () -> super.getCaster().getSkillEquip().getSkillOfClass(WeightStrikes.class).getValueOf("<+damage>"));
		super.registerVaribale("<extraMagic>", () -> super.getCaster().getSkillEquip().getSkillOfClass(MagicStrenth.class).getValueOf("<+damage>"));
		super.registerVaribale("<stat>", () -> Stat.MEELE_DAMAGE.getDisplayName());
		
		super.addDescLine("");
		super.addDescLine("§fBei einem physischen Treffer hast du eine Chance");
		super.addDescLine("§fvon <chance> §e(§6+<extraChance>§e)§f einen brutalen Schlag zu landen.");
		super.addDescLine("");
		super.addDescLine("§fDieser verursacht <multi> §e(§6+<extraMulti>§e)§f deines <stat>");
		super.addDescLine("§fals zusätzlichen magischen Schaden.");
		super.addDescLine("§fMomentaner Schaden: <damage> §e(§6+<extraDamage>§e)§f §9(§6+<extraMagic>§9)§f");
		super.addDescLine("");
		
	}
	
	@Getter @Setter
	private float chance;
	@Getter @Setter
	private float damageMultiplier;
	
	private double getExtraDamage(double multi) {
		return super.getCaster().getStatValue(Stat.MEELE_DAMAGE) * multi;
	}
	
	private double getBaseDamage() {
		return this.getExtraDamage(this.damageMultiplier);
	}
	
	@Override
	public void modifyDamageGeneration(DamagePackage pack) {
		
	}
	
	@Override
	public void onCast(Object[] parameter, Event triggerEvent) {
		
		EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) triggerEvent;
		
		if(!(event.getEntity() instanceof LivingEntity)) return;
		
		double extraChance = ((SharpEdge)super.getCaster().getSkillEquip().getSkillOfClass(SharpEdge.class)).getCurrentExtraChance();
		double extraMulti = ((WeightStrikes)super.getCaster().getSkillEquip().getSkillOfClass(WeightStrikes.class)).getExtraDamageMulti((LivingEntity)event.getEntity());
		
		double extraDamage = getExtraDamage(extraMulti) + ((MagicStrenth)super.getCaster().getSkillEquip().getSkillOfClass(MagicStrenth.class)).getDamage();
		
		if(ThreadLocalRandom.current().nextDouble(100D) > this.chance + extraChance) return;
		
		SkillDamageEvent skillEvent = new SkillDamageEvent(super.getCaster().getPlayer(), event.getEntity(), DamageCause.MAGIC, this.getBaseDamage() + extraDamage, this);
		Bukkit.getPluginManager().callEvent(skillEvent);
		
		if(!skillEvent.isCancelled()) {
			Particle.REDSTONE.builder().color(Color.RED).count(8).location(skillEvent.getEntity().getLocation()).receivers(16).offset(1.5D, 1D, 1.5D).spawn();
			skillEvent.getEntity().getWorld().playSound(skillEvent.getEntity().getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1.0F, 0.5F);
		}
		
	}
	
	@Override
	public void inflict(Object[] parameter, LivingEntity target) {
		
	}
	
	@Override
	public void cleanUp() {
		
	}

	@Override
	public void onLevelUp() {}

}
