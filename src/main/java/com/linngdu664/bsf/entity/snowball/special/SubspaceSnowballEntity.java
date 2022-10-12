package com.linngdu664.bsf.entity.snowball.special;

import com.linngdu664.bsf.entity.AbstractBSFSnowballEntity;
import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.util.LaunchFunc;
import com.linngdu664.bsf.util.TargetGetter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SubspaceSnowballEntity extends AbstractBSFSnowballEntity {
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
            List<AbstractBSFSnowballEntity> list = TargetGetter.getTargetList(this, AbstractBSFSnowballEntity.class, 2.5);
            for (AbstractBSFSnowballEntity snowball : list) {
                // todo: adjust the particles?
                ((ServerLevel) level).sendParticles(ParticleTypes.DRAGON_BREATH, snowball.getX(), snowball.getY(), snowball.getZ(), 8, 0, 0, 0, 0.05);
                snowball.discard();
                if (snowball instanceof SubspaceSnowballEntity) {
                    ((ServerLevel) level).sendParticles(ParticleTypes.DRAGON_BREATH, this.getX(), this.getY(), this.getZ(), 16, 0, 0, 0, 0.05);
                    this.discard();
                }
                if (damage < 15.0F) {
                    damage += snowball.getPower();
                    blazeDamage += snowball.getPower();
                }
            }
            List<Snowball> list2 = TargetGetter.getTargetList(this, Snowball.class, 2.5);
            for (Snowball snowball : list2) {
                ((ServerLevel) level).sendParticles(ParticleTypes.DRAGON_BREATH, snowball.getX(), snowball.getY(), snowball.getZ(), 8, 0, 0, 0, 0.05);
                snowball.discard();
                if (damage < 15.0F) {
                    damage += 1;
                    blazeDamage += 1;
                }
            }
            if (timer == 150) {
                ((ServerLevel) level).sendParticles(ParticleTypes.DRAGON_BREATH, this.getX(), this.getY(), this.getZ(), 16, 0, 0, 0, 0.05);
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
