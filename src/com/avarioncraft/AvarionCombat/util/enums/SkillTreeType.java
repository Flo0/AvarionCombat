package com.avarioncraft.AvarionCombat.util.enums;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import com.avarioncraft.AvarionCombat.gui.SkillTreeAggressor;
import com.avarioncraft.AvarionCombat.gui.SkillTreeAuxilior;
import com.avarioncraft.AvarionCombat.gui.SkillTreeImperior;
import com.avarioncraft.AvarionCombat.gui.SkillTreeProtector;
import com.avarioncraft.AvarionCombat.gui.SkillTreeVescerator;
import net.crytec.api.itemstack.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.crytec.api.smartInv.ClickableItem;
import net.crytec.api.smartInv.SmartInventory;

@AllArgsConstructor
public enum SkillTreeType {
	
	PROTECTOR("Protector", SmartInventory.builder().provider(new SkillTreeProtector()).title("Protector - Skills").size(5, 9).build(), new ItemBuilder(Material.IRON_CHESTPLATE).name("§6Protector - Skills").setItemFlag(ItemFlag.HIDE_ATTRIBUTES).build()),
	AGGRESSOR("Aggressor", SmartInventory.builder().provider(new SkillTreeAggressor()).title("Aggresor - Skills").size(5, 9).build(), new ItemBuilder(Material.IRON_SWORD).name("§6Aggressor - Skills").setItemFlag(ItemFlag.HIDE_ATTRIBUTES).build()),
	IMPERIOR("Imperior", SmartInventory.builder().provider(new SkillTreeImperior()).title("Imperior - Skills").size(5, 9).build(), new ItemBuilder(Material.DIAMOND).name("§6Imperior - Skills").setItemFlag(ItemFlag.HIDE_ATTRIBUTES).build()),
	AUXILIOR("Auxilior", SmartInventory.builder().provider(new SkillTreeAuxilior()).title("Auxilior - Skills").size(5, 9).build(), new ItemBuilder(Material.EMERALD).name("§6Auxilior - Skills").setItemFlag(ItemFlag.HIDE_ATTRIBUTES).build()),
	VESCERATOR("Vescerator", SmartInventory.builder().provider(new SkillTreeVescerator()).title("Vescerator - Skills").size(5, 9).build(), new ItemBuilder(Material.BOW).name("§6Vescerator - Skills").setItemFlag(ItemFlag.HIDE_ATTRIBUTES).build()),
	NONE("null", null, new ItemStack(Material.STICK));
	
	@Getter
	private String displayName;
	@Getter
	private SmartInventory inventoryGUI;
	private ItemStack icon;
	
	public ClickableItem getSkillTreeClickable() {
		return ClickableItem.of(this.icon, event -> {
			this.inventoryGUI.open((Player) event.getWhoClicked());
		});
	}
}
