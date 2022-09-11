package com.linngdu664.bsf.entity.snowball;

import com.linngdu664.bsf.entity.BSFSnowballEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import static com.linngdu664.bsf.util.MovingAlgorithm.forceEffect;

public class ForceSnowballEntity extends BSFSnowballEntity {
    public Class <? extends Entity> targetClass;
    public double range;
    public double GM;
    public double boundaryR2;

    public ForceSnowballEntity(LivingEntity livingEntity, Level level) {
        super(livingEntity, level);
    }

    @Override
    public void tick() {
        super.tick();
        forceEffect(this, targetClass, range, GM, boundaryR2);
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
