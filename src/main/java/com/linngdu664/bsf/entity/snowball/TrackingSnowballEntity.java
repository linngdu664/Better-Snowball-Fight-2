package com.linngdu664.bsf.entity.snowball;

import com.linngdu664.bsf.entity.AdvancedSnowballEntity;
import com.linngdu664.bsf.entity.BSFSnowballEntity;
import com.linngdu664.bsf.util.SnowballType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class TrackingSnowballEntity extends BSFSnowballEntity {

    public TrackingSnowballEntity(LivingEntity livingEntity, Level level) {
        super(livingEntity, level);
    }

    public TrackingSnowballEntity(LivingEntity livingEntity, Level level, double punch, float damage, float blazeDamage) {
        super(livingEntity, level, punch, damage, blazeDamage);
    }
}
