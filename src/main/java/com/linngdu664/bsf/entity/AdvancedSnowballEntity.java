package com.linngdu664.bsf.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Level;

public class AdvancedSnowballEntity extends Snowball {
    public AdvancedSnowballEntity(EntityType<? extends Snowball> entityType, Level level) {
        super(entityType, level);
    }

    public AdvancedSnowballEntity(Level level, LivingEntity livingEntity) {
        super(level, livingEntity);
    }

    public AdvancedSnowballEntity(Level level, double x, double y, double z) {
        super(level, x, y, z);
    }
}
