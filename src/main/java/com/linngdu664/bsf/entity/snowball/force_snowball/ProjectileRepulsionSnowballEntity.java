package com.linngdu664.bsf.entity.snowball.force_snowball;

import com.linngdu664.bsf.entity.snowball.ForceSnowballEntity;
import com.linngdu664.bsf.item.setter.ItemRegister;
import com.linngdu664.bsf.util.LaunchFunc;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class ProjectileRepulsionSnowballEntity extends ForceSnowballEntity {
    public ProjectileRepulsionSnowballEntity(LivingEntity livingEntity, Level level, LaunchFunc launchFunc) {
        super(livingEntity, level);
        this.setRange(20).setTargetClass(Projectile.class).setGM(-16).setBoundaryR2(16).setDamage(4).setBlazeDamage(6).setLaunchFrom(launchFunc.getLaunchForm());
        launchFunc.launchProperties(this);
        this.setItem(new ItemStack(ItemRegister.PROJECTILE_REPULSION_SNOWBALL.get()));
    }

    @Override
    protected Item getRegisterItem() {
        return ItemRegister.PROJECTILE_REPULSION_SNOWBALL.get();
    }

    @Override
    protected void onHit(@NotNull HitResult pResult) {
        super.onHit(pResult);
        if (!level.isClientSide) {
            this.discard();
        }
    }
/*
    @Override
    protected @NotNull Item getDefaultItem() {
        return ItemRegister.PROJECTILE_REPULSION_SNOWBALL.get();
    }*/
}