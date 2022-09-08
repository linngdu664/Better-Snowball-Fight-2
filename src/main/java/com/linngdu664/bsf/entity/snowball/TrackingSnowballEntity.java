package com.linngdu664.bsf.entity.snowball;

import com.linngdu664.bsf.entity.BSFSnowballEntity;
import com.linngdu664.bsf.util.BSFUtil;
import com.linngdu664.bsf.util.LaunchFrom;
import com.linngdu664.bsf.util.MovingAlgorithm;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TrackingSnowballEntity extends BSFSnowballEntity {
    public float v0;
    public float maxTurningAngleCos;
    public float maxTurningAngleSin;
    public int timer = 0;
    public Class <? extends Entity> targetClass;
    public double range;
    public boolean lockFeet = false;

    public TrackingSnowballEntity(LivingEntity livingEntity, Level level) {
        super(livingEntity, level);
    }
/*
    public TrackingSnowballEntity(LivingEntity livingEntity, Level level, float damage, float blazeDamage, double range, Class<? extends Entity> targetClass, LaunchFrom launchFrom) {
        super(livingEntity, level, 0, damage, blazeDamage, launchFrom);
        this.targetClass = targetClass;
        this.range = range;
    }*/

    @Override
    public void tick() {
        super.tick();
        if (timer == 0) {
            Vec3 vec3 = this.getDeltaMovement();
            v0 = (float) Math.sqrt(BSFUtil.modSqr(vec3));
            maxTurningAngleCos = Mth.cos(7.1619724F * v0 * Mth.DEG_TO_RAD);
            maxTurningAngleSin = Mth.sin(7.1619724F * v0 * Mth.DEG_TO_RAD);
        }
        MovingAlgorithm.missilesTracking(this, targetClass, range, true, maxTurningAngleCos, maxTurningAngleSin, lockFeet);
        timer++;
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
