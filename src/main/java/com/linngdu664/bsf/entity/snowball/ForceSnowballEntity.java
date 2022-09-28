package com.linngdu664.bsf.entity.snowball;

import com.linngdu664.bsf.entity.BSFSnowballEntity;
import com.linngdu664.bsf.util.MovingAlgorithm;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public abstract class ForceSnowballEntity extends BSFSnowballEntity {
    private Class<? extends Entity> targetClass;
    private double range;
    private double GM;
    private double boundaryR2;

    public ForceSnowballEntity(LivingEntity livingEntity, Level level) {
        super(livingEntity, level);
    }

    @Override
    public void tick() {
        super.tick();
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
