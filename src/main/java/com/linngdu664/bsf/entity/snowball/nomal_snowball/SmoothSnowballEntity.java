package com.linngdu664.bsf.entity.snowball.nomal_snowball;

import com.linngdu664.bsf.entity.BSFSnowballEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class SmoothSnowballEntity extends BSFSnowballEntity {

    public SmoothSnowballEntity(LivingEntity livingEntity, Level level) {
        super(livingEntity, level);
    }

    public SmoothSnowballEntity(LivingEntity livingEntity, Level level, double punch, float damage, float blazeDamage) {
        super(livingEntity, level, punch, damage, blazeDamage);
    }
}
