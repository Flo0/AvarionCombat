package com.avarioncraft.AvarionCombat.entity;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.server.v1_13_R2.DamageSource;
import net.minecraft.server.v1_13_R2.EntityArmorStand;
import net.minecraft.server.v1_13_R2.EnumMoveType;
import net.minecraft.server.v1_13_R2.World;

public class DamageIndicatorEntity extends EntityArmorStand {

    @SuppressWarnings("deprecation")
	public DamageIndicatorEntity(ChatColor color, World world, Location loc, String damage) {
	super(world, loc.getX(), loc.getY(), loc.getZ());
	ticksLived = 10;
	this.setCustomNameVisible(true);
	ArmorStand as = (ArmorStand) this.getBukkitEntity();
	as.setCustomName(color + damage);
	as.setMarker(true);
	as.setSmall(true);
	as.setVisible(false);
	as.setPersistent(false);
    }

    @Override
    public void tick() {

	this.motY += 0.0085;

	this.move(EnumMoveType.SELF, this.motX, this.motY, this.motZ);

	this.lastX = this.locX;
	this.lastY = this.locY;
	this.lastZ = this.locZ;

	this.motX *= ThreadLocalRandom.current().nextDouble(0.0, 0.5);
	this.motY *= 0.02;
	this.motZ *= ThreadLocalRandom.current().nextDouble(0.0, 0.5);

	if (ticksLived > 100) {
	    this.die();
	    return;
	}
	ticksLived++;
    }
    
    @Override
    public boolean damageEntity(DamageSource arg0, float arg1) {
    	return false;
    }
}