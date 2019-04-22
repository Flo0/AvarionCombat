package com.avarioncraft.AvarionCombat.skills.vault.Vescerator.main;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

import com.avarioncraft.AvarionCombat.data.CombatPlayer;
import com.avarioncraft.AvarionCombat.data.DamagePackage;
import com.avarioncraft.AvarionCombat.entity.ArcaneArrow;
import com.avarioncraft.AvarionCombat.events.SkillDamageEvent;
import com.avarioncraft.AvarionCombat.events.TwohandedWeaponToggleEvent;
import com.avarioncraft.AvarionCombat.skills.Skill;
import com.avarioncraft.AvarionCombat.skills.SkillType;
import com.avarioncraft.AvarionCombat.skills.TriggerType;
import com.avarioncraft.AvarionCombat.util.ParticleUtil;
import com.avarioncraft.AvarionCombat.util.enums.ArcaneArrowType;
import com.avarioncraft.AvarionCombat.util.enums.CombatRelationship;
import com.avarioncraft.AvarionCombat.util.enums.SkillTreeType;

import net.crytec.api.smartInv.content.SlotPos;
import net.crytec.api.util.UtilPlayer;

public class ArcaneArrows extends Skill {

	public ArcaneArrows(Player caster) {
		super(0, 1, SkillType.SKILLSHOT, TriggerType.ATTACK_RANGED, SkillTreeType.VESCERATOR, "Elementarpfeile", caster, 1, null, 0, Material.SPECTRAL_ARROW, SlotPos.of(0, 7));
		this.currentType = ArcaneArrowType.NATURE;
		
		super.addDescLine("§fDu kannst §63 §farten von Pfeilen schießen.");
		super.addDescLine("§fDrücke §6F §fum zwischen den Pfeilen zu wechseln.");
		super.addDescLine("");
		super.addDescLine("§eAstral");
		super.addDescLine("§fDeine Pfeile treffen §6Gegner §fund verursachen <asDmg>");
		super.addDescLine("§fzusätzlichen Schaden. Außerdem wird das Ziel mit einer");
		super.addDescLine("§fChance von <asChance> verlangsamt.");
		super.addDescLine("");
		super.addDescLine("§eNatur");
		super.addDescLine("§fDeine Pfeile treffen §6Verbündete §fund heilen diese");
		super.addDescLine("§ffür <naHeal>.");
		super.addDescLine("");
		super.addDescLine("§eInferno");
		super.addDescLine("§fDeine Pfeile treffen §6Gegner §fverursachen <inDmg>");
		super.addDescLine("§fzusätzlichen Schaden.");
		
		this.astralDamage = 4.0D;
		this.astralChance = 23.5D;
		this.natureHeal = 6.0D;
		this.infernoDamage = 7.0D;
		
	}
	
	private ArcaneArrowType currentType;
	
	private double astralDamage;
	private double astralChance;
	private double natureHeal;
	private double infernoDamage;
	
	private PotionEffect astralEffect = new PotionEffect(PotionEffectType.SLOW, 45, 1);
	
	public ArcaneArrow getArrowToShoot() {
		return new ArcaneArrow(super.getCaster().getPlayer().getWorld(), this.currentType, this);
	}
	
	public void cycleArrow() {
		this.currentType = ArcaneArrowType.values()[this.currentType.ordinal() < 2 ? this.currentType.ordinal() + 1 : 0];
		super.informCaster("Dein Pfeiltyp wurde zu §6" + this.currentType.getDisplayName() + "§f gewechselt.");
		UtilPlayer.playSound(super.getCaster().getPlayer(), Sound.ITEM_ARMOR_EQUIP_GENERIC, 0.5F, 1.45F);
	}
	
	@Override
	public void modifyDamageGeneration(DamagePackage pack) {
		
	}

	@Override
	public void onCast(Object[] parameter, Event triggerEvent) {
		
	}

	@Override
	public void inflict(Object[] parameter, LivingEntity target) {
		ArcaneArrow arrow = (ArcaneArrow)parameter[0];
		switch(arrow.getArcaneType()) {
		case ASTRAL: this.astralArrowHit(arrow, target);
			break;
		case NATURE: this.natureArrowHit(arrow, target);
			break;
		case INFERNO: this.infernoArrowHit(arrow, target);
			break;
		default:
			break;
		}
	}
	
	private void astralArrowHit(ArcaneArrow arrow, LivingEntity target) {
		if(super.getCaster().getRelationShip(target).equals(CombatRelationship.FRIENDLY)) return;
		new SkillDamageEvent(super.getCaster().getPlayer(), target, DamageCause.MAGIC, this.astralDamage, this).callEvent();
		Particle.CRIT_MAGIC.builder().count(8).offset(0.5, 0.5, 0.5).location(target.getLocation()).receivers(48).spawn();
		if(ThreadLocalRandom.current().nextDouble(0D, 100.1D) < this.astralChance) {
			target.addPotionEffect(this.astralEffect);
			ParticleUtil.circle(Particle.CRIT_MAGIC, target.getLocation().clone().add(0, 1.1, 0), 2, 36, part->part.receivers(48).extra(0).spawn());
			ParticleUtil.circle(Particle.CRIT_MAGIC, target.getLocation().clone().add(0, 0.6, 0), 2, 36, part->part.receivers(48).extra(0).spawn());
			ParticleUtil.circle(Particle.CRIT_MAGIC, target.getLocation().clone().add(0, 0.1, 0), 2, 36, part->part.receivers(48).extra(0).spawn());
		}
	}
	
	private void infernoArrowHit(ArcaneArrow arrow, LivingEntity target) {
		if(super.getCaster().getRelationShip(target).equals(CombatRelationship.FRIENDLY)) return;
		new SkillDamageEvent(super.getCaster().getPlayer(), target, DamageCause.MAGIC, this.infernoDamage, this).callEvent();
		Particle.FLAME.builder().extra(0.1).count(8).offset(0.5, 0.5, 0.5).location(target.getLocation()).receivers(48).spawn();
	}
	
	private void natureArrowHit(ArcaneArrow arrow, LivingEntity target) {
		if(!super.getCaster().getRelationShip(target).equals(CombatRelationship.FRIENDLY)) return;
		new EntityRegainHealthEvent(target, this.natureHeal, RegainReason.MAGIC);
		Particle.VILLAGER_HAPPY.builder().count(12).offset(0.5, 0.5, 0.5).location(target.getLocation()).receivers(48).spawn();
	}

	@Override
	public void cleanUp() {
		
	}

	@Override
	public void onLevelUp() {
		
	}
		
	public static class ArcaneArrowListener implements Listener{
		
		@EventHandler
		public void onSwitch(TwohandedWeaponToggleEvent event) {
			if(!event.getMainHandItem().getType().equals(Material.BOW)) return;
			Skill skill = CombatPlayer.of(event.getPlayer()).getSkillEquip().getSkill(TriggerType.ATTACK_RANGED);
			if(skill == null || !(skill instanceof ArcaneArrows)) return;
			((ArcaneArrows) skill).cycleArrow();
		}
		
		@EventHandler
		public void onShoot(EntityShootBowEvent event) {
			if(!(event.getEntity() instanceof Player)) return;
			
			ArcaneArrow arrow = ((ArcaneArrows)CombatPlayer.of((Player) event.getEntity()).getSkillEquip().getSkillOfClass(ArcaneArrows.class)).getArrowToShoot();
			
			Entity arrowBukkitEntity = arrow.getBukkitEntity();
			arrowBukkitEntity.setVelocity(event.getProjectile().getVelocity());
			event.setProjectile(arrowBukkitEntity);
		}
		
	}
}