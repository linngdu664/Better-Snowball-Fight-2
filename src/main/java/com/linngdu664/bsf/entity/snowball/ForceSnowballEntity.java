package com.linngdu664.bsf.entity.snowball;

import com.linngdu664.bsf.entity.BSFSnowballEntity;
import com.linngdu664.bsf.util.LaunchFrom;
import com.linngdu664.bsf.util.MovingAlgorithm;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class ForceSnowballEntity extends BSFSnowballEntity {
    public Class <? extends Entity> targetClass;
    public double range;
    public double GM;
    public double boundaryR2;

    public ForceSnowballEntity(LivingEntity livingEntity, Level level) {
        super(livingEntity, level);
    }

    public ForceSnowballEntity(LivingEntity livingEntity, Level level, float damage, float blazeDamage, double range, Class<? extends Entity> targetClass, double GM, LaunchFrom launchFrom) {
        super(livingEntity, level, 0, damage, blazeDamage, launchFrom);
        this.targetClass = targetClass;
        this.range = range;
        this.GM = GM;
    }

    @Override
    public void tick() {
        super.tick();
        //MovingAlgorithm.gravityTracking(this, targetClass, range, GM, false, true, false, true);
        MovingAlgorithm.forceEffect(this, targetClass, range, GM, boundaryR2);
    }

    public ForceSnowballEntity setRange(double range) {
        this.range = range;
        return this;
    }

    public ForceSnowballEntity setTargetClass(Class<? extends Entity> targetClass) {
        this.targetClass = targetClass;
        return this;
    }

    public ForceSnowballEntity setGM(double GM) {
        this.GM = GM;
        return this;
    }

    public ForceSnowballEntity setBoundaryR2(double boundaryR2) {
        this.boundaryR2 = boundaryR2;
        return this;
    }
}
