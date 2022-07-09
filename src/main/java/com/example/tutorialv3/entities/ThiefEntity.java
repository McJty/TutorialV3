package com.example.tutorialv3.entities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class ThiefEntity extends PathfinderMob {
	private boolean stealing = false;

	public ThiefEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new AvoidEntityAndEscapeGoal<>(this, Player.class, 6.0F, 1.2, 1.2));
		this.goalSelector.addGoal(1, new ThiefFindChestGoal(this, 1.3));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		stealing = tag.getBoolean("Stealing");
	}

	@Override
	public boolean save(CompoundTag tag) {
		tag.putBoolean("Stealing", stealing);
		return super.save(tag);
	}

	public boolean isStealing() {
		return stealing;
	}

	public void setStealing(boolean stealing) {
		this.stealing = stealing;
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	public static AttributeSupplier.Builder prepareAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.ATTACK_DAMAGE, 3.0)
				.add(Attributes.MAX_HEALTH, 20.0)
				.add(Attributes.FOLLOW_RANGE, 40.0)
				.add(Attributes.MOVEMENT_SPEED, 0.3);
	}
}
