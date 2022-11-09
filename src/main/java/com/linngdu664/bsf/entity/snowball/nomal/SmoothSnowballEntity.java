package com.linngdu664.bsf.entity.snowball.nomal;

import com.linngdu664.bsf.entity.AbstractBSFSnowballEntity;
import com.linngdu664.bsf.entity.EntityRegister;
import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.util.LaunchFunc;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.system.CallbackI;

public class SmoothSnowballEntity extends AbstractBSFSnowballEntity {
    public SmoothSnowballEntity(LivingEntity livingEntity, Level level, LaunchFunc launchFunc) {
        super(EntityRegister.SMOOTH_SNOWBALL.get(), livingEntity, level);
        this.setLaunchFrom(launchFunc.getLaunchForm());
        launchFunc.launchProperties(this);
        this.setItem(new ItemStack(ItemRegister.SMOOTH_SNOWBALL.get()));
    }

    //This is only used for dispenser
    public SmoothSnowballEntity(Level level, double x, double y, double z) {
        super(EntityRegister.SMOOTH_SNOWBALL.get(), level, x, y, z);
        this.setItem(new ItemStack(ItemRegister.SMOOTH_SNOWBALL.get()));
    }

    public SmoothSnowballEntity(EntityType<? extends SmoothSnowballEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public Item getCorrespondingItem() {
        return ItemRegister.SMOOTH_SNOWBALL.get();
    }

    @Override
    protected void onHit(@NotNull HitResult pResult) {
        super.onHit(pResult);
        if (!level.isClientSide) {
            this.discard();
        }
    }
}
