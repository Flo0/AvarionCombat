package com.avarioncraft.AvarionCombat.skills.vault.Aggressor.main;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.avarioncraft.AvarionCombat.buffs.StatBuff;
import com.avarioncraft.AvarionCombat.data.DamagePackage;
import com.avarioncraft.AvarionCombat.events.StatModifyEvent;
import com.avarioncraft.AvarionCombat.skills.Skill;
import com.avarioncraft.AvarionCombat.skills.SkillType;
import com.avarioncraft.AvarionCombat.skills.TriggerType;
import com.avarioncraft.AvarionCombat.threads.CheckableSkill;
import com.avarioncraft.AvarionCombat.threads.SkillCheckerThread;
import com.avarioncraft.AvarionCombat.util.RoundUtil;
import com.avarioncraft.AvarionCombat.util.enums.SkillTreeType;
import com.avarioncraft.AvarionCombat.util.enums.Stat;

import lombok.Getter;
import lombok.Setter;
import net.crytec.api.smartInv.content.SlotPos;

public class SejuStyle extends Skill implements CheckableSkill{

	public SejuStyle(Player caster) {
		
		super(0, 1, SkillType.FIGHTING_STYLE, TriggerType.ATTACK_MEELE, SkillTreeType.AGGRESSOR, "Seju Kampfstil", caster, 1, null, 1, Material.IRON_SWORD, SlotPos.of(0, 4));
		
		super.registerVaribale("<comboCount>", () -> "" + this.comboCount);
		super.registerVaribale("<maxCombo>", () -> "" + this.maxCombo);
		super.registerVaribale("<dmgBuff>", () -> "" + this.buffMulti * 100 + "%");
		super.registerVaribale("<stat>", () -> Stat.MEELE_DAMAGE.getDisplayName());
		super.registerVaribale("<seconds>", () -> "" + RoundUtil.unsafeRound(((double)this.runout / 1000D), 1) + " Sek.");
		
		super.addDescLine("");
		super.addDescLine("§fDieser Kampfstil baut sich mit jedem Schlag weiter auf.");
		super.addDescLine("§fDu kannst den Kampfstil verschieden ausbauen.");
		super.addDescLine("");
		super.addDescLine("§fZu Beginn ist er schwächer als andere Skills und zeigt");
		super.addDescLine("§ferst recht spät sein volles Potenzial.");
		super.addDescLine("");
		super.addDescLine("§fJeder Schlag erhöht deinen <stat> um <dmgBuff>.");
		super.addDescLine("§fDies läuft nach <seconds> aus.");
		super.addDescLine("");
		super.addDescLine("§fKombo: §e[<comboCount>§e/<maxCombo>§e]");
		super.addDescLine("");
		
		this.comboCount = 0;
		this.maxCombo = 30;
		this.buffMulti = 0.01F;
		this.runout = 12000;
		this.extraArmor = 0;
		this.magic = 0;
		this.resetSoftCaps();
		
		SkillCheckerThread.registerSkill(this);
	}
	
	@Getter
	private int comboCount;
	@Getter @Setter
	private int maxCombo;
	@Getter @Setter
	private int upperSoftCap;
	@Getter @Setter
	private int lowerSoftCap;
	private long runout;
	private float buffMulti;
	private long lastRise = 0L;
	private double extraCrit;
	private double extraSpeed;
	private double extraArmor;
	private double magic;
	
	private SejuBuff sejuBuff;
	
	public void addMaxCombo(int amount) {
		this.maxCombo += amount;
	}
	
	public void addExtraCrit(double amount) {
		this.extraCrit += amount;
	}
	
	public void addExtraSpeed(double amount) {
		this.extraSpeed += amount;
	}
	
	public void addExtraArmor(double amount) {
		this.extraArmor += amount;
	}
	
	public void addExtraMagic(double amount) {
		this.magic += amount;
	}
	
	public void checkForReset() {
		if(this.comboCount == this.lowerSoftCap) return;
		if(System.currentTimeMillis() - this.lastRise > this.runout) {
			this.comboCount = this.lowerSoftCap;
		}
	}
	
	public void riseCount() {
		if(this.comboCount < this.upperSoftCap && this.comboCount < this.maxCombo) {
			this.lastRise = System.currentTimeMillis();
			this.comboCount++;
		}
	}
	
	public void lowerCount() {
		if(this.comboCount <= this.lowerSoftCap) return;
		if(this.comboCount > 0) this.comboCount --;
	}
	
	public void resetSoftCaps() {
		this.upperSoftCap = this.maxCombo;
		this.lowerSoftCap = 0;
	}
	
	@Override
	public void modifyDamageGeneration(DamagePackage pack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCast(Object[] parameter, Event triggerEvent) {
		if(this.sejuBuff == null) this.sejuBuff = new SejuBuff(this);
		this.riseCount();
	}

	@Override
	public void inflict(Object[] parameter, LivingEntity target) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cleanUp() {
		StatBuff.statBuffs.get(super.getCaster().getPlayer()).remove(this.sejuBuff);
	}

	@Override
	public void onLevelUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCheckup() {
		this.checkForReset();
	}
	
	private class SejuBuff extends StatBuff{

		public SejuBuff(SejuStyle style) {
			super(style.getCaster().getPlayer(), -1);
			this.style = style;
			
			super.setDisplayName("§6Seju Style");
			
			super.setIconMaterial(Material.IRON_SWORD);
			
			super.addDescLine("§fDieser Buff erhöht deinen");
			super.addDescLine("§6Normalen Schaden §fje nachdem");
			super.addDescLine("§fwie hoch dein §6Seju-Stil");
			super.addDescLine("§fgewachsen ist.");
		}
		
		private final SejuStyle style;
		
		@Override
		public void onModify(StatModifyEvent event) {
			if(style.comboCount == 0) return;
			
			switch(event.getStat()) {
			case MEELE_DAMAGE:
				double baseD = event.getValue();
				event.setValue(baseD + (baseD * (style.buffMulti * style.comboCount)));
				return;
			case ARMOR:
				double baseA = event.getValue();
				event.setValue(baseA + (baseA * (style.extraArmor * style.comboCount)));
				return;
			case CRIT_CHANCE:
				double baseCC = event.getValue();
				event.setValue(baseCC + (style.extraCrit * style.comboCount));
				return;
			case SPEED:
				double baseS = event.getValue();
				event.setValue(baseS + (style.extraSpeed * style.comboCount));
				return;
			case MAGIC_POWER:
				double baseM = event.getValue();
				event.setValue(baseM + (baseM * ((style.magic / 100) * style.comboCount)));
				return;
			default:
				return;
			}
			
		}

	}
	
}
