package com.avarioncraft.AvarionCombat.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.avarioncraft.AvarionCombat.actions.CombatCooldown;
import com.avarioncraft.AvarionCombat.data.DamagePackage;
import com.avarioncraft.AvarionCombat.events.AvarionCombatEvent;
import com.avarioncraft.AvarionCombat.events.SkillDamageEvent;
import com.avarioncraft.AvarionCombat.handler.DamagePackageHandler;

public class CombatListener implements Listener {
	
	@EventHandler
	public void damageListener(EntityDamageEvent event) {
		
		float multi = 1.0F;
		DamagePackage damagePackage;
		boolean ranged = false;
		boolean magic = false;
		LivingEntity attacker = null;
		LivingEntity defender;
		
		if(!(event.getEntity() instanceof LivingEntity)) return;
		
		defender = (LivingEntity) event.getEntity();
		
		if(event.getCause().equals(DamageCause.MAGIC)) magic = true;
		
		if(event instanceof EntityDamageByEntityEvent) {
			
			EntityDamageByEntityEvent eeEvent = (EntityDamageByEntityEvent) event;
			
			if(eeEvent.getDamager() instanceof Projectile) {
				
				ranged = true;
				
				Projectile pro = (Projectile) eeEvent.getDamager();
				if(pro.getShooter() instanceof LivingEntity) {
					attacker = (LivingEntity) pro.getShooter();
				}else {
					//TODO dispenser?
					return;
				}
			}else {
				if(!(eeEvent.getDamager() instanceof LivingEntity)) return;
				attacker = (LivingEntity) eeEvent.getDamager();
				if(attacker instanceof Player) multi = CombatCooldown.of((Player) attacker);
			}
			
			damagePackage = new DamagePackage(event.getDamage(), false, ranged, false, magic, attacker, defender, null, event.getCause());
			
		}else {
			
			damagePackage = new DamagePackage(event.getDamage(), true, false, false, magic, null, defender, null, event.getCause());
			
		}
		
		if(event instanceof SkillDamageEvent) {
			multi = 1F;
			damagePackage.setInflictingSkill(((SkillDamageEvent)event).getSkill());
		}
		
		damagePackage = DamagePackageHandler.handlePackage(damagePackage);
		
		damagePackage.setDamage(damagePackage.getDamage() * multi);
		
		AvarionCombatEvent combatEvent = new AvarionCombatEvent(damagePackage);
		Bukkit.getPluginManager().callEvent(combatEvent);
		
		event.setDamage(0);
		
		//TODO remove debug
		if(attacker != null && attacker instanceof Player) {
			attacker.sendMessage("§fDone §e" + damagePackage.getDamage() + " §fDamage. Type: §e" + damagePackage.getCause().toString());
		}
		
		if(!combatEvent.isCancelled()) {
			if (defender.getHealth() - damagePackage.getDamage() <= 0) {
				defender.setHealth(0);
				event.setCancelled(true);
			} else {
				defender.setHealth(defender.getHealth() - damagePackage.getDamage());
			}
		}
		
	}
	
}
