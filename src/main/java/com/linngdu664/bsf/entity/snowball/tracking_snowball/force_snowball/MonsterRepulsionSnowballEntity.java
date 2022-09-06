package com.linngdu664.bsf.entity.snowball.tracking_snowball.force_snowball;

import com.linngdu664.bsf.entity.snowball.tracking_snowball.ForceSnowballEntity;
import com.linngdu664.bsf.item.setter.ItemRegister;
import com.linngdu664.bsf.util.LaunchFunc;
import com.linngdu664.bsf.util.SnowballType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class MonsterRepulsionSnowballEntity extends ForceSnowballEntity {
    public MonsterRepulsionSnowballEntity(LivingEntity livingEntity, Level level, LaunchFunc launchFunc) {
        super(livingEntity, level);
        this.setRange(20).setTargetClass(Monster.class).setGM(-0.5).setDamage(4).setBlazeDamage(6).setLaunchFrom(launchFunc.getLaunchForm());
        launchFunc.launchProperties(this);
    }

    @Override
    protected void onHit(@NotNull HitResult pResult) {
        super.onHit(pResult);
        if (!level.isClientSide) {
            this.discard();
        }
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return ItemRegister.REPULSION_SNOWBALL_TO_MONSTER.get();
    }
}
