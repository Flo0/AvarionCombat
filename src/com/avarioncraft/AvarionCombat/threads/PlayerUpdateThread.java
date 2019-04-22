package com.avarioncraft.AvarionCombat.threads;

import java.util.Queue;

import org.bukkit.Bukkit;

import com.avarioncraft.AvarionCombat.core.AvarionCombat;
import com.avarioncraft.AvarionCombat.data.CombatPlayer;
import com.google.common.collect.Queues;

public class PlayerUpdateThread implements Runnable{
	
	private static int id;
	public static void start() {
		id = Bukkit.getScheduler().runTaskTimer(AvarionCombat.getPlugin(), new PlayerUpdateThread(), 20, 20).getTaskId();
	}
	public static void stop() {
		Bukkit.getScheduler().cancelTask(id);
	}
	
	private PlayerUpdateThread() {
		this(Queues.newArrayDeque(CombatPlayer.getAll()));
	}
	
	private PlayerUpdateThread(Queue<CombatPlayer> leftWork) {
		this.playerWorkload = leftWork;
		this.plugin = AvarionCombat.getPlugin();
	}
	
	private final AvarionCombat plugin;
	private long startRun = 0L;
	private Queue<CombatPlayer> playerWorkload;
	
	@Override
	public void run() {
		this.setup();
		this.work();
		this.clean();
	}
	
	private void setup() {
		if(this.playerWorkload.isEmpty()) {
			this.playerWorkload = Queues.newArrayDeque(CombatPlayer.getAll());
		}
		startRun = System.currentTimeMillis();
	}
	
	private void work() {
		while( (System.currentTimeMillis() - this.startRun) < 10) {
			if(this.playerWorkload.isEmpty()) break;
			this.playerWorkload.poll().calculateFull();
		}
	}
	
	private void clean() {
		if(this.playerWorkload.isEmpty()) return;
		Bukkit.getScheduler().runTaskLater(plugin, new PlayerUpdateThread(this.playerWorkload), 1);
	}

}
