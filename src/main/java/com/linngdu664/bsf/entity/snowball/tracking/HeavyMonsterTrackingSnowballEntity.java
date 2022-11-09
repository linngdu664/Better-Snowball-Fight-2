package com.linngdu664.bsf.entity.snowball.tracking;

import com.linngdu664.bsf.entity.EntityRegister;
import com.linngdu664.bsf.entity.snowball.AbstractTrackingSnowballEntity;
import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.util.LaunchFunc;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class HeavyMonsterTrackingSnowballEntity extends AbstractTrackingSnowballEntity {
    public HeavyMonsterTrackingSnowballEntity(LivingEntity livingEntity, Level level, LaunchFunc launchFunc) {
        super(EntityRegister.HEAVY_MONSTER_TRACKING_SNOWBALL.get(), livingEntity, level);
        this.setRange(10).setTargetClass(Monster.class).setDamage(4).setBlazeDamage(6).setLaunchFrom(launchFunc.getLaunchForm());
        launchFunc.launchProperties(this);
        this.setItem(new ItemStack(ItemRegister.HEAVY_MONSTER_TRACKING_SNOWBALL.get()));
    }

    public HeavyMonsterTrackingSnowballEntity(EntityType<HeavyMonsterTrackingSnowballEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public Item getCorrespondingItem() {
        return ItemRegister.HEAVY_MONSTER_TRACKING_SNOWBALL.get();
    }

    @Override
    protected void onHit(@NotNull HitResult pResult) {
        super.onHit(pResult);
        if (!level.isClientSide) {
            this.discard();
        }
    }

    public float getPower() {
        return 1.5f;
    }
}
