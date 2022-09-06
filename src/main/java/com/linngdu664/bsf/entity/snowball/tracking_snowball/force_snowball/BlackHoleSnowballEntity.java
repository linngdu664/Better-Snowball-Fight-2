package com.linngdu664.bsf.entity.snowball.tracking_snowball.force_snowball;

import com.linngdu664.bsf.entity.BSFSnowballEntity;
import com.linngdu664.bsf.entity.snowball.tracking_snowball.ForceSnowballEntity;
import com.linngdu664.bsf.item.setter.ItemRegister;
import com.linngdu664.bsf.util.LaunchFunc;
import com.linngdu664.bsf.util.SnowballType;
import com.linngdu664.bsf.util.TrackingAlgorithm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class BlackHoleSnowballEntity extends BSFSnowballEntity {
    public int timer = 0;

    public BlackHoleSnowballEntity(LivingEntity livingEntity, Level level, LaunchFunc launchFunc) {
        super(livingEntity, level);
        this.setDamage(4).setBlazeDamage(6).setLaunchFrom(launchFunc.getLaunchForm());
        launchFunc.launchProperties(this);
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult p_37258_) {
        super.onHitBlock(p_37258_);
        if (!level.isClientSide) {
            this.discard();
        }
    }

    @Override
    public void tick() {
        super.tick();
        TrackingAlgorithm.gravityTracking(this, Monster.class, 20, 1, false, true, false, true);
        TrackingAlgorithm.gravityTracking(this, Projectile.class, 20, 1, false, true, false, true);
        if (timer == 100 && !level.isClientSide) {
            this.discard();
        }
        timer++;
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return ItemRegister.BLACK_HOLE_SNOWBALL.get();
    }
}
