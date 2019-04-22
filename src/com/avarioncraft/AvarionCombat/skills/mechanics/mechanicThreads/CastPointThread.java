package com.avarioncraft.AvarionCombat.skills.mechanics.mechanicThreads;

import java.util.LinkedList;
import java.util.Queue;

import com.avarioncraft.AvarionCombat.skills.mechanics.CastPoint;
import com.google.common.collect.Queues;

public class CastPointThread implements Runnable{

	public static Queue<CastPoint> memory = Queues.newArrayDeque();
	private Queue<CastPoint> points;
	
	@Override
	public void run() {
		
		this.points = new LinkedList<CastPoint>(memory);
		
		memory.clear();
		
		CastPoint point;
		
		while(!this.points.isEmpty()) {
			
			point = points.poll();
			
			point.onTick();
			
			if(!point.isDone()) {
				point.onRunout();
				memory.add(point);
			}
			
		}
	}
	
}
