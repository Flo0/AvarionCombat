package com.avarioncraft.AvarionCombat.util;

import java.util.Set;
import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import com.destroystokyo.paper.ParticleBuilder;
import com.google.common.collect.Sets;

public class ParticleUtil {
	
	private static final Vector EMPTY_VEC = new Vector(0D,0D,0D);
	
	public static void line(Particle particle, Location from, Location to, Consumer<ParticleBuilder> particleConsumer) {
		line(particle, from, to, EMPTY_VEC, EMPTY_VEC, particleConsumer);
	}
	
	public static void line(Particle particle, Location from, Location to, Vector fromOffset, Vector toOffset, Consumer<ParticleBuilder> particleConsumer) {
		from = from.clone();
		to = to.clone();
		from.add(fromOffset);
		to.add(toOffset);
		
		double distanceSq = from.distanceSquared(to);
		
		Vector direction = to.clone().subtract(from).toVector().normalize();
		Vector line = direction.clone();
		
		while(line.lengthSquared() < distanceSq) {
			particleConsumer.accept(particle.builder().location(from.clone().add(line)));
			line.add(direction);
		}
	}
	
	public static void circle(Particle particle, Location center, double radius, int points, Consumer<ParticleBuilder> particleConsumer) {
		double degFragment = 2 * Math.PI / points;
		double deg = 0D;
		Set<Vector> circlePoints = Sets.newHashSet();
		Vector circleArm = new Vector(0D, 0D, radius);
		double x = circleArm.getX();
		double z = circleArm.getZ();
		double sin;
		double cos;
		double vx;
		double vz;
		while(deg < 2 * Math.PI) {
			circlePoints.add(circleArm);
			sin = Math.sin(deg);
			cos = Math.cos(deg);
			vx = x * cos - z * sin;
			vz = x * sin + z * cos;
			circleArm = new Vector(vx, 0D, vz);
			deg += degFragment;
		}
		circlePoints.forEach(point -> particleConsumer.accept(particle.builder().location(center.clone().add(point))));
	}
	
	public static void helix(Particle particle, Location center, double radius, double height, double steepness, int points, Consumer<ParticleBuilder> particleConsumer) {
		helix(particle, center, radius, height, steepness, points, false, false, particleConsumer);
	}
	
	public static void helix(Particle particle, Location center, double radius, double height, double steepness, int points, boolean reversed, boolean inverse, Consumer<ParticleBuilder> particleConsumer) {
		double degFragment = 2 * Math.PI / points;
		double deg = 0D;
		Set<Vector> circlePoints = Sets.newHashSet();
		double xi = 0;
		double zi = 0;
		if(inverse) {
			zi = reversed ? -radius : radius;
		}else {
			xi = reversed ? -radius : radius;
		}
		Vector circleArm = new Vector(xi, 0D, zi);
		double x = circleArm.getX();
		double y = circleArm.getY();
		double z = circleArm.getZ();
		double sin;
		double cos;
		double vx;
		double vy = y;
		double vz;
		while(vy < height) {
			circlePoints.add(circleArm);
			sin = Math.sin(deg);
			cos = Math.cos(deg);
			vx = x * cos - z * sin;
			vz = x * sin + z * cos;
			circleArm = new Vector(vx, vy, vz);
			deg += degFragment;
			vy += steepness;
		}
		circlePoints.forEach(point -> particleConsumer.accept(particle.builder().location(center.clone().add(point))));
	}
	
	public static void doubleHelix(Particle particle, Location center, double radius, double height, double steepness, int points, Consumer<ParticleBuilder> particleConsumer) {
		helix(particle, center, radius, height, steepness, points, false, false, particleConsumer);
		helix(particle, center, radius, height, steepness, points, true, false, particleConsumer);
	}
	
	public static void quadrupleHelix(Particle particle, Location center, double radius, double height, double steepness, int points, Consumer<ParticleBuilder> particleConsumer) {
		helix(particle, center, radius, height, steepness, points, false, false, particleConsumer);
		helix(particle, center, radius, height, steepness, points, true, false, particleConsumer);
		helix(particle, center, radius, height, steepness, points, false, true, particleConsumer);
		helix(particle, center, radius, height, steepness, points, true, true, particleConsumer);
	}
	
}
