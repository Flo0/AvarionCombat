package com.avarioncraft.AvarionCombat.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.avarioncraft.AvarionCombat.data.CombatPlayer;
import com.avarioncraft.AvarionCombat.util.enums.SkillTreeType;

import net.crytec.api.itemstack.ItemBuilder;
import net.crytec.api.smartInv.ClickableItem;
import net.crytec.api.smartInv.content.InventoryContents;
import net.crytec.api.smartInv.content.InventoryProvider;
import net.crytec.api.smartInv.content.SlotPos;

public class SkillTreeProtector implements InventoryProvider{

	@Override
	public void init(Player player, InventoryContents contents) {
		
		CombatPlayer.of(player).getSkillEquip().getSkillsForTree(SkillTreeType.PROTECTOR).stream().forEach(skill -> {
			contents.set(skill.getGuiPos(), skill.getGuiRepresenter());
		});
		
		contents.set(SlotPos.of(4, 8), ClickableItem.of(new ItemBuilder(Material.REDSTONE_BLOCK).name("§cZurück").build(), e -> {
			GUIs.S_SkillMainGUI.open(player);
		}));
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		
	}
}
