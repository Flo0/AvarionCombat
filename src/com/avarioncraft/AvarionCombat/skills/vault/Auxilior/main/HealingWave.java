package com.avarioncraft.AvarionCombat.skills.vault.Auxilior.main;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.util.Vector;

import com.avarioncraft.AvarionCombat.data.DamagePackage;
import com.avarioncraft.AvarionCombat.skills.Skill;
import com.avarioncraft.AvarionCombat.skills.SkillType;
import com.avarioncraft.AvarionCombat.skills.TriggerType;
import com.avarioncraft.AvarionCombat.util.RoundUtil;
import com.avarioncraft.AvarionCombat.util.enums.SkillTreeType;
import com.google.common.collect.Lists;

import net.crytec.api.interfaces.ConditionalRunnable;
import net.crytec.api.smartInv.content.SlotPos;
import net.crytec.api.util.Tasks;

public class HealingWave extends Skill {

	public HealingWave(Player caster) {
		super(12000, 1, SkillType.AOE, TriggerType.SPELL_CAST, SkillTreeType.AUXILIOR, "Wiederherstellender Geist", caster, 1, null, 1, Material.GHAST_TEAR, SlotPos.of(0, 7));
	
		super.registerVaribale("<restore>", () -> String.valueOf(RoundUtil.unsafeRound(restore, 2)));
		super.registerVaribale("<length>", () -> String.valueOf(RoundUtil.unsafeRound(length, 2)));
		
		super.addDescLine("");
		super.addDescLine("§fDu erschaffst eine Welle der Heilung,");
		super.addDescLine("§fdiese Heilt alle Verbündeten um <restore> Leben");
		super.addDescLine("§fLänge: <length>");
		super.addDescLine("");
	
	}

	private double restore = 10;
	private double length = 10;
	
	@Override
	public void modifyDamageGeneration(DamagePackage pack) {
		
	}

	@Override
	public void onCast(Object[] parameter, Event triggerEvent) {
		
		Player player = this.getCaster().getPlayer();
		
		Location ploc = player.getLocation().clone();
		ploc.setPitch(0F);
		
		Location front = ploc.add(ploc.getDirection().multiply(this.length));
		front.add(0, 0.15, 0);
		
		ArrayList<Location> locs = points(player.getLocation().clone().add(ploc.getDirection().multiply(1.5)), front);
		
		Tasks.schedule(new ConditionalRunnable() {
			int i = 0;
			@Override
			public void run() {
				if (i > locs.size()) return;
				Location loc = locs.get(i);
				
				loc.getWorld().getNearbyEntities(loc, 1.5, 3, 1.5).stream().filter(e -> e instanceof LivingEntity).map(e -> (LivingEntity) e).forEach(ent -> {
					healEntity(ent, restore);
				});
				
				Particle.VILLAGER_HAPPY.builder().location(locs.get(i)).receivers(64).extra(0).count(25).offset(1.5, 0, 1.5).spawn();
				i++;
			}
			
			@Override
			public boolean canCancel() {
				return false;
			}
		}, 1, 5, locs.size());
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
	
	private ArrayList<Location> points(Location from, Location to) {
		ArrayList<Location> locs = Lists.newArrayList();
		from = from.clone();
		to = to.clone();
		
		double distanceSq = from.distanceSquared(to);
		
		Vector direction = to.clone().subtract(from).toVector().normalize();
		Vector line = direction.clone();
		
		while(line.lengthSquared() < distanceSq) {
			locs.add(from.clone().add(line));
			line.add(direction);
		}
		return locs;
	}
	
	private void healEntity(LivingEntity entity, double restore) {
		double maxHealth = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		
		if (entity.getHealth() < maxHealth) {
			
			if ( (entity.getHealth() + restore) >= maxHealth) {
				entity.setHealth(maxHealth);
				new EntityRegainHealthEvent(entity, restore, RegainReason.MAGIC_REGEN).callEvent();
			} else {
				entity.setHealth(entity.getHealth() + restore);
				new EntityRegainHealthEvent(entity, restore, RegainReason.MAGIC_REGEN).callEvent();
			}
		}
	}
}