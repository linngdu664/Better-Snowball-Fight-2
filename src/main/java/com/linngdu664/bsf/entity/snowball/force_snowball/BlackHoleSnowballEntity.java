package com.linngdu664.bsf.entity.snowball.force_snowball;

import com.linngdu664.bsf.entity.BSFSnowballEntity;
import com.linngdu664.bsf.item.setter.ItemRegister;
import com.linngdu664.bsf.util.LaunchFunc;
import com.linngdu664.bsf.util.MovingAlgorithm;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import static com.linngdu664.bsf.util.BSFUtil.handleExplosion;

public class BlackHoleSnowballEntity extends BSFSnowballEntity {
    public int timer = 0;
    public int startTime = 20;
    public int endTime = 150;

    public BlackHoleSnowballEntity(LivingEntity livingEntity, Level level, LaunchFunc launchFunc) {
        super(livingEntity, level);
        this.setDamage(4).setBlazeDamage(6).setLaunchFrom(launchFunc.getLaunchForm());
        launchFunc.launchProperties(this);
        this.setItem(new ItemStack(ItemRegister.BLACK_HOLE_SNOWBALL.get()));
        this.setNoGravity(true);
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult p_37258_) {
        super.onHitBlock(p_37258_);
        handleExplosion(this, 6.0F);
        if (!level.isClientSide) {
            this.discard();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (timer == startTime && !level.isClientSide) {
            Vec3 vec3 = this.getDeltaMovement();
            this.setDeltaMovement(vec3.x * 0.25, vec3.y * 0.25, vec3.z * 0.25);
            ((ServerLevel) level).sendParticles(ParticleTypes.SNOWFLAKE, this.getX(), this.getY(), this.getZ(), 200, 0, 0, 0, 0.32);
        }
        if (timer > startTime) {
            MovingAlgorithm.forceEffect(this, Entity.class, 50, 16, 16);
            ((ServerLevel) level).sendParticles(ParticleTypes.DRAGON_BREATH, this.getX(), this.getY(), this.getZ(), 8, 0, 0, 0, 0.12);
        }
        if (timer == endTime) {
            handleExplosion(this, 6.0F);
            if (!level.isClientSide) {
                this.discard();
            }
        }
        timer++;
    }
/*
    @Override
    protected @NotNull Item getDefaultItem() {
        return ItemRegister.BLACK_HOLE_SNOWBALL.get();
    }*/
}
