package com.avarioncraft.AvarionCombat.handler;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Wither;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffectType;

import com.avarioncraft.AvarionCombat.data.CombatPlayer;
import com.avarioncraft.AvarionCombat.data.DamagePackage;
import com.avarioncraft.AvarionCombat.util.enums.EnchantmentStat;
import com.avarioncraft.AvarionCombat.util.enums.Stat;

import io.netty.util.internal.ThreadLocalRandom;

public class DamagePackageHandler {
	
	public static DamagePackage handlePackage(DamagePackage pack) {
		
		switch(pack.getCombatType()) {
		case ENV: 
			reduceDamage(pack);
			break;
		case PVE:
			generateDamage(pack);
			reduceDamage(pack);
			break;
		case PVP: 
			generateDamage(pack);
			reduceDamage(pack);
			break;
		case EVP: 
			reduceDamage(pack);
			break;
		case EVE: 
			reduceDamage(pack);
			break;
		}
		
		return pack;
	}
	
    private static void generateDamage(DamagePackage pack) {
    	
    	boolean ranged = pack.isRanged();
    	boolean magic = pack.isMagic();
    	DamageCause cause = pack.getCause();
    	
    	Player player = (Player) pack.getAttacker().get();
        CombatPlayer stats = CombatPlayer.of(player);
        
        if(!magic) {
        	if (ranged) {
                pack.setDamage(stats.getStatValue(Stat.RANGED_DAMAGE));
                pack.setDamage(pack.getDamage() + pack.getDamage() / 40.0 * (double)stats.getEnchantmentValue(EnchantmentStat.ARROW_DAMAGE));
                pack.setDamage(pack.getDamage() + pack.getDamage() / 40.0 * (double)stats.getEnchantmentValue(EnchantmentStat.IMPALING));
            } else {
                pack.setDamage(stats.getStatValue(Stat.MEELE_DAMAGE) * ((cause.equals(DamageCause.ENTITY_SWEEP_ATTACK)) ? 0.25D : 1.0D));
                pack.setDamage(pack.getDamage() + pack.getDamage() / 40.0 * (double)stats.getEnchantmentValue(EnchantmentStat.DAMAGE_ALL));
                pack.setDamage(pack.getDamage() + pack.getDamage() / 60.0 * (double)stats.getEnchantmentValue(EnchantmentStat.IMPALING));
            }
        }
        
        double chance = stats.getStatValue(Stat.CRIT_CHANCE);
        
        if (player.getPotionEffect(PotionEffectType.UNLUCK) != null) {
            chance -= (double)(player.getPotionEffect(PotionEffectType.UNLUCK).getAmplifier() * 10);
        }
        if (player.getPotionEffect(PotionEffectType.LUCK) != null) {
            chance += chance / 100.0 * (double)(player.getPotionEffect(PotionEffectType.LUCK).getAmplifier() * 10);
        }
        if (player.getPotionEffect(PotionEffectType.INCREASE_DAMAGE) != null) {
            pack.addDamage(pack.getDamage() / 100.0 * (double)(player.getPotionEffect(PotionEffectType.INCREASE_DAMAGE).getAmplifier() * 10));
        }
        if (player.getPotionEffect(PotionEffectType.WEAKNESS) != null) {
            pack.addDamage(pack.getDamage() / 100.0 * (double)(player.getPotionEffect(PotionEffectType.WEAKNESS).getAmplifier() * -10));
        }
        
        pack.setCritChance(chance);
        
        if(pack.getInflictingSkill() != null) {
        	pack.getInflictingSkill().modifyDamageGeneration(pack);
        }
        
        chance = pack.getCritChance();
        
        if (chance <= ThreadLocalRandom.current().nextDouble(100.1)) return;
        
        pack.addDamage(pack.getDamage() / 100.0 * stats.getStatValue(Stat.CRIT_DAMAGE));
        pack.setCrit(true);
        
        return;
    }
	
	private static void reduceDamage(DamagePackage pack) {
		
		switch(pack.getCause()) {
		case BLOCK_EXPLOSION: explosionHandler(pack);
			break;
		case CONTACT: physicHandler(pack);
			break;
		case CRAMMING: physicHandler(pack);
			break;
		case CUSTOM: magicHandler(pack);
			break;
		case DRAGON_BREATH: fireHandler(pack);
			break;
		case DROWNING: bioHandler(pack);
			break;
		case ENTITY_ATTACK: fightHandler(pack);
			break;
		case ENTITY_EXPLOSION: explosionHandler(pack);
			break;
		case ENTITY_SWEEP_ATTACK: fightHandler(pack);
			break;
		case FALL: physicHandler(pack);
			break;
		case FALLING_BLOCK: physicHandler(pack);
			break;
		case FIRE: fireHandler(pack);
			break;
		case FIRE_TICK: fireHandler(pack);
			break;
		case FLY_INTO_WALL: physicHandler(pack);
			break;
		case HOT_FLOOR: fireHandler(pack);
			break;
		case LAVA: fireHandler(pack);
			break;
		case LIGHTNING: fireHandler(pack);
			break;
		case MAGIC: magicHandler(pack);
			break;
		case POISON: bioHandler(pack);
			break;
		case PROJECTILE: fightHandler(pack);
			break;
		case STARVATION: bioHandler(pack);
			break;
		case SUFFOCATION: bioHandler(pack);
			break;
		case SUICIDE: instantDeathHandler(pack);
			break;
		case THORNS: magicHandler(pack);
			break;
		case VOID: instantDeathHandler(pack);
			break;
		case WITHER: magicHandler(pack);
			break;
		default:
			break;
		
		}
		
	}
	
	private static DamagePackage bioHandler(DamagePackage pack) {
        return finalReducer(pack);
    }
	
    private static DamagePackage fightHandler(DamagePackage pack) {
        if (pack.isHumanDefender()) {
            CombatPlayer stats = CombatPlayer.of((Player)pack.getDefender());
            pack.setDamage(pack.getDamage() * (2000.0 / (2000.0 + stats.getStatValue(Stat.ARMOR))));
            if (pack.isRanged()) {
                pack.setDamage(pack.getDamage() * (20.0 / (20.0 + (double)stats.getEnchantmentValue(EnchantmentStat.PROTECTION_PROJECTILE))));
                return finalReducer(pack);
            }
            pack.setDamage(pack.getDamage() * (20.0 / (20.0 + (double)stats.getEnchantmentValue(EnchantmentStat.PROTECTION_ENVIRONMENTAL))));
            return finalReducer(pack);
        }
        if (!pack.isHumanAttacker()) {
            return finalReducer(pack);
        }
        LivingEntity target = pack.getDefender();
        if (target instanceof Zombie || target instanceof Skeleton || target instanceof Wither || target instanceof Phantom) {
            pack.setDamage(pack.getDamage() + pack.getDamage() / 40.0 * (double)CombatPlayer.of((Player)pack.getAttacker().get()).getEnchantmentValue(EnchantmentStat.DAMAGE_UNDEAD));
            return finalReducer(pack);
        }
        if (target instanceof Spider || target instanceof Silverfish || target instanceof Endermite) {
        	pack.setDamage(pack.getDamage() + pack.getDamage() / 40.0 * (double)CombatPlayer.of((Player)pack.getAttacker().get()).getEnchantmentValue(EnchantmentStat.DAMAGE_ARTHROPODS));
            return finalReducer(pack);
        }
        return finalReducer(pack);
    }
	
    private static DamagePackage fireHandler(DamagePackage pack) {
        if (!pack.isHumanDefender()) return finalReducer(pack);
        CombatPlayer stats = CombatPlayer.of((Player)((Player)pack.getDefender()));
        pack.setDamage(pack.getDamage() * (2000.0 / (2000.0 + stats.getStatValue(Stat.ARMOR) * 0.25)));
        pack.setDamage(pack.getDamage() * (20.0 / (20.0 + (double)stats.getEnchantmentValue(EnchantmentStat.PROTECTION_FIRE))));
        return finalReducer(pack);
    }
	
	private static DamagePackage instantDeathHandler(DamagePackage pack) {
        pack.setDamage(pack.getAttacker().get().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + 0.1);
        return pack;
    }
	
    private static DamagePackage physicHandler(DamagePackage pack) {
        if (!pack.isHumanDefender()) return finalReducer(pack);
        CombatPlayer stats = CombatPlayer.of((Player)pack.getDefender());
        pack.setDamage(pack.getDamage() * (2000.0 / (2000.0 + stats.getStatValue(Stat.ARMOR) * 0.8)));
        pack.setDamage(pack.getDamage() * (20.0 / (20.0 + (double)stats.getEnchantmentValue(EnchantmentStat.PROTECTION_FALL))));
        return finalReducer(pack);
    }
	
	private static DamagePackage explosionHandler(DamagePackage pack) {
        if (!pack.isHumanDefender()) return finalReducer(pack);
        CombatPlayer stats = CombatPlayer.of((Player)pack.getDefender());
        pack.setDamage(pack.getDamage() * (2000.0 / (2000.0 + stats.getStatValue(Stat.ARMOR) * 0.25)));
        pack.setDamage(pack.getDamage() * (20.0 / (20.0 + (double)stats.getEnchantmentValue(EnchantmentStat.PROTECTION_ENVIRONMENTAL) * 0.25)));
        pack.setDamage(pack.getDamage() * (20.0 / (20.0 + (double)stats.getEnchantmentValue(EnchantmentStat.PROTECTION_EXPLOSIONS) * 2.0)));
        return finalReducer(pack);
    }
	
	private static DamagePackage magicHandler(DamagePackage pack) {
        if (!pack.isHumanDefender()) return finalReducer(pack);
        CombatPlayer stats = CombatPlayer.of((Player)pack.getDefender());
        pack.setDamage(pack.getDamage() * (2000.0 / (2000.0 + stats.getStatValue(Stat.ARMOR) * 0.5)));
        pack.setDamage(pack.getDamage() * (20.0 / (20.0 + (double)stats.getEnchantmentValue(EnchantmentStat.PROTECTION_ENVIRONMENTAL) * 2.0)));
        return finalReducer(pack);
    }
	
	private static DamagePackage finalReducer(DamagePackage pack) {
        if (((LivingEntity)pack.getDefender()).getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE) == null) return pack;
        pack.setDamage(pack.getDamage() - pack.getDamage() / 10.0 * (double)((LivingEntity)pack.getDefender()).getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getAmplifier());
        return pack;
    }
	
}
