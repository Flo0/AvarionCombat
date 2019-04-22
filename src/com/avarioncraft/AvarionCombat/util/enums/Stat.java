package com.avarioncraft.AvarionCombat.util.enums;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import com.google.common.collect.Lists;

import lombok.Getter;

public enum Stat {
	
	MEELE_DAMAGE("Nahkampf Schaden", 2.0D, -1.0D, Material.IRON_SWORD, new String[] {"§fDieser Stat beeinflusst den Schaden, welchen", "§fdu mit deinen normalen Angriffen verursachst."}, false),
	RANGED_DAMAGE("Fernkampf Schaden", 2.0D, -1.0D, Material.BOW, new String[] {"§fDieser Stat beeinflusst den Schaden, welchen", "§fdu mit Projektilen verursachst."}, false),
	MAGIC_POWER("Macht", 0.0D, -1.0D, Material.ENCHANTED_BOOK, new String[] {"§fDieser Stat dient zu der Berechnung von magischem Schaden", "§fdurch Fähigkeiten von Waffen."}, false),
	ARMOR("Rüstung", 0.0D, -1.0D, Material.IRON_CHESTPLATE, new String[] {"§fDieser Stat verringert den eingehenden Schaden."}, false),
	ARMOR_PENETRATION("Rüstungsdurchdringung", 0.0D, 100.0D, Material.IRON_PICKAXE, new String[] {"§fDieser Stat lässt dich einen Teil der gegnerischen", "§fRüstung durchdringen."}, true),
	CRIT_CHANCE("Kritische Trefferchance", 0.0D, 100.0D, Material.IRON_HOE, new String[] {"§fDieser Stat zeigt an, wie hoch die Wahrscheinlichkeit", "§fauf einen kritischen Treffer ist."}, true),
	CRIT_DAMAGE("Kritischer Zusatzschaden", 50.0D, -1.0D, Material.IRON_AXE, new String[] {"§fDieser Stat zeigt an um wie viel mehr Schaden du", "§fbei einem kritischen Treffer verursachst."}, true),
	BLOCK_CHANCE("Block Chance", 0.0D, 100.0D, Material.SHIELD, new String[] {"§fDieser Stat zeigt dir die Chance an, mit der du", "§ferfolgreich blocken kannst.", "§fGeblockte Schläge verursachen halben Schaden."}, true),
	WEIGHT("Gewicht", 0.0D, 1000.0D, Material.FEATHER, new String[] {"§fDas Gewicht wirkt sich negativ auf", "gesprochene Zauber aus aber", "erhöht die Rückstoßresistenz."}, false),
	HEALTH("Lebenskraft", 100.0D, -1.0D, Material.ROSE_RED, new String[] {"§fErhöht die Lebenspunkte."}, false),
	FAITH("Glaube", 0.0D, -1.0D, Material.NETHER_STAR, new String[] {"§f-desc"}, false),
	SPEED("Lauftempo", 100.0D, 500.0D, Material.LEATHER_BOOTS, new String[] {"§fErhöht dein Lauftempo."}, false);
	
	@Getter
	private String displayName;
	@Getter
	private double baseValue;
	@Getter
	private Material icon;
	@Getter
	private List<String> description = Lists.newArrayList();
	@Getter
	private boolean percentile;
	
	private double maxValue;
	
	public double getMaxValue() {
		if(this.maxValue < 0) return Double.MAX_VALUE;
		return this.maxValue;
	}
	
	private Stat(String displayName, double baseValue, double maxValue, Material icon, String[] desclines, boolean percentile) {
		this.displayName = displayName;
		this.baseValue = baseValue;
		this.maxValue = maxValue;
		this.icon = icon;
		this.description = Arrays.asList(desclines);
		this.percentile = percentile;
	}
}
