package com.avarioncraft.AvarionCombat.core;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import com.avarioncraft.AvarionCombat.actions.CombatCooldown;
import com.avarioncraft.AvarionCombat.commands.CombatCommands;
import com.avarioncraft.AvarionCombat.data.CombatPlayer;
import com.avarioncraft.AvarionCombat.io.PlayerStorage;
import com.avarioncraft.AvarionCombat.listener.BuffListener;
import com.avarioncraft.AvarionCombat.listener.CombatListener;
import com.avarioncraft.AvarionCombat.listener.DamageIndicatorListener;
import com.avarioncraft.AvarionCombat.listener.JoinEvents;
import com.avarioncraft.AvarionCombat.listener.SkillTriggerListener;
import com.avarioncraft.AvarionCombat.listener.UpdateStatsEvents;
import com.avarioncraft.AvarionCombat.skills.SkillHandler;
import com.avarioncraft.AvarionCombat.skills.SkillRegistration;
import com.avarioncraft.AvarionCombat.skills.mechanics.mechanicThreads.CastPointThread;
import com.avarioncraft.AvarionCombat.skills.mechanics.mechanicThreads.ProjectileThread;
import com.avarioncraft.AvarionCombat.skills.mechanics.mechanicThreads.RepeatableThread;
import com.avarioncraft.AvarionCombat.skills.vault.Vescerator.main.ArcaneArrows;
import com.avarioncraft.AvarionCombat.skills.vault.Vescerator.main.Overdrawing;
import com.avarioncraft.AvarionCombat.threads.BuffTimeoutThread;
import com.avarioncraft.AvarionCombat.threads.PlayerUpdateThread;
import com.avarioncraft.AvarionCombat.threads.SkillCheckerThread;
import com.avarioncraft.AvarionCombat.util.enums.Stat;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import lombok.Getter;
import net.crytec.api.devin.commands.CommandRegistrar;
import net.crytec.api.devin.commands.ObjectParsing;
import net.crytec.shaded.org.apache.lang3.EnumUtils;
import net.crytec.taskchain.BukkitTaskChainFactory;
import net.crytec.taskchain.TaskChainFactory;

public class AvarionCombat extends JavaPlugin{
	
	@Getter
	private ProtocolManager protocolManager;
	@Getter
	private static AvarionCombat plugin;
	@Getter
	private PlayerStorage storage;
	
	private CommandRegistrar commandRegistrar;
	
	@Getter
	private TaskChainFactory taskFactory;
	
	@Override
	public void onLoad() {
		plugin = this;
		
		
		ObjectParsing.registerParser(Material.class, (args) -> {
			String input = args.next();
			if (!EnumUtils.isValidEnum(Material.class, input)) {
				throw new IllegalArgumentException(input + " ist kein gültiges Material!");
			}
			return Material.valueOf(input);
		});
		
		ObjectParsing.registerParser(Stat.class, (args) -> {
			String input = args.next();
			if (!EnumUtils.isValidEnum(Stat.class, input)) {
				throw new IllegalArgumentException(input + " ist keine gültige Statistik");
			}
			return Stat.valueOf(input);
		});
	}
	
	@Override
	public void onEnable() {
		
		this.getDataFolder().mkdir();
		
		commandRegistrar = new CommandRegistrar(this);
		this.commandRegistrar.registerCommands(new CombatCommands());
		
		protocolManager = ProtocolLibrary.getProtocolManager();
		this.taskFactory = BukkitTaskChainFactory.create(this);
		
		Bukkit.getPluginManager().registerEvents(new BuffListener(), this);
		Bukkit.getPluginManager().registerEvents(new UpdateStatsEvents(), this);
		Bukkit.getPluginManager().registerEvents(new JoinEvents(), this);
		Bukkit.getPluginManager().registerEvents(new SkillTriggerListener(), this);
		Bukkit.getPluginManager().registerEvents(new CombatListener(), this);
		Bukkit.getPluginManager().registerEvents(new DamageIndicatorListener(this), this);
		Bukkit.getPluginManager().registerEvents(new Overdrawing.OverDrawListener(), this);
		Bukkit.getPluginManager().registerEvents(new ArcaneArrows.ArcaneArrowListener(), this);
		SkillHandler skillHandler = new SkillHandler();
		
		for (SkillRegistration register : SkillRegistration.values()) {
			skillHandler.register(register.getId(), register.getClazz());
		}
		
		CombatCooldown.startCooldownThread();
		PlayerUpdateThread.start();
		Bukkit.getScheduler().runTaskTimer(this, new BuffTimeoutThread(), 1, 15);
		Bukkit.getScheduler().runTaskTimer(this, new SkillCheckerThread(), 1, 1);
		Bukkit.getScheduler().runTaskTimer(this, new ProjectileThread(), 1, 1);
		Bukkit.getScheduler().runTaskTimer(this, new CastPointThread(), 1, 1);
		Bukkit.getScheduler().runTaskTimer(this, new RepeatableThread(), 1, 1);
		
		this.storage = new PlayerStorage(this);
	}
	
	@Override
	public void onDisable() {
		Bukkit.getOnlinePlayers().forEach(cur -> {
			this.storage.savePlayer( CombatPlayer.of(cur) );
			CombatPlayer.logout(cur);
		});
		this.storage.closePool();
	}

}
