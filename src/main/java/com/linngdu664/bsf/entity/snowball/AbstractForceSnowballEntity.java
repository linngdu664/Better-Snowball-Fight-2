package com.linngdu664.bsf.entity.snowball;

import com.linngdu664.bsf.entity.AbstractBSFSnowballEntity;
import com.linngdu664.bsf.util.MovingAlgorithm;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public abstract class AbstractForceSnowballEntity extends AbstractBSFSnowballEntity {
    private Class<? extends Entity> targetClass;
    private double range;
    private double GM;
    private double boundaryR2;

    public AbstractForceSnowballEntity(LivingEntity livingEntity, Level level) {
        super(livingEntity, level);
    }

    @Override
    public void tick() {
        super.tick();
        MovingAlgorithm.forceEffect(this, targetClass, range, GM, boundaryR2);
    }

    public AbstractForceSnowballEntity setRange(double range) {
        this.range = range;
        return this;
    }

    public AbstractForceSnowballEntity setTargetClass(Class<? extends Entity> targetClass) {
        this.targetClass = targetClass;
        return this;
    }

    public AbstractForceSnowballEntity setGM(double GM) {
        this.GM = GM;
        return this;
    }

    public AbstractForceSnowballEntity setBoundaryR2(double boundaryR2) {
        this.boundaryR2 = boundaryR2;
        return this;
    }
}
