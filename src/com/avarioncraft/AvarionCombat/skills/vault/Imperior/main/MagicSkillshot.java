package com.avarioncraft.AvarionCombat.skills.vault.Imperior.main;

import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import com.avarioncraft.AvarionCombat.data.DamagePackage;
import com.avarioncraft.AvarionCombat.skills.Skill;
import com.avarioncraft.AvarionCombat.skills.SkillType;
import com.avarioncraft.AvarionCombat.skills.TriggerType;
import com.avarioncraft.AvarionCombat.threads.CheckableSkill;
import com.avarioncraft.AvarionCombat.threads.SkillCheckerThread;
import com.avarioncraft.AvarionCombat.util.enums.SkillTreeType;
import com.google.common.collect.Sets;

import net.crytec.api.smartInv.content.SlotPos;

public class MagicSkillshot extends Skill implements CheckableSkill{

	public MagicSkillshot(Player caster) {
		super(4000, 1, SkillType.SKILLSHOT, TriggerType.SPELL_CAST, SkillTreeType.IMPERIOR, "Magisches Geschoss", caster, 1, null, 1, Material.ENDER_EYE, SlotPos.of(0, 1));
		super.setRange(20);
		
		super.registerVaribale("<damage>", () -> "" + this.damage);
		
		super.addDescLine("§fDu feuerst ein magisches Geschoss,");
		super.addDescLine("§fwelches <damage> Schaden verursacht.");
		
		SkillCheckerThread.registerSkill(this);
		
	}
	
	private double damage;
	
	private double getProjectileSpeed() {
		return 6D;
	}
	
	private final Set<MagicProjectile> projectileSet = Sets.newHashSet();
	
	private MagicProjectile getNextProjectile() {
		Location playerLoc = super.getCaster().getPlayer().getEyeLocation().clone();
		return new MagicProjectile(this, playerLoc, playerLoc.getDirection(), this.getProjectileSpeed());
	}
	
	@Override
	public void modifyDamageGeneration(DamagePackage pack) {
		
	}

	@Override
	public void onCast(Object[] parameter, Event triggerEvent) {
		this.projectileSet.add(this.getNextProjectile());
	}

	@Override
	public void inflict(Object[] parameter, LivingEntity target) {
		
	}

	@Override
	public void cleanUp() {
		
	}
	
	@Override
	public void onCheckup() {
		this.projectileSet.forEach(projectile -> projectile.onTick());
		this.projectileSet.removeIf(projectile -> projectile.shouldBeRemoved);
	}

	@Override
	public void onLevelUp() {}
	
	private class MagicProjectile{
		
		private MagicProjectile(MagicSkillshot skill, Location location, Vector direction, double speed) {
			this.skill = skill;
			this.velocity = direction.clone().normalize().multiply(speed);
			this.size = 1D + (speed / 5D);
			this.location = location;
			this.hitBox = BoundingBox.of(location.toVector(), this.size, this.size, this.size);
			this.ticksLived = 0;
			this.lifespan = 60;
			
			double dist = 0.2D;
			this.particleDist = direction.clone().normalize().multiply(dist);
		}
		
		private Location location;
		private final MagicSkillshot skill;
		private boolean shouldBeRemoved = false;
		private Vector velocity;
		private Vector particleDist;
		private BoundingBox hitBox;
		private final double size;
		
		private int ticksLived;
		private int lifespan;
		
		private void move() {
			this.location.add(this.velocity);
			this.hitBox.shift(this.velocity);
		}
		
		private Optional<LivingEntity> getTarget(){
			
			Set<LivingEntity> entities = this.location.getNearbyEntities(size, size, size).stream()
					.filter(e -> (e instanceof LivingEntity) && (!e.getUniqueId().equals(skill.getCaster().getPlayer().getUniqueId())))
					.map(e -> (LivingEntity)e)
					.collect(Collectors.toSet());
			
			if (entities.isEmpty()) return Optional.empty();
			
			return Optional.ofNullable(Collections.min(entities, Comparator.comparing(s -> s.getLocation().distanceSquared(this.location))));
							
		}
					
		private void onTick() {
			this.ticksLived++;
			this.move();
			Optional<LivingEntity> target = this.getTarget();
			
			location.getWorld().playSound(this.location, Sound.ENTITY_PHANTOM_BITE, 0.8F, 2.55F);
			
			if(target.isPresent()) {
				this.onEntityHit(target.get());
				return;
			}
			if(this.location.getBlock().getType().isSolid()) {
				this.onGroundHit(this.location);
				return;
			}
			if(this.ticksLived >= this.lifespan) {
				this.onRunout(this.location);
				return;
			}
			
			Vector p = this.particleDist.clone();
			DustOptions color = new DustOptions(Color.fromRGB(0, 153, 255), 2.5F);
			while(p.lengthSquared() < this.velocity.lengthSquared()) {
				Particle.REDSTONE.builder().location(this.location.clone().add(p)).receivers(64).count(1).data(color).spawn();
				p.add(this.particleDist);
			}
		}
		
		private void onEntityHit(LivingEntity target) {
			System.out.println("Hit");
			location.getWorld().playSound(this.location, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.0F, 2.0F);
			DustOptions color = new DustOptions(Color.fromRGB(0, 153, 255), 1.5F);
			Particle.REDSTONE.builder().location(this.location).receivers(64).offset(0.5, 0.5, 0.5).count(8).data(color).spawn();
			Particle.CRIT_MAGIC.builder().location(this.location).receivers(64).offset(0.5, 0.5, 0.5).count(8).spawn();
			this.remove();
		}
		
		private void onRunout(Location loc) {
			System.out.println("Runout");
			location.getWorld().playSound(this.location, Sound.ENTITY_CAT_HISS, 1.0F, 2.0F);
			location.getWorld().playSound(this.location, Sound.ENTITY_CAT_HISS, 1.0F, 2.5F);
			DustOptions color = new DustOptions(Color.fromRGB(0, 153, 255), 1.5F);
			Particle.REDSTONE.builder().location(this.location).receivers(64).offset(0.5, 0.5, 0.5).count(4).data(color).spawn();
			Particle.CRIT_MAGIC.builder().location(this.location).receivers(64).offset(0.5, 0.5, 0.5).count(4).spawn();
			this.remove();
		}
		
		private void onGroundHit(Location loc) {
			location.getWorld().playSound(this.location, Sound.BLOCK_STONE_HIT, 1.5F, 1.55F);
			location.getWorld().playSound(this.location, Sound.BLOCK_STONE_HIT, 1.5F, 1.25F);
			DustOptions color = new DustOptions(Color.fromRGB(0, 153, 255), 1.5F);
			Particle.REDSTONE.builder().location(this.location).receivers(64).offset(0.5, 0.5, 0.5).count(4).data(color).spawn();
			Particle.CRIT_MAGIC.builder().location(this.location).receivers(64).offset(0.5, 0.5, 0.5).count(4).spawn();
			System.out.println("Wall");
			this.remove();
		}
		
		private void remove() {
			this.shouldBeRemoved = true;
		}
		
	}



}
