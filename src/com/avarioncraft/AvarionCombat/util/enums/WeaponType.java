package com.avarioncraft.AvarionCombat.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum WeaponType {
	
	DAGGER(false, "Dolch", "DAGGER"),
	SHORT_SWORD(false, "Kurzschwert", "SHORT_SWORD"),
	LONG_SWORD(false, "Langschwert", "LONG_SWORD"),
	CLAYMORE(true, "Zweihänder", "CLAYMORE"),
	AXE(false, "Axt", "AXE"),
	BATTLE_AXE(true, "Kriegsaxt", "BATTLE_AXE"),
	LANCE(false, "Lanze", "LANCE"),
	SPEAR(false, "Speer", "SPEAR"),
	MACE(false, "Morgenstern", "MACE"),
	BOW(true, "Bogen", "BOW"),
	COMPOUND_BOW(true, "Kompositbogen", "COMPOUND_BOW"),
	STAFF(true, "Zauberstab", "Staff");
	
	@Getter
	private boolean twoHanded;
	@Getter
	private String displayName;
	@Getter
	private String nbtTag;
	
	public static final String NBT_KEY = "WeaponType";
}