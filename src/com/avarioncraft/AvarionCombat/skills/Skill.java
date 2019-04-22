package com.avarioncraft.AvarionCombat.skills;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import com.avarioncraft.AvarionCombat.data.CombatPlayer;
import com.avarioncraft.AvarionCombat.data.DamagePackage;
import com.avarioncraft.AvarionCombat.skills.conditions.Condition;
import com.avarioncraft.AvarionCombat.util.enums.SkillTreeType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import lombok.Getter;
import lombok.Setter;
import net.crytec.api.itemstack.ItemBuilder;
import net.crytec.api.smartInv.ClickableItem;
import net.crytec.api.smartInv.content.SlotPos;

public abstract class Skill {
	
	public Skill(long baseCooldown, int maxLevel, SkillType skillType, TriggerType triggerType, SkillTreeType treeType,
			String name, Player caster, int playerLevelRequ, @Nullable Class<? extends Skill> parentClass, int parentLevel, Material icon,
			SlotPos pos) {
		
		this(baseCooldown, maxLevel, skillType, triggerType, treeType, name, caster, playerLevelRequ, parentClass, parentLevel, new ItemStack(icon), pos);
	}
	

	public Skill(long baseCooldown, int maxLevel, SkillType skillType, TriggerType triggerType, SkillTreeType treeType,
			String name, Player caster, int playerLevelRequ, @Nullable Class<? extends Skill> parentClass, int parentLevel, ItemStack icon,
			SlotPos pos) {
		this.icon = icon;
		this.childs = Sets.newHashSet();
		this.parentClass = parentClass;
		this.playerLevelRequ = playerLevelRequ;
		this.parentLevelReq = parentLevel;
		this.baseCooldown = baseCooldown;
		this.skillType = skillType;
		this.triggerType = triggerType;
		this.treeType = treeType;
		this.name = name;
		this.caster = CombatPlayer.of(caster);
		if(parentClass != null) {
			this.parent = Optional.ofNullable(this.caster.getSkillEquip().getSkillOfClass(parentClass));
		}else {
			this.parent = Optional.ofNullable(null);
		}
		
		this.cooldown = this.baseCooldown;
		this.maxLevel = maxLevel;
		this.level = 0;
		this.guiPos = pos;
		
		if(this.parent.isPresent()) {
			this.parent.get().getChilds().add(this);
		}
		
		this.description.add("§fSkillbaum: §6" + this.treeType.getDisplayName());
		this.description.add("§fSkill Typ: §6" + this.skillType.getDisplayName());
		this.description.add("§fAuslöser: §6" + this.triggerType.getDisplayName());
		this.description.add("§fCooldown: §6" + ((this.getCooldown() == 0) ? "Keine" : (this.getCooldown() / 1000) + " Sekunden" ));
	}

	@Getter
	@Setter
	private long cooldown;
	@Getter
	private final long baseCooldown;
	@Getter
	private final SkillTreeType treeType;
	@Getter
	private final SkillType skillType;
	@Getter
	private final String name;
	@Getter
	private final CombatPlayer caster;
	@Getter
	private final TriggerType triggerType;
	@Getter
	private Set<Condition> conditions = Sets.newHashSet();
	@Getter
	private ArrayList<String> description = Lists.newArrayList();
	@Getter
	private final int maxLevel;
	@Getter
	@Setter
	private int level;
	@Getter
	private final Optional<Skill> parent;
	@Getter
	private final Set<Skill> childs;
	@Getter
	private final int parentLevelReq;
	@Getter
	private final int playerLevelRequ;
	@Getter
	@Setter
	private ItemStack icon;
	@Getter
	@Setter
	private int range = 1;
	@Getter
	private final SlotPos guiPos;
	@Getter
	@Setter
	protected DamageCause damageType = DamageCause.MAGIC;
	@Getter
	private final Class<? extends Skill> parentClass;
	
	private Map<String, Supplier<String>> variables = Maps.newHashMap();
	
	public void informCaster(String information) {
		this.caster.getPlayer().sendMessage("§9[§e" + this.name + "§9]§f " + information);
	}
	
	public String getValueOf(String key) {
		if(!this.variables.containsKey(key)) return "null";
		return this.variables.get(key).get();
	}
	
	protected void registerVaribale(String key, Supplier<String> var) {
		this.variables.put(key, var);
	}
	
	public Set<Skill> getNonZeroChilds(){
		return this.childs.stream().filter(skill->skill.getLevel() > 0).collect(Collectors.toSet());
	}
	
	public void addDescLine(String line) {
		this.description.add(line);
	}

	public void putDescLine(String line, int index) {
		this.description.set(index, line);
	}

	public List<String> updatedDescription() {

		ArrayList<String> desc = Lists.newArrayList(this.description);

		for (int index = 0; index < desc.size(); index++) {
			String line = desc.get(index);
			for (String var : this.variables.keySet()) {
				if (line.contains(var)) {
					line = line.replace(var, "§6" + this.getValueOf(var) + "§f");
				}
			}
			desc.set(index, line);
		}
		
		return desc;
	}

	private String getSkillReqPlaceholder() {
		String skillName;

		if (this.parent.isPresent()) {
			if (this.parent.get().getLevel() == 0) {
				skillName = "§c" + this.parent.get().getName();
			} else {
				skillName = "§a" + this.parent.get().getName();
			}
		} else {
			skillName = "§f -";
		}

		return skillName;
	}

	private String getPlayerLevelReqPlaceholder() {
		return (this.playerLevelRequ > this.caster.getLevelSet().getLevel()) ? "§c" + this.parentLevelReq
				: "§a" + this.parentLevelReq;
	}

	private String getSkillLevelReqPlaceholder() {
		String skillLevel;
		if (this.parent.isPresent()) {
			if (this.parent.get().getLevel() < this.parentLevelReq) {
				skillLevel = "§c" + this.parentLevelReq;
			} else {
				skillLevel = "§a" + this.parentLevelReq;
			}
		} else {
			skillLevel = "§f -";
		}

		return skillLevel;
	}

	public ItemStack getTriggerGuiRepresenter() {

		return new ItemBuilder(this.icon.clone())
				.name("§2" + this.name + " §6(" + this.level + " / " + this.maxLevel + ")").lore("")
				.lore("§fTrigger-Typ: §6" + this.triggerType.getDisplayName()).lore("").lore(this.updatedDescription())
				.lore("").lore("§fKlicke zum auswählen.").build();
	}

	public ClickableItem getGuiRepresenter() {

		ItemStack item;
		ClickableItem clicker;

		if (this.levelUpConditionsMet()) {
			
			item = new ItemBuilder(this.icon.clone())
					.name("§2" + this.name + " §6(" + this.level + " / " + this.maxLevel + ")")
					.lore(this.updatedDescription()).lore("")
					.lore("")
					.lore("§2Spieler-Level Benötigt: " + this.getPlayerLevelReqPlaceholder())
					.lore("§2Skill Benötigt: " + this.getSkillReqPlaceholder())
					.lore("§2Skill-Level Benötigt: " + this.getSkillLevelReqPlaceholder())
					.setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
					.setItemFlag(ItemFlag.HIDE_ENCHANTS)
					.build();

			clicker = ClickableItem.of(item, event -> {
				this.caster.getLevelSet().removeSkillPoints(1);
				this.lvlUp();
				this.getTreeType().getInventoryGUI().open(this.caster.getPlayer());
			});

		} else {
			
			String titleColor = (this.level  != this.maxLevel) ? "§7" : "§3";
			
			item = new ItemBuilder(this.icon.clone())
					.name(titleColor + this.name + " §8(" + this.level + " / " + this.maxLevel + ")")
					.lore(this.updatedDescription())
					.lore("")
					.lore("§2Spieler-Level Benötigt: " + this.getPlayerLevelReqPlaceholder())
					.lore("§2Skill Benötigt: " + this.getSkillReqPlaceholder())
					.lore("§2Skill-Level Benötigt: " + this.getSkillLevelReqPlaceholder())
					.setItemFlag(ItemFlag.HIDE_ATTRIBUTES)
					.setItemFlag(ItemFlag.HIDE_ENCHANTS)
					.build();

			clicker = ClickableItem.empty(item);

		}

		return clicker;
	}

	private boolean levelUpConditionsMet() {
		boolean isMet = true;
		if(this.caster.getLevelSet().getSkillPoints() == 0)
			isMet = false;
		if (this.caster.getLevelSet().getLevel() < this.playerLevelRequ)
			isMet = false;
		if (this.parent.isPresent()) {
			if (this.parent.get().getLevel() < this.parentLevelReq)
				isMet = false;
		}
		if (this.level == this.maxLevel)
			isMet = false;

		return isMet;
	}
	
	public void lvlUp() {
		if(this.level == maxLevel) return;
		this.level++;
		this.onLevelUp();
	}
	
	public void addLevel(int amount) {
		for(int lvl = 0; lvl < amount; lvl++) {
			this.lvlUp();
		}
	}

	public boolean metConditions() {
		boolean valid = true;

		for (Condition c : conditions) {
			if (!c.validate(this.caster)) {
				valid = false;
				break;
			}
		}

		return valid;
	}
	
	public abstract void modifyDamageGeneration(DamagePackage pack);

	public abstract void onCast(Object[] parameter, Event triggerEvent);
	
	public abstract void inflict(Object[] parameter, LivingEntity target);

	public abstract void cleanUp();
	
	public abstract void onLevelUp();
	
}
