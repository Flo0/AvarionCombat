package com.avarioncraft.AvarionCombat.gui;

import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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

public class SkillChooserGUI implements InventoryProvider{
	
	public SkillChooserGUI(TriggerType type) {
		this.type = type;
	}
	
	private final TriggerType type;
	private PriorityQueue<Skill> skills;
	
	private Consumer<InventoryClickEvent> getConsumer(CombatPlayer cPlayer, Skill skill){
		return new Consumer<InventoryClickEvent>() {
			
			@Override
			public void accept(InventoryClickEvent event) {
				
				cPlayer.getSkillEquip().addSkill(skill);
				
				GUIs.S_EquippedSkillsGUI.open(cPlayer.getPlayer());
				
			}
		};
	}
	
	@Override
	public void init(Player player, InventoryContents contents) {
		CombatPlayer cPlayer = CombatPlayer.of(player);
		Set<Skill> triggerSkills = cPlayer
				.getSkillEquip()
				.getSkillSet()
				.values()
				.stream()
				.filter(skill -> skill != null)
				.filter(skill -> skill.getLevel() >= 1)
				.filter(skill -> skill.getTriggerType().equals(this.type))
				.collect(Collectors.toSet());
		
		if(!triggerSkills.isEmpty()) {
			
			this.skills = new PriorityQueue<Skill>(triggerSkills.size(), (a,b) -> a.getLevel() - b.getLevel());
			this.skills.addAll(triggerSkills);
			
			while(!this.skills.isEmpty()) {
				Skill skill = this.skills.poll();
				contents.add(ClickableItem.of(skill.getTriggerGuiRepresenter(), this.getConsumer(cPlayer, skill)));
			}
			
		}
		
		
		
		contents.set(SlotPos.of(5, 4), ClickableItem.of(new ItemBuilder(Material.ARMOR_STAND).name("Zurück").build(), event->{
			GUIs.S_EquippedSkillsGUI.open((Player)event.getWhoClicked());
		}));
		
	}

	@Override
	public void update(Player player, InventoryContents contents) {}
	
}
