package com.avarioncraft.AvarionCombat.gui;

import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.avarioncraft.AvarionCombat.data.CombatPlayer;
import com.avarioncraft.AvarionCombat.skills.Skill;
import com.avarioncraft.AvarionCombat.skills.TriggerType;

import net.crytec.api.itemstack.ItemBuilder;
import net.crytec.api.smartInv.ClickableItem;
import net.crytec.api.smartInv.content.InventoryContents;
import net.crytec.api.smartInv.content.InventoryProvider;
import net.crytec.api.smartInv.content.SlotPos;

public class EquippedSkillsGui implements InventoryProvider{
	
	private Consumer<InventoryClickEvent> getConsumerFor(TriggerType type){
		return new Consumer<InventoryClickEvent>() {

			@Override
			public void accept(InventoryClickEvent event) {
				
				switch(type) {
				case ATTACK_MEELE: GUIs.S_SkillChooser_ATTACK_MEELE.open((Player)event.getWhoClicked());break;
				case ATTACK_SKILL: GUIs.S_SkillChooser_ATTACK_SKILL.open((Player)event.getWhoClicked());break;
				case ATTACK_RANGED: GUIs.S_SkillChooser_ATTACK_RANGED.open((Player)event.getWhoClicked());break;
				case BLOCK_BREAK: GUIs.S_SkillChooser_BLOCK_BREAK.open((Player)event.getWhoClicked());break;
				case CAST_OTHER_SKILL: GUIs.S_SkillChooser_CAST_OTHER_SKILL.open((Player)event.getWhoClicked());break;
				case DEATH: GUIs.S_SkillChooser_DEATH.open((Player)event.getWhoClicked());break;
				case DEFEND_MAGIC: GUIs.S_SkillChooser_DEFEND_MAGIC.open((Player)event.getWhoClicked());break;
				case DEFEND_MEELE: GUIs.S_SkillChooser_DEFEND_MEELE.open((Player)event.getWhoClicked());break;
				case DEFEND_RANGED: GUIs.S_SkillChooser_DEFEND_RANGED.open((Player)event.getWhoClicked());break;
				case PASSIVE: GUIs.S_SkillChooser_PASSIVE.open((Player)event.getWhoClicked());break;
				case SNEAK_CAST: GUIs.S_SkillChooser_SNEAK_CAST.open((Player)event.getWhoClicked());break;
				case SPELL_CAST: GUIs.S_SkillChooser_SPELL_CAST.open((Player)event.getWhoClicked());break;
				case SWING: GUIs.S_SkillChooser_SWING.open((Player)event.getWhoClicked());break;
				case ENHANCING:
					break;
				default:
					break;
				}
				
			}
		};
	}
	
	private ClickableItem getEmptyIcon(TriggerType type) {
		return ClickableItem.of(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)
				.name("§fSlot für Auslöser: §6" + type.getDisplayName())
				.build(), this.getConsumerFor(type));
	}
	
	private ClickableItem getCurrentIcon(TriggerType type, Map<TriggerType, Skill> equippedSkills) {
		return (equippedSkills.get(type) == null) ? this.getEmptyIcon(type) : ClickableItem.of(equippedSkills.get(type).getTriggerGuiRepresenter(), this.getConsumerFor(type));
	}
	
	@Override
	public void init(Player player, InventoryContents contents) {
		
		Map<TriggerType, Skill> equippedSkills = CombatPlayer.of(player).getSkillEquip().getPlayerSkills();
		
		contents.set(SlotPos.of(0, 4), this.getCurrentIcon(TriggerType.PASSIVE, equippedSkills));
		
		contents.set(SlotPos.of(1, 3), this.getCurrentIcon(TriggerType.SNEAK_CAST, equippedSkills));
		
		contents.set(SlotPos.of(1, 5), this.getCurrentIcon(TriggerType.SPELL_CAST, equippedSkills));
		
		contents.set(SlotPos.of(2, 2), this.getCurrentIcon(TriggerType.DEFEND_MEELE, equippedSkills));
		
		contents.set(SlotPos.of(2, 4), this.getCurrentIcon(TriggerType.DEFEND_RANGED, equippedSkills));
		
		contents.set(SlotPos.of(2, 6), this.getCurrentIcon(TriggerType.DEFEND_MAGIC, equippedSkills));
		
		contents.set(SlotPos.of(3, 1), this.getCurrentIcon(TriggerType.ATTACK_MEELE, equippedSkills));
		
		contents.set(SlotPos.of(3, 3), this.getCurrentIcon(TriggerType.SWING, equippedSkills));
		
		contents.set(SlotPos.of(3, 5), this.getCurrentIcon(TriggerType.ATTACK_RANGED, equippedSkills));
		
		contents.set(SlotPos.of(3, 7), this.getCurrentIcon(TriggerType.ATTACK_SKILL, equippedSkills));
		
		contents.set(SlotPos.of(4, 2), this.getCurrentIcon(TriggerType.CAST_OTHER_SKILL, equippedSkills));
		
		contents.set(SlotPos.of(4, 4), this.getCurrentIcon(TriggerType.DEATH, equippedSkills));
		
		contents.set(SlotPos.of(4, 6), this.getCurrentIcon(TriggerType.BLOCK_BREAK, equippedSkills));
		
		contents.set(SlotPos.of(4, 8), ClickableItem.of(new ItemBuilder(Material.REDSTONE_BLOCK).name("§cZurück").build(), e -> {
			GUIs.S_SkillMainGUI.open(player);
		}));
		
	}
	
	@Override
	public void update(Player player, InventoryContents contents) {}
	
}
