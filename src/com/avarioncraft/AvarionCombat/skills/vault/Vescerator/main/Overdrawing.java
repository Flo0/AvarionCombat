package com.avarioncraft.AvarionCombat.skills.vault.Vescerator.main;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.metadata.FixedMetadataValue;

import com.avarioncraft.AvarionCombat.core.AvarionCombat;
import com.avarioncraft.AvarionCombat.data.CombatPlayer;
import com.avarioncraft.AvarionCombat.data.DamagePackage;
import com.avarioncraft.AvarionCombat.events.SkillDamageEvent;
import com.avarioncraft.AvarionCombat.skills.Skill;
import com.avarioncraft.AvarionCombat.skills.SkillType;
import com.avarioncraft.AvarionCombat.skills.TriggerType;
import com.avarioncraft.AvarionCombat.util.enums.SkillTreeType;

import net.crytec.api.interfaces.ConditionalRunnable;
import net.crytec.api.smartInv.content.SlotPos;
import net.crytec.api.util.Tasks;

public class Overdrawing extends Skill{

	public Overdrawing(Player caster) {
		super(0, 1, SkillType.TARGET, TriggerType.ATTACK_RANGED, SkillTreeType.VESCERATOR, "Überspannen", caster, 1, null, 1, Material.BOW, SlotPos.of(0, 1));
		
		super.registerVaribale("<maxOverdraw>", () -> "" + this.maxOverdraws);
		super.registerVaribale("<overdrawDmg>", () -> "" + this.overDrawDamage);
		super.registerVaribale("<drawTime>", () -> ((double) this.overDrawTicks / 20.0D) + " Sek");
		
		super.addDescLine("§fDu kannst deinen Bogen überspannen,");
		super.addDescLine("§fdadurch extra Schaden verursachen und.");
		super.addDescLine("§fzusätzliche Effekte auslösen.");
		super.addDescLine("§f");
		super.addDescLine("§fMaximale Überspannung: <maxOverdraw>");
		super.addDescLine("§fSchaden pro Überspannung: <overdrawDmg>");
		super.addDescLine("§fÜberspannungszeit: <drawTime>");
		
		this.maxOverdraws = 3;
		this.overDrawDamage = 2.0;
		this.overDraws = 0;
		this.bowTicks = 0;
		this.overDrawTicks = 20;
	}
	
	private int maxOverdraws;
	private double overDrawDamage;
	private int overDraws;
	private int overDrawTicks;
	private int bowTicks;
	
	private void addBowTick() {
		if(this.overDraws == this.maxOverdraws) return;
		this.bowTicks++;
		if(this.bowTicks < 24) return;
		if((this.bowTicks - 24) % this.overDrawTicks == 0) {
			this.overDraws++;
			Player p = super.getCaster().getPlayer();
			float overSec = (this.overDraws / 10F);
			p.playSound(p.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 0.5F + overSec, 0.8F + overSec);
		}
	}
	
	private void resetDraw() {
		this.bowTicks = 0;
		this.overDraws = 0;
	}
	
	@Override
	public void modifyDamageGeneration(DamagePackage pack) { }

	@Override
	public void onCast(Object[] parameter, Event triggerEvent) {
		EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) triggerEvent;
		Projectile pro = (Projectile) event.getDamager();
		if(pro.hasMetadata("overdraw")){
			int draws = (int) pro.getMetadata("overdraw").get(0).value();
			this.inflict(new Object[]{draws}, (LivingEntity) event.getEntity());
		}
	}

	@Override
	public void inflict(Object[] parameter, LivingEntity target) {
		System.out.println("inflicting");
		SkillDamageEvent dmgEvent = new SkillDamageEvent(super.getCaster().getPlayer(), target, DamageCause.MAGIC, this.overDrawDamage * (int) parameter[0], this);
		dmgEvent.callEvent();
		
	}

	@Override
	public void cleanUp() {}

	@Override
	public void onLevelUp() {}
	
	public static final class OverDrawListener implements Listener{
		
		@EventHandler
		public void onBowDraw(PlayerInteractEvent event) {
			Player player = event.getPlayer();

			if(event.getHand().equals(EquipmentSlot.OFF_HAND)) return;
			if(!player.getInventory().getItemInMainHand().getType().equals(Material.BOW)) return;
			Skill s = CombatPlayer.of(player).getSkillEquip().getSkill(TriggerType.ATTACK_RANGED);
			if(s == null || !s.getClass().equals(Overdrawing.class)) return;
			
			Tasks.schedule(new ConditionalRunnable() {
				
				int raiseTime = 0;
				Overdrawing over = CombatPlayer.of(player).getSkillEquip().getSkillOfClass(Overdrawing.class);
				
				@Override
				public void run() {
					raiseTime++;
					over.addBowTick();
				}
				
				@Override
				public boolean canCancel() {
					return player.getHandRaisedTime() < raiseTime;
				}
				
				@Override
				public void onCancel() {
					
				}
			}, 0, 1, -1);
			
		}
		
		@EventHandler
		public void onBowShoot(EntityShootBowEvent event) {
			if(!(event.getEntity() instanceof Player)) return;
			Overdrawing over = CombatPlayer.of((Player)event.getEntity()).getSkillEquip().getSkillOfClass(Overdrawing.class);
			if (over.getLevel() == 0 || over.overDraws == 0) return;
			
			event.getProjectile().setMetadata("overdraw", new FixedMetadataValue(AvarionCombat.getPlugin(), over.overDraws));
			over.resetDraw();
		}
		
	}

}
