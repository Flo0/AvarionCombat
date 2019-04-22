package com.avarioncraft.AvarionCombat.buffs.statBuffs;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.avarioncraft.AvarionCombat.buffs.StatBuff;
import com.avarioncraft.AvarionCombat.events.StatModifyEvent;
import com.avarioncraft.AvarionCombat.util.enums.Stat;

public class StartBuff extends StatBuff{

	public StartBuff(Player player) {
		super(player, 120000);
		super.setDisplayName("§fStarter Buff");
		super.setIconMaterial(Material.IRON_AXE);
		super.addDescLine("");
		super.addDescLine("§fDieser Buff erhöht deinen normalen");
		super.addDescLine("§fSchaden für §e120 Sekunden §fum §e100%");
	}

	@Override
	public void onModify(StatModifyEvent event) {
		
		if(event.getStat().equals(Stat.MEELE_DAMAGE)) {
			event.setValue(event.getValue() * 2);
		}
		
	}

}
