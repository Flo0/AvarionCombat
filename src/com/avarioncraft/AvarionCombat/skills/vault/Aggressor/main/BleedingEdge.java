package com.avarioncraft.AvarionCombat.skills.vault.Aggressor.main;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
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
import com.avarioncraft.AvarionCombat.threads.CheckableSkill;
import com.avarioncraft.AvarionCombat.threads.SkillCheckerThread;
import com.avarioncraft.AvarionCombat.util.enums.SkillTreeType;
import com.google.common.collect.Sets;

import net.crytec.api.smartInv.content.SlotPos;

public class BleedingEdge extends Skill implements CheckableSkill{

	public BleedingEdge(Player caster) {
		super(0, 1, SkillType.TARGET, TriggerType.ATTACK_MEELE, SkillTreeType.AGGRESSOR, "Verwunden", caster, 1, null, 1, Material.ROSE_RED, SlotPos.of(0, 7));
		
		super.registerVaribale("<chance>", () -> this.chance + "%");
		super.registerVaribale("<seconds>", () -> this.time + " Sek.");
		super.registerVaribale("<damage>", () -> "" + this.damage);
		
		super.addDescLine("§fDu hast eine Chance von <chance> dem");
		super.addDescLine("§fGegner eine blutende Wunde zuzufügen.");
		super.addDescLine("");
		super.addDescLine("§fDiese hält <seconds> an und verursacht");
		super.addDescLine("§finsgesamt <damage> Schaden.");
		
		SkillCheckerThread.registerSkill(this);
		
	}
	
	private double chance = 5.0D;
	private double damage = 10.0D;
	private int time = 3;
	
	private final Set<BleedingEnemy> bleedingEnemys = Sets.newHashSet();
	
	@Override
	public void modifyDamageGeneration(DamagePackage pack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCast(Object[] parameter, Event triggerEvent) {
		EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) triggerEvent;
		if(!(event.getEntity() instanceof LivingEntity)) return;
		
		if(ThreadLocalRandom.current().nextDouble(100.1D) < this.chance) {
			System.out.println("bleeding");
			this.bleedingEnemys.add(new BleedingEnemy(this, (LivingEntity) event.getEntity(), this.time, this.damage / this.time));
		}
		
	}

	@Override
	public void inflict(Object[] parameter, LivingEntity target) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLevelUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCheckup() {
		this.bleedingEnemys.forEach(be ->{
			if(be.onTick()) {
				this.bleedingEnemys.remove(be);
			}
		});
	}
	
	private class BleedingEnemy{
		private BleedingEnemy(BleedingEdge skillInstance, LivingEntity enemy, int bleeds, double dmg) {
			this.enemy = enemy;
			this.bleeds = bleeds;
			this.dmg = dmg;
			this.bleedsDone = 0;
			this.lastBleed = 0;
			this.skillInstance = skillInstance;
		}
		
		private final BleedingEdge skillInstance;
		private final LivingEntity enemy;
		private final int bleeds;
		private final double dmg;
		private int bleedsDone;
		private long lastBleed;
		
		public boolean onTick() {
			if(this.enemy.isDead()) return true;
			long time = System.currentTimeMillis();
			if(time - lastBleed >= 1000) {
				this.lastBleed = time;
				SkillDamageEvent skillEvent = new SkillDamageEvent(this.skillInstance.getCaster().getPlayer(), this.enemy, DamageCause.MAGIC, this.dmg, this.skillInstance);
				Bukkit.getPluginManager().callEvent(skillEvent);
				Particle.REDSTONE.builder().color(Color.RED).count(8).location(skillEvent.getEntity().getLocation().clone().add(0, 0.5, 0)).receivers(16).offset(0.4D, 1D, 0.4D).spawn();
				this.bleedsDone++;
				if(this.bleedsDone >= this.bleeds) return true;
			}
			return false;
		}
	}
	
}
