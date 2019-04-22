package com.avarioncraft.AvarionCombat.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import com.avarioncraft.AvarionCombat.data.CombatPlayer;
import com.avarioncraft.AvarionCombat.util.enums.EnchantmentStat;
import com.avarioncraft.AvarionCombat.util.enums.Stat;

import net.crytec.api.itemstack.ItemBuilder;
import net.crytec.api.smartInv.ClickableItem;
import net.crytec.api.smartInv.content.InventoryContents;
import net.crytec.api.smartInv.content.InventoryProvider;
import net.crytec.api.smartInv.content.SlotPos;

public class CombatMain implements InventoryProvider{

	@Override
	public void init(Player player, InventoryContents contents) {
		
		int row = 1;
		int column = -1;
		
		CombatPlayer cStats = CombatPlayer.of(player);
		
		String perc;
		
		for(Stat stat : Stat.values()) {
			
			perc = stat.isPercentile() ? "%" : "";
			
			column++;
			
			if(column == 9) {
				row++;
				column = 0;
			}
			
			contents.set(SlotPos.of(row, column),ClickableItem.empty(new ItemBuilder(stat.getIcon())
					.name("§6" + stat.getDisplayName() + " §e" + cStats.getStatValue(stat) + perc)
					.lore("")
					.lore(stat.getDescription())
					.setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
					.build()));
			
		}
		
		row = 3;
		column = -1;
		
		for(EnchantmentStat ench : EnchantmentStat.values()) {
			column++;
			if(column == 10) {
				column = 0;
				row++;
			}
			
			int amount = 1;
			
			if (cStats.getEnchantmentValue(ench) > 1) {
				amount = cStats.getEnchantmentValue(ench);
			}
			
			contents.set(SlotPos.of(row, column),ClickableItem.empty(new ItemBuilder(ench.getIcon())
					.name("§6" + ench.getDisplayName() + " §e" + cStats.getEnchantmentValue(ench) + " / " + ench.getMaxValue())
					.amount(amount)
					.lore("")
					.lore(ench.getDescription())
					.setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
					.build()));
			
		}
		
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		
	}

}
