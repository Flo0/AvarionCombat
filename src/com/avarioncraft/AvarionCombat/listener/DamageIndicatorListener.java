package com.avarioncraft.AvarionCombat.listener;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.util.Vector;

import com.avarioncraft.AvarionCombat.core.AvarionCombat;
import com.avarioncraft.AvarionCombat.data.DamagePackage;
import com.avarioncraft.AvarionCombat.entity.DamageIndicatorEntity;
import com.avarioncraft.AvarionCombat.events.AvarionCombatEvent;

import net.minecraft.server.v1_13_R2.WorldServer;

public class DamageIndicatorListener implements Listener {

//	private EntityHider entityHider;
	
	private final ChatColor defaultColor = ChatColor.YELLOW;
	private final ChatColor critColor = ChatColor.RED;
	private final ChatColor magicColor = ChatColor.DARK_PURPLE;
	private final ChatColor rangedColor = ChatColor.GOLD;

	public DamageIndicatorListener(AvarionCombat plugin) {
//		this.entityHider = new EntityHider(plugin, Policy.BLACKLIST);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDamage(AvarionCombatEvent event) {
		Entity target = event.getDamagePack().getDefender();
		DamagePackage damage = event.getDamagePack();
		
		if (damage.getDefender() == null)
			return;

		String indicator = String.valueOf(Math.round(damage.getDamage()));

		ChatColor color = this.defaultColor;
		
		if (damage.isCrit()) {
			color = this.critColor;
			indicator = indicator + " CRIT!";
		} else if (damage.isMagic()) {
			color = this.magicColor;
		} else if (damage.isRanged()) {
			color = this.rangedColor;
		}
		
		if (damage.getInflictingSkill() != null) {
			indicator = indicator + " " + damage.getInflictingSkill().getName();
		}
		
		Vector offset = new Vector(ThreadLocalRandom.current().nextDouble(1D) - 0.5D, ThreadLocalRandom.current().nextDouble(0.5D) + 1D, ThreadLocalRandom.current().nextDouble(1D) - 0.5D);
		
		WorldServer cw = ((CraftWorld) target.getWorld()).getHandle();
		DamageIndicatorEntity e = new DamageIndicatorEntity(color, cw, target.getLocation().clone().add(offset), color + indicator);
		cw.addEntity(e);

//		target.getNearbyEntities(20, 20, 20).stream().filter(ent -> ent instanceof Player).map(ent -> (Player) ent).forEach(cur -> {
//			if (damage.getAttacker().isPresent() && cur != damage.getAttacker().get())
//				this.entityHider.hideEntity(cur, e.getBukkitEntity());
//		});
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onHealthRegain(EntityRegainHealthEvent event) {
		Entity target = event.getEntity();

		if (target instanceof Player && event.getAmount() > 0) {
			WorldServer cw = ((CraftWorld) target.getWorld()).getHandle();
			DamageIndicatorEntity e = new DamageIndicatorEntity(ChatColor.GREEN, cw, target.getLocation(), String.valueOf(Math.round(event.getAmount())));
			cw.addEntity(e);
		}
	}

}
