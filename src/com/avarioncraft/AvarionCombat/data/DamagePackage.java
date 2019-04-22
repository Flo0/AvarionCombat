package com.avarioncraft.AvarionCombat.data;

import java.util.Optional;

import javax.annotation.Nullable;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import com.avarioncraft.AvarionCombat.skills.Skill;
import com.avarioncraft.AvarionCombat.util.enums.CombatType;

import lombok.Getter;
import lombok.Setter;

public class DamagePackage {
	
	public DamagePackage(double damage, 
			boolean environmental, 
			boolean ranged, 
			boolean crit, 
			boolean magic, 
			@Nullable LivingEntity attacker, 
			LivingEntity defender, 
			@Nullable Skill inflictingSkill, 
			DamageCause cause) {
		this.time = System.currentTimeMillis();
		this.humanAttacker = attacker instanceof Player;
		this.humanDefender = defender instanceof Player;
		this.originalDamage = damage;
		this.damage = damage;
		this.attacker = Optional.ofNullable(attacker);
		this.defender = defender;
		this.environmental = environmental;
		this.ranged = ranged;
		this.crit = crit;
		this.magic = magic;
		this.cause = cause;
		
		if (this.getAttacker().isPresent()) {
			if(this.getAttacker().get().getType().equals(EntityType.PLAYER)) {
				this.setInflicingItem(((Player)this.getAttacker().get()).getInventory().getItemInMainHand());
			}
			this.combatType = CombatType.typeOf(this.getAttacker().get(), this.getDefender());
		} else {
			this.combatType = CombatType.typeOf(null, this.getDefender());
		}
		
	}
	
	@Getter
	private final long time;
	
	@Getter @Setter
	private double damage;
	@Getter @Setter
	private Optional<LivingEntity> attacker;
	@Getter @Setter
	private LivingEntity defender;
	@Getter @Setter
	private double originalDamage;
	
	@Getter @Setter
	private boolean environmental;
	@Getter @Setter
	private boolean ranged;
	@Getter @Setter
	private boolean crit;
	@Getter @Setter
	private boolean magic;
	@Getter @Setter
	private boolean humanAttacker;
	@Getter @Setter
	private boolean humanDefender;
	
	@Getter @Setter
	private Skill inflictingSkill = null;
	@Getter @Setter
	private ItemStack inflicingItem = null;
	@Getter
	private DamageCause cause;
	@Getter
	private final CombatType combatType;
	@Getter @Setter
	private double critChance;
	
	public void addDamage(double damage) {
		this.damage += damage;
	}
}
