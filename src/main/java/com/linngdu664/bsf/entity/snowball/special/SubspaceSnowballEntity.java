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
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SubspaceSnowballEntity extends BSFSnowballEntity {
    private int timer = 0;

    public SubspaceSnowballEntity(LivingEntity livingEntity, Level level, LaunchFunc launchFunc) {
        super(livingEntity, level);
        this.setLaunchFrom(launchFunc.getLaunchForm());
        launchFunc.launchProperties(this);
        this.setItem(new ItemStack(ItemRegister.SUBSPACE_SNOWBALL.get()));
        this.setNoGravity(true);
    }

    @Override
    public void tick() {
        super.tick();
        if (!level.isClientSide) {
            List<BSFSnowballEntity> list = TargetGetter.getTargetList(this, BSFSnowballEntity.class, 2.5);
            for (BSFSnowballEntity snowball : list) {
                // todo: adjust the particles?
                ((ServerLevel) level).sendParticles(ParticleTypes.DRAGON_BREATH, snowball.getX(), snowball.getY(), snowball.getZ(), 8, 0, 0, 0, 0.05);
                snowball.discard();
                if (snowball instanceof SubspaceSnowballEntity) {
                    ((ServerLevel) level).sendParticles(ParticleTypes.DRAGON_BREATH, this.getX(), this.getY(), this.getZ(), 8, 0, 0, 0, 0.05);
                    this.discard();
                }
                if (damage < 15.0F) {
                    damage += 1.0F;
                    blazeDamage += 1.0F;
                }
            }
            if (timer == 150) {
                this.discard();
            }
            timer++;
        }
    }

    @Override
    protected void onHit(@NotNull HitResult pResult) {
        super.onHit(pResult);
        if (!level.isClientSide) {
            // todo: adjust the particles?
            ((ServerLevel) level).sendParticles(ParticleTypes.ITEM_SNOWBALL, this.getX(), this.getY(), this.getZ(), (int) damage * 2, 0, 0, 0, 0);
            ((ServerLevel) level).sendParticles(ParticleTypes.SNOWFLAKE, this.getX(), this.getY(), this.getZ(), (int) damage * 2, 0, 0, 0, 0.04);
            this.discard();
        }
    }
}
