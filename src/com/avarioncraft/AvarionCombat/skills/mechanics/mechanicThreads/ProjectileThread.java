package com.avarioncraft.AvarionCombat.skills.mechanics.mechanicThreads;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;

import com.avarioncraft.AvarionCombat.skills.mechanics.SkillProjectile;
import com.google.common.collect.Queues;

public class ProjectileThread implements Runnable{
	
	public static Queue<SkillProjectile> memory = Queues.newArrayDeque();
	private Queue<SkillProjectile> projectiles;
	
	@Override
	public void run() {
		
		this.projectiles = new LinkedList<SkillProjectile>(memory);
		
		memory.clear();
		
		SkillProjectile projectile;
		
		while(!this.projectiles.isEmpty()) {
			
			projectile= this.projectiles.poll();
			
			Optional<Block> block = projectile.willCollide();
			Optional<LivingEntity> target = projectile.willImpact();
			
			if(block.isPresent()) {
				projectile.onCollide(block.get());
				continue;
			}else
			
			if(target.isPresent()) {
				projectile.onImpact(target.get());
				continue;
			}else
			
			if(projectile.willRunout()) {
				projectile.onRunout();
				continue;
			}
			
			projectile.onTick();
			
			memory.add(projectile);
			
		}
		
		
	}

}
