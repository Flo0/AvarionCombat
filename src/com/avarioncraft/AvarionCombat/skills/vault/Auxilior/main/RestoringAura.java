package com.avarioncraft.AvarionCombat.skills.vault.Auxilior.main;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import com.avarioncraft.AvarionCombat.data.DamagePackage;
import com.avarioncraft.AvarionCombat.skills.Skill;
import com.avarioncraft.AvarionCombat.skills.SkillType;
import com.avarioncraft.AvarionCombat.skills.TriggerType;
import com.avarioncraft.AvarionCombat.threads.CheckableSkill;
import com.avarioncraft.AvarionCombat.threads.SkillCheckerThread;
import com.avarioncraft.AvarionCombat.util.enums.SkillTreeType;
import com.avarioncraft.AvarionCombat.util.enums.Stat;

import net.crytec.api.smartInv.content.SlotPos;

public class RestoringAura extends Skill implements CheckableSkill {

	public RestoringAura(Player caster) {
		super(60000, 1, SkillType.AOE, TriggerType.SPELL_CAST, SkillTreeType.AUXILIOR, "Ritual des Lichts", caster, 1, null, 1, Material.GREEN_CARPET, SlotPos.of(0, 1));
	
		super.registerVaribale("<duration>", () -> "" + this.duration);
		super.registerVaribale("<radius>", () -> "" + this.radius);
		super.registerVaribale("<tick>", () -> "" + this.tick);
		super.registerVaribale("<restoreHP>", () -> "" + this.getHealthRestore());
		
		super.addDescLine("");
		super.addDescLine("§fDu badest im Feuer des Sanguis,");
		super.addDescLine("§fum  dich und deine Verbündeten für <duration>s lang");
		super.addDescLine("§falle <tick>s um <restoreHP>  zu heilen.");
		super.addDescLine("§fDeine gebündelte spirituelle Hingabe");
		super.addDescLine("§freicht bis zu einem Radius von <radius> Blöcken.");
		super.addDescLine("");
		
		this.duration = 12;
		this.radius = 4;
		this.tick = 3;
		this.health = 1;
		
		SkillCheckerThread.registerSkill(this);
	}
	
	private int duration;
	private int radius;
	private int tick;
	private double health;
	
	private final static DustOptions colordata = new DustOptions(Color.YELLOW, 2F);
	
	
	private Location castLocation;
	private long castTime = 0;
	private int tickslived = 0;
	
	public double getHealthRestore() {
		return this.health + (0.15 * this.getCaster().getStatValue(Stat.MAGIC_POWER));
	}

	@Override
	public void modifyDamageGeneration(DamagePackage pack) {
	}

	@Override
	public void onCast(Object[] parameter, Event triggerEvent) {
		
		RayTraceResult result = this.getCaster().getPlayer().rayTraceBlocks(64);
		if (result.getHitBlock() == null || result.getHitBlock().getType() == Material.AIR) return;
		
		this.castLocation = result.getHitBlock().getLocation();
		this.castTime = System.currentTimeMillis();
		
	}

	@Override
	public void inflict(Object[] parameter, LivingEntity target) {}

	@Override
	public void cleanUp() {}

	@Override
	public void onLevelUp() {}

	@Override
	public void onCheckup() {
		
		if (this.castLocation == null) return;
		if (System.currentTimeMillis() - castTime > (this.duration * 1000) ) {
			this.castLocation = null;
			this.castTime = 0;
			this.tickslived = 0;
			return;
		}
		
		double n = 1;
		
		while (n < 10) {
			Vector vector = new Vector(Math.cos((double) this.getCaster().getPlayer().getTicksLived() * 3.141592653589793 / 18.0) * this.radius, 0.1, Math.sin((double) this.getCaster().getPlayer().getTicksLived() * 3.141592653589793 / 18.0) * this.radius);
			Particle.REDSTONE.builder().location(this.castLocation.add(vector.multiply(2))).extra(0).data(colordata).receivers(32).spawn();
			this.castLocation.subtract(vector);
			++n;
		}
		
		if (this.tickslived % (20 * this.tick) == 0) {
			double restore = this.getHealthRestore();
			
			this.castLocation.getWorld().getNearbyEntities(this.castLocation, this.radius, 25, this.radius, ent -> (ent instanceof Player))
			.stream().map(ent -> (Player) ent).forEach(cur -> healEntity(cur, restore));
		}
		this.tickslived++;
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
