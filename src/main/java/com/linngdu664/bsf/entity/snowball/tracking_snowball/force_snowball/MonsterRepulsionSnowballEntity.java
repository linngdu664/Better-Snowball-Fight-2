package com.linngdu664.bsf.entity.snowball.tracking_snowball.force_snowball;

import com.linngdu664.bsf.entity.snowball.tracking_snowball.ForceSnowballEntity;
import com.linngdu664.bsf.util.SnowballType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class MonsterRepulsionSnowballEntity extends ForceSnowballEntity {
    public MonsterRepulsionSnowballEntity(Level level, LivingEntity livingEntity, SnowballType type) {
        super(level, livingEntity, type);
    }

    public MonsterRepulsionSnowballEntity(Level level, LivingEntity livingEntity, SnowballType type, float damage, float blazeDamage) {
        super(level, livingEntity, type, damage, blazeDamage);
    }

    public MonsterRepulsionSnowballEntity(Level level, double x, double y, double z, SnowballType type) {
        super(level, x, y, z, type);
    }

    public MonsterRepulsionSnowballEntity(Level level, double x, double y, double z, SnowballType type, float damage, float blazeDamage) {
        super(level, x, y, z, type, damage, blazeDamage);
    }
}
