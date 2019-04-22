package com.avarioncraft.AvarionCombat.gui;

import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import com.avarioncraft.AvarionCombat.buffs.CombatBuff;
import com.google.common.collect.Sets;

import net.crytec.api.itemstack.ItemBuilder;
import net.crytec.api.smartInv.ClickableItem;
import net.crytec.api.smartInv.content.InventoryContents;
import net.crytec.api.smartInv.content.InventoryProvider;

public class CombatBuffGUI implements InventoryProvider{

	@Override
	public void init(Player player, InventoryContents contents) {
		
		Set<CombatBuff> buffs = CombatBuff.combatBuffs.get(player);
		Set<ItemStack> stacks = Sets.newHashSet();
		
		for(CombatBuff buff : buffs) {
			stacks.add(new ItemBuilder(buff.getIcon())
					.name(buff.getDisplayName())
					.lore(buff.getDescription())
					.setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
					.build());
		}
		
		for(ItemStack item : stacks) {
			contents.add(ClickableItem.empty(item));
		}
		
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		
	}

}
