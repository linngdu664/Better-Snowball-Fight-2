package com.linngdu664.bsf.entity.snowball;

import com.linngdu664.bsf.entity.BSFSnowballEntity;
import com.linngdu664.bsf.util.MovingAlgorithm;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class TrackingSnowballEntity extends BSFSnowballEntity {
    public float v0;
    public float maxTurningAngleCos;
    public float maxTurningAngleSin;
    public Class<? extends Entity> targetClass;
    public double range;
    public boolean lockFeet = false;
    private boolean init = true;

    public TrackingSnowballEntity(LivingEntity livingEntity, Level level) {
        super(livingEntity, level);
    }

    @Override
    public void tick() {
        super.tick();
        if (init) {
            Vec3 vec3 = this.getDeltaMovement();
            v0 = (float) vec3.length();
            maxTurningAngleCos = Mth.cos(7.1619724F * v0 * Mth.DEG_TO_RAD);
            maxTurningAngleSin = Mth.sin(7.1619724F * v0 * Mth.DEG_TO_RAD);
            init = false;
        }
        MovingAlgorithm.missilesTracking(this, targetClass, range, true, maxTurningAngleCos, maxTurningAngleSin, lockFeet);
    }

    public TrackingSnowballEntity setRange(double range) {
        this.range = range;
        return this;
    }

    public TrackingSnowballEntity setTargetClass(Class<? extends Entity> targetClass) {
        this.targetClass = targetClass;
        return this;
    }

    public TrackingSnowballEntity setLockFeet(boolean lockFeet) {
        this.lockFeet = lockFeet;
        return this;
    }
}
