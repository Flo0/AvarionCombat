package com.avarioncraft.AvarionCombat.data;

import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import com.avarioncraft.AvarionCombat.actions.StatCalculator;
import com.avarioncraft.AvarionCombat.data.skillEquipment.SkillEquipment;
import com.avarioncraft.AvarionCombat.events.EnchantmentModifyEvent;
import com.avarioncraft.AvarionCombat.events.StatModifyEvent;
import com.avarioncraft.AvarionCombat.util.enums.CombatRelationship;
import com.avarioncraft.AvarionCombat.util.enums.EnchantmentStat;
import com.avarioncraft.AvarionCombat.util.enums.Stat;
import com.avarioncraft.AvarionCombat.util.enums.StatRegion;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import lombok.Getter;

public class CombatPlayer {
	
	//Constructor
	private CombatPlayer(Player player) {
		this.player = player;
		statMap.put(player, this);
		for(Stat stat : Stat.values()) {
			this.stats.put(stat, stat.getBaseValue());
		}
		for(EnchantmentStat ench : EnchantmentStat.values()) {
			this.enchantments.put(ench, ench.getBaseValue());
		}
		this.calc = new StatCalculator(player);
		for(StatRegion region : StatRegion.values()) {
			this.statRegions.put(region, new StatContainer());
		}
		
		
		this.skillEquip = new SkillEquipment(this);
		this.skillEquip.initSkills();
		this.levelSet = new LevelSet(this);
	}
	
	//Statics
	private static Map<Player, CombatPlayer> statMap = Maps.newHashMap();
	
	public static Set<CombatPlayer> getAll(){
		return Sets.newHashSet(statMap.values());
	}
	
	public static CombatPlayer of(Player player) {
		return statMap.get(player);
	}
	
	public static boolean init(Player player) {
		new CombatPlayer(player);
		return statMap.containsKey(player);
	}
	
	public static void logout(Player player) {
		statMap.remove(player);
	}
	
	//Variables
	private StatCalculator calc;
	
	@Getter
	private Player player;
	@Getter
	private final LevelSet levelSet;
	@Getter
	private final SkillEquipment skillEquip;
	
	private Map<StatRegion, StatContainer> statRegions = Maps.newHashMap();
	
	private Map<Stat, Double> stats = Maps.newHashMap();
	private Map<EnchantmentStat, Integer> enchantments = Maps.newHashMap();
	
	//Methods
	public StatContainer getRegionContainer(StatRegion region) {
		return this.statRegions.get(region);
	}
	
	//TODO erweitern
	public CombatRelationship getRelationShip(LivingEntity entity) {
		if(entity instanceof Monster) return CombatRelationship.HOSTILE;
		if(entity instanceof Mob) return CombatRelationship.NEUTRAL;
		return CombatRelationship.FRIENDLY;
	}
	
	public void updateSum() {
		//Stats nullen
		this.stats.forEach((stat,val)->{
			this.stats.put(stat, stat.getBaseValue());
		});
		this.enchantments.forEach((ench,val)->{
			this.enchantments.put(ench, ench.getBaseValue());
		});
		
		
		//Stats aus den einzelnen Regionen aufsummieren
		for(StatRegion region : StatRegion.values()) {
			for(Stat stat : Stat.values()) {
				this.stats.put(stat, this.statRegions.get(region).getStats().get(stat) + this.stats.get(stat));
			}
			for(EnchantmentStat ench : EnchantmentStat.values()) {
				this.enchantments.put(ench, this.statRegions.get(region).getEnchantments().get(ench) + this.enchantments.get(ench));
			}
		}
		
		this.player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(this.getStatValue(Stat.WEIGHT) / 1000);
		this.player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(this.getStatValue(Stat.HEALTH));
		player.setWalkSpeed((float) (0.2D / 100D * this.getStatValue(Stat.SPEED)));
	}
	
	public void addRawStats(RawStats raw, StatRegion region) {
		
		raw.getStats().forEach((stat, value)->{
//			System.out.println("+" + value + " " + stat.name());//TODO Debug entfernen
			this.addStatValue(stat, value, region);
		});
		
		raw.getEnchantments().forEach((ench, value)->{
			this.addEnchantmentValue(ench, value, region);
		});
		
	}
	
	public double getStatValue(Stat stat) {
		return stats.get(stat);
	}
	
	public void calculateFull() {
		this.calc.updateAll(this.player);
	}
	
	public void calculateArmor() {
		this.calc.updateArmor(this.player);
	}
	
	public void calculateHands() {
		this.calc.updateMainSlots(this.player);
	}
	
	public void calculateRpg() {
		this.calc.updateRPG(this.player);
	}
	
	public boolean addStatValue(Stat inStat, double inValue, StatRegion inRegion) {
		
		StatModifyEvent event = new StatModifyEvent(this.player, inStat, inValue, this.getStatValue(inStat), inRegion);
		Bukkit.getPluginManager().callEvent(event);
		
		if(event.isCancelled()) return false;
		
		double value = event.getValue();
		Stat stat = event.getStat();
		
//		System.out.println("AddStat after event: +" + value + " " + stat.name());//TODO Debug entfernen
		
		if(stat.getMaxValue() < value + this.getStatValue(stat)) return false;
		
		this.statRegions.get(event.getRegion()).getStats().merge(stat, value, Double::sum);
		
		return true;
	}
	
	public int getEnchantmentValue(EnchantmentStat ench) {
		return this.enchantments.get(ench);
	}

	public boolean addEnchantmentValue(EnchantmentStat inEnch, int inValue, StatRegion inRegion) {
		
		EnchantmentModifyEvent event = new EnchantmentModifyEvent(this.player, inEnch, inValue, inRegion);
		Bukkit.getPluginManager().callEvent(event);
		
		if(event.isCancelled()) return false;
		
		EnchantmentStat ench = event.getStat();
		int value = event.getValue();
		
		if(this.enchantments.get(ench) + value > 40) return false;
		
		this.statRegions.get(event.getRegion()).getEnchantments().put(ench, this.enchantments.get(ench) + value);

		return true;
	}
	
}
