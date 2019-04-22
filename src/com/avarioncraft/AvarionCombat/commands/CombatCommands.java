package com.avarioncraft.AvarionCombat.commands;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Cow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.avarioncraft.AvarionCombat.data.CombatPlayer;
import com.avarioncraft.AvarionCombat.gui.GUIs;
import com.avarioncraft.AvarionCombat.util.enums.Stat;
import com.avarioncraft.AvarionCombat.util.enums.WeaponType;

import net.crytec.api.devin.commands.Command;
import net.crytec.api.devin.commands.CommandResult;
import net.crytec.api.devin.commands.Commandable;
import net.crytec.api.devin.commands.OptionalArg;
import net.crytec.api.itemstack.ItemBuilder;
import net.crytec.api.util.F;

public class CombatCommands implements Commandable {
		
	@Command(struct = "debugitem")
	public CommandResult debugsword(Player sender, Material material, Stat stat, int lvl) {
		
		ItemStack item = new ItemBuilder(material)
					.name("§aSchwert mit Attribut")
					.loreReplace("§aAttribut:")
					.loreReplace("§f" + stat.getDisplayName() + ": " + lvl)
					.addNBTDouble(stat.toString(), lvl)
					.build();
		
		sender.getInventory().addItem(item);
		
		return CommandResult.success();
	}
	
	@Command(struct = "debugstaff")
	public CommandResult debugStaff(Player sender, Material material, Stat stat, int lvl) {
		
		ItemStack item = new ItemBuilder(material)
					.name("§dStab mit Attribut")
					.loreReplace("§aAttribut:")
					.loreReplace("§f" + stat.getDisplayName() + ": " + lvl)
					.addNBTDouble(stat.toString(), lvl)
					.addNBTString(WeaponType.NBT_KEY, WeaponType.STAFF.getNbtTag())
					.build();
		
		sender.getInventory().addItem(item);
		
		return CommandResult.success();
	}
	
	@Command(struct = "combat target")
	public CommandResult target(Player sender, @OptionalArg("10") int level) {
		Cow cow = (Cow) sender.getWorld().spawnEntity(sender.getLocation(), EntityType.COW);
		cow.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(level);
		cow.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1);
		cow.setHealth(cow.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		cow.setAI(false);
		return CommandResult.success();
	}
	
	@Command(struct = "combat levelup")
	public CommandResult lvlUp(Player sender, int level) {
		CombatPlayer.of(sender).getLevelSet().addLevel(level);
		sender.sendMessage(F.main("Skills", "Dir wurden " + F.elem(level + " Skillpunkte") + " hinzugefügt."));
		return CommandResult.success();
	}
	
	@Command(struct = "combat getSkillPoints")
	public CommandResult getSkillPoints(Player sender) {
		int level = CombatPlayer.of(sender).getLevelSet().getSkillPoints();
		sender.sendMessage(F.main("Skills", "Du hast " + F.elem(level + " Skillpunkte") + "."));
		return CommandResult.success();
	}
	
	@Command(struct = "combat setxp")
	public CommandResult setXP(Player sender, int xp) {
		CombatPlayer.of(sender).getLevelSet().serializeExp(xp);
		sender.sendMessage(F.main("Skills", "Dir wurden " + F.elem(xp + " Erfahrung") + " hinzugefügt."));
		return CommandResult.success();
	}
	
	@Command(struct = "combat getxp")
	public CommandResult getXP(Player sender) {
		long xp = CombatPlayer.of(sender).getLevelSet().getExp();
		sender.sendMessage(F.main("Skills", "Du hast " + F.elem(xp + " Erfahrung") + "."));
		return CommandResult.success();
	}
	
	@Command(struct = "combat getLevel")
	public CommandResult getLevel(Player sender) {
		long xp = CombatPlayer.of(sender).getLevelSet().getLevel();
		sender.sendMessage(F.main("Skills", "Du hast " + F.elem(xp + " Level") + "."));
		return CommandResult.success();
	}
	
	@Command(struct = "combat reset")
	public CommandResult lvlUp(Player sender) {
		CombatPlayer cp = CombatPlayer.of(sender);
		cp.getSkillEquip().getSkillSet().values().forEach(skill -> skill.setLevel(0));
		cp.getLevelSet().serializeExp(0);
		sender.sendMessage(F.main("Skills", "Deine Skills wurden zurückgesetzt."));
		return CommandResult.success();
	}
	
	@Command(struct = "combat")
	public CommandResult userHelpCommand(Player sender) {
		// Base Command ohne Argumente ist nötig damit es in der TabCompletion angezeigt wird.
		return CommandResult.success();
	}
	
	@Command(struct = "combat stats", desc = "Öffnet deine Kampf-Stats")
	public CommandResult userCombatMenu(Player sender) {
		GUIs.S_CombatMainGUI.open(sender);
		return CommandResult.success();
	}
	
	@Command(struct = "combat buffs", desc = "Öffnet deine aktiven Buffs")
	public CommandResult userBuffMenu(Player sender) {
		GUIs.S_BuffGUI.open(sender);
		return CommandResult.success();
	}
	
	@Command(struct = "combat skills", desc = "Öffnet den SkillTree")
	public CommandResult skillTree(Player sender) {
		GUIs.S_SkillMainGUI.open(sender);
		return CommandResult.success();
	}
	
	@Command(struct = "combat debugitems", desc = "Öffnet deine Kampf-Stats")
	public CommandResult admingDebugItems(Player sender) {
		
		sender.getInventory().addItem(new ItemBuilder(Material.STICK)
				.name("§eDicker Knüppel")
				.addNBTDouble(Stat.MEELE_DAMAGE.toString(), 6.0)
				.addNBTDouble(Stat.CRIT_CHANCE.toString(), 25.0)
				.build());
		
		sender.getInventory().addItem(new ItemBuilder(Material.IRON_CHESTPLATE)
				.name("§eMetallplatte")
				.addNBTDouble(Stat.ARMOR.toString(), 200.0)
				.build());
		
		return CommandResult.success();
	}
}