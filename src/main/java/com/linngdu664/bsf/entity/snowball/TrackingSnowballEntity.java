package com.linngdu664.bsf.entity.snowball;

import com.linngdu664.bsf.entity.BSFSnowballEntity;
import com.linngdu664.bsf.util.LaunchFrom;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class TrackingSnowballEntity extends BSFSnowballEntity {
    public Class<? extends Entity> targetClass;
    public double range;

    public TrackingSnowballEntity(LivingEntity livingEntity, Level level) {
        super(livingEntity, level);
    }

    public TrackingSnowballEntity(LivingEntity livingEntity, Level level, float damage, float blazeDamage, double range, Class<? extends Entity> targetClass, LaunchFrom launchFrom) {
        super(livingEntity, level, 0, damage, blazeDamage, launchFrom);
        this.range = range;
        this.targetClass = targetClass;
    }

    public TrackingSnowballEntity setRange(double range) {
        this.range = range;
        return this;
    }

    public TrackingSnowballEntity setTargetClass(Class<? extends Entity> targetClass) {
        this.targetClass = targetClass;
        return this;
    }
}
