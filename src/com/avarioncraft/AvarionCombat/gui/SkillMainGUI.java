package com.avarioncraft.AvarionCombat.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.avarioncraft.AvarionCombat.util.enums.SkillTreeType;

import net.crytec.api.itemstack.ItemBuilder;
import net.crytec.api.smartInv.ClickableItem;
import net.crytec.api.smartInv.content.InventoryContents;
import net.crytec.api.smartInv.content.InventoryProvider;
import net.crytec.api.smartInv.content.SlotPos;

public class SkillMainGUI implements InventoryProvider{
	
	private static final int TREE_ROW = 2;
	
	@Override
	public void init(Player player, InventoryContents contents) {
		
		for(SkillTreeType type : SkillTreeType.values()) {
			contents.set(SlotPos.of(TREE_ROW, 0 + (2 * type.ordinal())), type.getSkillTreeClickable());
		}
		
		contents.set(SlotPos.of(0, 4), ClickableItem.of(new ItemBuilder(Material.ARMOR_STAND).name("§6Skills anlegen").build(), event-> {
			GUIs.S_EquippedSkillsGUI.open((Player) event.getWhoClicked());
		}));
		
	}

	@Override
	public void update(Player player, InventoryContents contents) {}

}
