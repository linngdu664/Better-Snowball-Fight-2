package com.linngdu664.bsf.entity.snowball.tracking_snowball.missiles_snowball;

import com.linngdu664.bsf.entity.snowball.tracking_snowball.MissileSnowballEntity;
import com.linngdu664.bsf.util.SnowballType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class LightMonsterTrackingSnowballEntity extends MissileSnowballEntity {
    public LightMonsterTrackingSnowballEntity(Level level, LivingEntity livingEntity, SnowballType type) {
        super(level, livingEntity, type);
    }

    public LightMonsterTrackingSnowballEntity(Level level, LivingEntity livingEntity, SnowballType type, float damage, float blazeDamage) {
        super(level, livingEntity, type, damage, blazeDamage);
    }

    public LightMonsterTrackingSnowballEntity(Level level, double x, double y, double z, SnowballType type) {
        super(level, x, y, z, type);
    }

    public LightMonsterTrackingSnowballEntity(Level level, double x, double y, double z, SnowballType type, float damage, float blazeDamage) {
        super(level, x, y, z, type, damage, blazeDamage);
    }
}
