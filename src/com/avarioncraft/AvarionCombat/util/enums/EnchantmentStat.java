package com.avarioncraft.AvarionCombat.util.enums;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import com.google.common.collect.Lists;

import lombok.Getter;

public enum EnchantmentStat{
	
	ARROW_DAMAGE("Pfeilschaden", Material.FLINT, new String[] {"", ""}),
	ARROW_KNOCKBACK("Pfeilwucht", Material.STICK, new String[] {"", ""}),
	DAMAGE_ALL("Nahkampfschaden", Material.IRON_SWORD, new String[] {"", ""}),
	DAMAGE_ARTHROPODS("Insektengift", Material.SCUTE, new String[] {"", ""}),
	DAMAGE_UNDEAD("Geweiht", Material.NETHER_STAR, new String[] {"", ""}),
 	IMPALING("Wassergift", Material.HEART_OF_THE_SEA, new String[] {"", ""}),
 	KNOCKBACK("Rückschlag", Material.BONE, new String[] {"", ""}),
 	LOOT_BONUS_MOBS("Finderglück", Material.RABBIT_FOOT, new String[] {"", ""}),
 	PROTECTION_ENVIRONMENTAL("Schutz", Material.SHIELD, new String[] {"", ""}),
 	PROTECTION_EXPLOSIONS("Explosionsschutz", Material.TNT, new String[] {"", ""}),
 	PROTECTION_FALL("Fallschutz", Material.FEATHER, new String[] {"", ""}),
 	PROTECTION_FIRE("Feuerschutz", Material.BLAZE_POWDER, new String[] {"", ""}),
 	PROTECTION_PROJECTILE("Projektilschutz", Material.TURTLE_HELMET, new String[] {"", ""}),
 	SWEEPING_EDGE("Schwungschlag", Material.SHEARS, new String[] {"", ""}),
 	THORNS("Dornenpanzer", Material.PRISMARINE_SHARD, new String[] {"", ""});
	
	@Getter
	private String displayName;
	@Getter
	private int baseValue = 0;
	@Getter
	private int maxValue = 40;
	@Getter
	private Material icon;
	@Getter
	private List<String> description = Lists.newArrayList();
	
	private EnchantmentStat(String displayName, Material icon, String[] desclines) {
		this.displayName = displayName;
		this.icon = icon;
		this.description = Arrays.asList(desclines);
	}

}
