package com.linngdu664.bsf.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Level;

public class AdvancedSnowballEntity extends Snowball {
    public AdvancedSnowballEntity(EntityType<? extends Snowball> p_37391_, Level p_37392_) {
        super(p_37391_, p_37392_);
    }

    public AdvancedSnowballEntity(Level p_37399_, LivingEntity p_37400_) {
        super(p_37399_, p_37400_);
    }

    public AdvancedSnowballEntity(Level p_37394_, double p_37395_, double p_37396_, double p_37397_) {
        super(p_37394_, p_37395_, p_37396_, p_37397_);
    }
}
