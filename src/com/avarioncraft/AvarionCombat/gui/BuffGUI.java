package com.avarioncraft.AvarionCombat.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import net.crytec.api.itemstack.ItemBuilder;
import net.crytec.api.smartInv.ClickableItem;
import net.crytec.api.smartInv.content.InventoryContents;
import net.crytec.api.smartInv.content.InventoryProvider;
import net.crytec.api.smartInv.content.SlotPos;

public class BuffGUI implements InventoryProvider{

	@Override
	public void init(Player player, InventoryContents contents) {
		
		contents.set(SlotPos.of(1, 2), ClickableItem.of(new ItemBuilder(Material.DIAMOND)
				.name("§eStat Buffs")
				.lore("")
				.lore("§fZeigt dir deine aktiven Buffs an, welche")
				.lore("§fdeine Kampfstats verändern können.")
				.build(), event->{
					GUIs.S_StatBuffGUI.open(player);
				}));
		
		contents.set(SlotPos.of(1, 6), ClickableItem.of(new ItemBuilder(Material.EMERALD)
				.name("§eKampf Buffs")
				.lore("")
				.lore("§fZeigt dir deine aktiven Buffs an, welche")
				.lore("§fden Kampf direkt verändern können.")
				.build(), event ->{
					GUIs.S_CombatBuffGUI.open(player);
				}));
		
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		
	}

}
