package com.avarioncraft.AvarionCombat.entity;

import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftArrow;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftLivingEntity;

import com.avarioncraft.AvarionCombat.skills.vault.Vescerator.main.ArcaneArrows;
import com.avarioncraft.AvarionCombat.util.enums.ArcaneArrowType;

import lombok.Getter;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.EntityTippedArrow;
import net.minecraft.server.v1_13_R2.IMaterial;
import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.Items;
import net.minecraft.server.v1_13_R2.SoundEffect;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.minecraft.server.v1_13_R2.World;

public class ArcaneArrow extends EntityTippedArrow {
	
	public ArcaneArrow(org.bukkit.World world, ArcaneArrowType arrowType, ArcaneArrows skill) {
		this(((CraftWorld) world).getHandle(), ((CraftLivingEntity) skill.getCaster().getPlayer()).getHandle(), arrowType, skill);
	}
	
	public ArcaneArrow(World world, EntityLiving entityliving, ArcaneArrowType arrowType, ArcaneArrows skill) {
		super(world, entityliving);
		this.arcaneType = arrowType;
		this.velocityChanged = true;
		this.skill = skill;
		
		this.a(entityliving, 1F);
		world.addEntity(this);
		((CraftArrow) this.getBukkitEntity()).setPickupStatus(org.bukkit.entity.Arrow.PickupStatus.DISALLOWED);
	}
	
	@Getter
	private final ArcaneArrowType arcaneType;
	private final ArcaneArrows skill;
	
	//Arrow drop
	@Override
	protected net.minecraft.server.v1_13_R2.ItemStack getItemStack() {
		return new ItemStack( (IMaterial) Items.ARROW );
	}
	
	// Arrow collide
	@Override
	public void a(EntityLiving entity) {
		super.a(entity);
		skill.inflict(new Object[] {this}, entity.getBukkitLivingEntity());
	}
	
	//Arrow Hit Sound
	@Override
	protected SoundEffect i() {
		return SoundEffects.BLOCK_ANVIL_HIT;
	}
	
	@Override
	public void tick() {
		super.tick();
		if (this.onGround || this.inGround || this.inWater) return;
		this.arcaneType.particle.builder().offset(0.01, 0.01, 0.01).extra(0.01).location(this.bukkitEntity.getLocation()).receivers(48).spawn();
	}

}