package com.linngdu664.bsf.entity.snowball.tracking_snowball;

import com.linngdu664.bsf.entity.BSFSnowballEntity;
import com.linngdu664.bsf.entity.snowball.TrackingSnowballEntity;
import com.linngdu664.bsf.util.BSFUtil;
import com.linngdu664.bsf.util.LaunchFrom;
import com.linngdu664.bsf.util.SnowballType;
import com.linngdu664.bsf.util.TrackingAlgorithm;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class MissileSnowballEntity extends BSFSnowballEntity {
    public float v0;
    public float maxTurningAngleCos;
    public float maxTurningAngleSin;
    public int timer = 0;
    public Class <? extends Entity> targetClass;
    public double range;

    public MissileSnowballEntity(LivingEntity livingEntity, Level level) {
        super(livingEntity, level);
    }

    public MissileSnowballEntity(LivingEntity livingEntity, Level level, float damage, float blazeDamage, double range, Class<? extends Entity> targetClass, LaunchFrom launchFrom) {
        super(livingEntity, level, 0, damage, blazeDamage, launchFrom);
        this.targetClass = targetClass;
        this.range = range;
    }

    @Override
    public void tick() {
        super.tick();
        if (timer == 0) {
            Vec3 vec3 = this.getDeltaMovement();
            v0 = (float) Math.sqrt(BSFUtil.modSqr(vec3));
            maxTurningAngleCos = Mth.cos(7.1619724F * v0 * Mth.DEG_TO_RAD);
            maxTurningAngleSin = Mth.sin(7.1619724F * v0 * Mth.DEG_TO_RAD);
        }
        TrackingAlgorithm.missilesTracking(this, targetClass, range, true, maxTurningAngleCos, maxTurningAngleSin);
        timer++;
    }
}
