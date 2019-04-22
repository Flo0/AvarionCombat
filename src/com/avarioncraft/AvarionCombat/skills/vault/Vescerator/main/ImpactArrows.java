package com.avarioncraft.AvarionCombat.skills.vault.Vescerator.main;

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

import net.crytec.api.smartInv.content.SlotPos;

public class ImpactArrows extends Skill{

	public ImpactArrows(Player caster) {
		super(0, 1, SkillType.TARGET, TriggerType.ATTACK_RANGED, SkillTreeType.VESCERATOR, "Heftiger Einschlag", caster, 1, null, 1, Material.FLINT, SlotPos.of(0, 4));
		
		super.registerVaribale("<damage>", () -> "" + this.getBaseDamage());
		super.registerVaribale("<multi>", () -> "" + this.damageMultiplier * 100 + "%");
		super.registerVaribale("<chance>", () -> "" + this.chance + "%");
		super.registerVaribale("<stat>", () -> Stat.RANGED_DAMAGE.getDisplayName());
		
		super.addDescLine("");
		super.addDescLine("§fBei einem Pfeil-Treffer hast du eine Chance");
		super.addDescLine("§fvon <chance> §e(§6+<extraChance>§e)§f einen heftigen Einschlag zu landen.");
		super.addDescLine("");
		super.addDescLine("§fDieser verursacht <multi> §e(§6+<extraMulti>§e)§f deines <stat>");
		super.addDescLine("§fals zusätzlichen magischen Schaden.");
		super.addDescLine("§fMomentaner Schaden: <damage> §e(§6+<extraDamage>§e)§f §9(§6+<extraMagic>§9)§f");
		super.addDescLine("");
		
		this.dmg = 10D;
		this.damageMultiplier = 0.3F;
		this.chance = 15.0D;
	}
	
	private double dmg;
	private float damageMultiplier;
	private double chance;
	
	private double getBaseDamage() {
		return this.dmg;
	}

	@Override
	public void modifyDamageGeneration(DamagePackage pack) {
		
	}

	@Override
	public void onCast(Object[] parameter, Event triggerEvent) {
		EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) triggerEvent;
		
		if(!(event.getEntity() instanceof LivingEntity)) return;
		
		if(ThreadLocalRandom.current().nextDouble(100D) > this.chance) return;
		
		SkillDamageEvent skillEvent = new SkillDamageEvent(super.getCaster().getPlayer(), event.getEntity(), DamageCause.MAGIC, this.getBaseDamage(), this);
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
	public void onLevelUp() {
		
	}

}
