package com.avarioncraft.AvarionCombat.skills.vault.Auxilior.main;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
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
import com.avarioncraft.AvarionCombat.util.ParticleUtil;
import com.avarioncraft.AvarionCombat.util.RoundUtil;
import com.avarioncraft.AvarionCombat.util.enums.SkillTreeType;

import net.crytec.api.recharge.Recharge;
import net.crytec.api.smartInv.content.SlotPos;
import net.crytec.api.util.UtilPlayer;

public class HealShot extends Skill {

	public HealShot(Player caster) {
		super(8000, 1, SkillType.TARGET, TriggerType.SPELL_CAST, SkillTreeType.AUXILIOR, "Strahlender Fokus", caster, 1, null, 1, Material.CACTUS_GREEN, SlotPos.of(0, 4));
	
		super.registerVaribale("<restore>", () -> String.valueOf(RoundUtil.unsafeRound(restore, 2)));
		super.registerVaribale("<reach>", () -> String.valueOf(RoundUtil.unsafeRound(reach, 2)));
		super.registerVaribale("<hitbox>", () -> String.valueOf(RoundUtil.unsafeRound(hitbox, 2)));
		
		super.addDescLine("");
		super.addDescLine("Du heilst mit einem Strahl des heiligen");
		super.addDescLine("Feuers einen Verbündeten um <restore>");
		super.addDescLine("Deine Magie reicht bis zu einem Radius von <reach> Blöcken.");
		super.addDescLine("");
	
	}
	
	private double restore = 10;
	private double reach = 60;
	
	private final static double hitbox = 1;

	@Override
	public void modifyDamageGeneration(DamagePackage pack) {
		
	}

	@Override
	public void onCast(Object[] parameter, Event event) {
		
		Player player = this.getCaster().getPlayer();
		Vector dir = player.getEyeLocation().clone().getDirection();
		RayTraceResult result = player.getWorld().rayTraceEntities(player.getLocation().clone(), dir, reach, hitbox, entity -> !entity.getUniqueId().equals(player.getUniqueId()));
		
		if (result == null || result.getHitEntity() == null) {
			//Reset Cooldown on invalid target
			Recharge.Instance.recharge(player, this.getName());
			return;
		}
		
		Entity target = result.getHitEntity();

		if (!(target instanceof LivingEntity)) {
			//Reset Cooldown on invalid target
			Recharge.Instance.recharge(player, this.getName());
			return;
		}
		
		LivingEntity entity = (LivingEntity) target;
		
		Location from = player.getEyeLocation();
		Location to = target.getLocation().add(0, (target.getBoundingBox().getHeight() / 2) , 0);
		
		ParticleUtil.line(Particle.REDSTONE, from, to, particle -> particle.extra(0).color(Color.GREEN).receivers(24).spawn());
		UtilPlayer.playSound(player, Sound.ENTITY_CHICKEN_EGG, SoundCategory.PLAYERS, 1, 0.90F);
		this.healEntity(entity, restore);
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
