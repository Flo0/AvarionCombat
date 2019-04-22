package com.avarioncraft.AvarionCombat.skills.mechanics;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public interface SkillProjectile{
	
	public Player getShooter();
	public boolean doesBounce();
	public void setBounce(boolean doesBounce);
	public Location getLocation();
	public void setVelocity(Vector velocity);
	public Vector getVelocity();
	public BoundingBox getBoundingBox();
    public double getHeight();
    public double getWidth();
    public int maxTicksLiving();
    public boolean isOnGround();
    public boolean teleport(Location location);
    public List<Entity> getNearbyEntities(double x, double y, double z);
    public List<LivingEntity> getNearbyLivingEntities(double xz, double height);
    public Location getOrigin();
    public Set<LivingEntity> getTargets();
    public boolean stopAtEntity();
    public boolean stopAtBlock();
    public Optional<LivingEntity> willImpact();
    public boolean willRunout();
    public Optional<Block> willCollide();
    public void onShoot();
    public void onTick();
    public void onImpact(LivingEntity target);
    public void onCollide(Block block);
    public void onRunout();
	
}
