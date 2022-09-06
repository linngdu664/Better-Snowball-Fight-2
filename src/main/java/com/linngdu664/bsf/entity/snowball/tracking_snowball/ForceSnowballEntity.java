package com.linngdu664.bsf.entity.snowball.tracking_snowball;

import com.linngdu664.bsf.entity.snowball.TrackingSnowballEntity;
import com.linngdu664.bsf.util.SnowballType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class ForceSnowballEntity extends TrackingSnowballEntity {

    public ForceSnowballEntity(LivingEntity livingEntity, Level level) {
        super(livingEntity, level);
    }

    public ForceSnowballEntity(LivingEntity livingEntity, Level level, double punch, float damage, float blazeDamage) {
        super(livingEntity, level, punch, damage, blazeDamage);
    }
}
