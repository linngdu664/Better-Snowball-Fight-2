package com.linngdu664.bsf.entity.snowball.special;

import com.linngdu664.bsf.entity.BSFSnowballEntity;
import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.util.LaunchFunc;
import com.linngdu664.bsf.util.TargetGetter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SubSpaceSnowball extends BSFSnowballEntity {
    private int timer = 0;

    public SubSpaceSnowball(LivingEntity livingEntity, Level level, LaunchFunc launchFunc) {
        super(livingEntity, level);
        this.setLaunchFrom(launchFunc.getLaunchForm());
        launchFunc.launchProperties(this);
        this.setItem(new ItemStack(ItemRegister.EXPLOSIVE_MONSTER_TRACKING_SNOWBALL.get()));
        this.setNoGravity(true);
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult p_37258_) {
        super.onHitBlock(p_37258_);
        spawnMoreParticles(level);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        spawnMoreParticles(level);
    }

    @Override
    public void tick() {
        super.tick();
        List<BSFSnowballEntity> list = TargetGetter.getTargetList(this, BSFSnowballEntity.class, 2.5);
        for (BSFSnowballEntity snowball : list) {
            if (!level.isClientSide) {
                ((ServerLevel) level).sendParticles(ParticleTypes.DRAGON_BREATH, snowball.getX(), snowball.getY(), snowball.getZ(), 8, 0, 0, 0, 0.05);
            }
            snowball.discard();
            if (damage < 15.0F) {
                damage += 1.0F;
                blazeDamage += 1.0F;
            }
        }
        if (timer == 150) {
            if (!level.isClientSide) {
                this.discard();
            }
        }
        timer++;
    }

    @Override
    protected void onHit(@NotNull HitResult pResult) {
        super.onHit(pResult);
        if (!level.isClientSide) {
            this.discard();
        }
    }

    private void spawnMoreParticles(Level level) {
        if (!level.isClientSide) {
            ((ServerLevel) level).sendParticles(ParticleTypes.ITEM_SNOWBALL, this.getX(), this.getY(), this.getZ(), (int) damage * 2, 0, 0, 0, 0);
            ((ServerLevel) level).sendParticles(ParticleTypes.SNOWFLAKE, this.getX(), this.getY(), this.getZ(), (int) damage * 2, 0, 0, 0, 0.04);
        }
    }
}
