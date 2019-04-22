package com.avarioncraft.AvarionCombat.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.avarioncraft.AvarionCombat.actions.CombatCooldown;
import com.avarioncraft.AvarionCombat.data.CombatPlayer;
import com.avarioncraft.AvarionCombat.events.PassiveSkillTriggerEvent;
import com.avarioncraft.AvarionCombat.events.SkillCastEvent;
import com.avarioncraft.AvarionCombat.events.SkillDamageEvent;
import com.avarioncraft.AvarionCombat.events.TwohandedWeaponToggleEvent;
import com.avarioncraft.AvarionCombat.skills.Skill;
import com.avarioncraft.AvarionCombat.skills.SkillType;
import com.avarioncraft.AvarionCombat.skills.TriggerType;
import com.avarioncraft.AvarionCombat.util.enums.WeaponType;

import net.crytec.api.nbt.NBTItem;
import net.crytec.api.recharge.Recharge;

public class SkillTriggerListener implements Listener{
	
	private void trigger(Player player, Event event, TriggerType type) {
		
		if(CombatCooldown.of(player) < 0.99F) return;
		
		Object[] params = new Object[3];
		Skill skill = CombatPlayer.of(player).getSkillEquip().getSkill(type);
		
		if (skill == null) return;
		
		if(skill.getSkillType().equals(SkillType.TARGET)) {
			params[0] = player.getTargetEntity(skill.getRange(), true);
		}
		
		if (!Recharge.Instance.usable(player, skill.getName())) return;
		
		SkillCastEvent castEvent = new SkillCastEvent(skill);
		Bukkit.getPluginManager().callEvent(castEvent);
		
		if(castEvent.isCancelled()) return;
		
		Recharge.Instance.use(player, skill.getName(), skill.getName(), skill.getBaseCooldown(), true, true);
		skill.onCast(params, event);
		
		
	}
	
	@EventHandler
	public void PassiveTrigger(PassiveSkillTriggerEvent event) {
		Player player = event.getSkill().getCaster().getPlayer();
		this.trigger(player, event, TriggerType.PASSIVE);
	}
	
	@EventHandler
	public void SkillCastTrigger(SkillCastEvent event) {
		
		if(event.getSkill().getTriggerType().equals(TriggerType.CAST_OTHER_SKILL)) return;
		
		Player player = event.getSkill().getCaster().getPlayer();
		this.trigger(player, event, TriggerType.CAST_OTHER_SKILL);
	}
	
	@EventHandler
	public void InteractTrigger(PlayerInteractEvent event) {
		if(event.getHand() != EquipmentSlot.HAND) return;
		
		Player player = event.getPlayer();
		
		if(!player.isSneaking()) return;
		
		this.trigger(player, event, TriggerType.SNEAK_CAST);
		
	}
	
	@EventHandler
	public void MeeleTrigger(EntityDamageByEntityEvent event) {
		
		if(event instanceof SkillDamageEvent) return;
		
		if(!(event.getDamager() instanceof Player)) return;
		
		Player player = (Player) event.getDamager();
		
		this.trigger(player, event, TriggerType.ATTACK_MEELE);
		
	}
	
	@EventHandler
	public void MeeleDefendTrigger(EntityDamageByEntityEvent event) {
		
		if(event instanceof SkillDamageEvent) return;
		
		if(!(event.getEntity() instanceof Player)) return;
		
		Player player = (Player) event.getEntity();
		
		this.trigger(player, event, TriggerType.DEFEND_MEELE);
		
	}
	
	@EventHandler
	public void SkillTrigger(EntityDamageByEntityEvent event) {
		
		if(!(event instanceof SkillDamageEvent)) return;
		
		if(!((SkillDamageEvent) event).getSkill().getDamageType().equals(DamageCause.MAGIC)) return;
		
		if(!(event.getDamager() instanceof Player)) return;
		
		this.trigger((Player) event.getDamager(), event, TriggerType.ATTACK_SKILL);
		
	}
	
	@EventHandler
	public void SkillDefendTrigger(EntityDamageByEntityEvent event) {
		
		if(!(event instanceof SkillDamageEvent)) return;
		
		if(!((SkillDamageEvent) event).getSkill().getDamageType().equals(DamageCause.MAGIC)) return;
		
		if(!(event.getEntity() instanceof Player)) return;
		
		this.trigger((Player) event.getEntity(), event, TriggerType.DEFEND_MAGIC);
		
	}
	
	@EventHandler
	public void DeathTrigger(PlayerDeathEvent event) {
		
		Player player = event.getEntity();
		
		this.trigger(player, event, TriggerType.DEATH);
		
	}
	
	@EventHandler
	public void BreakTrigger(BlockBreakEvent event) {
		
		Player player = event.getPlayer();
		
		this.trigger(player, event, TriggerType.BLOCK_BREAK);
		
	}
	
	@EventHandler
	public void RangedTrigger(EntityDamageByEntityEvent event) {
		if(!(event.getDamager() instanceof Arrow)) return;
		Arrow damager = (Arrow) event.getDamager();
		if(damager.getVelocity().length() < 2.48D) return;
		if(!(damager.getShooter() instanceof Player)) return;
		
		Player player = (Player) damager.getShooter();
		
		this.trigger(player, event, TriggerType.ATTACK_RANGED);
		
	}
	
	@EventHandler
	public void RangedDefenderTrigger(EntityDamageByEntityEvent event) {
		if(!(event.getDamager() instanceof Projectile)) return;
		if(!(event.getEntity() instanceof Player)) return;
		
		Player player = (Player) event.getEntity();
		
		this.trigger(player, event, TriggerType.DEFEND_RANGED);
		
	}
	
	@EventHandler
	public void SwingTrigger(PlayerAnimationEvent event) {
		if(event.getAnimationType() != PlayerAnimationType.ARM_SWING) return;
		
		Player player = event.getPlayer();
		
		if(player == null) return;
		
		this.trigger(player, event, TriggerType.SWING);

	}
	
	@EventHandler
	public void SpeallCastTrigger(PlayerInteractEvent event) {
		if(event.getHand() != EquipmentSlot.HAND) return;
		if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
		
		if(item == null || item.getType() == Material.AIR) return;
		//TODO Testen, ob Stab
		NBTItem nbt = new NBTItem(item);
		if (nbt.hasKey(WeaponType.NBT_KEY) && nbt.getString(WeaponType.NBT_KEY).equals(WeaponType.STAFF.getNbtTag())) {
			this.trigger(event.getPlayer(), event, TriggerType.SPELL_CAST);
		}
	}
	
	@EventHandler
	public void onItemSwitch(PlayerSwapHandItemsEvent event) {
		ItemStack item = event.getMainHandItem();
		if(item == null || !item.hasItemMeta()) return;
		//TODO testen ob zweihändig und Bogen löschen!
		if(item.getType().equals(Material.BOW) /*|| item.getItemMeta().getCustomTagContainer().hasCustomTag(arg0, arg1)*/){
			new TwohandedWeaponToggleEvent(event).callEvent();
		}
	}
}