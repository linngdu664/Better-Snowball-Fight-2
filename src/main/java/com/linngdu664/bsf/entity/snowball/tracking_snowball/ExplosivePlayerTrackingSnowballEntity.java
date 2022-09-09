package com.linngdu664.bsf.entity.snowball.tracking_snowball;

import com.linngdu664.bsf.entity.snowball.TrackingSnowballEntity;
import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.util.LaunchFunc;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import static com.linngdu664.bsf.util.BSFUtil.handleExplosion;

public class ExplosivePlayerTrackingSnowballEntity extends TrackingSnowballEntity {
    public ExplosivePlayerTrackingSnowballEntity(LivingEntity livingEntity, Level level, LaunchFunc launchFunc) {
        super(livingEntity, level);
        this.setLockFeet(true).setRange(10).setTargetClass(Player.class).setDamage(3).setBlazeDamage(5).setLaunchFrom(launchFunc.getLaunchForm());
        launchFunc.launchProperties(this);
        this.setItem(new ItemStack(ItemRegister.EXPLOSIVE_PLAYER_TRACKING_SNOWBALL.get()));
    }

    @Override
    protected Item getRegisterItem() {
        return ItemRegister.EXPLOSIVE_PLAYER_TRACKING_SNOWBALL.get();
    }

    @Override
    protected void onHit(@NotNull HitResult pResult) {
        super.onHit(pResult);
        handleExplosion(this, 1.5F);
        if (!level.isClientSide) {
            this.discard();
        }
    }
/*
    @Override
    protected @NotNull Item getDefaultItem() {
        return ItemRegister.EXPLOSIVE_PLAYER_TRACKING_SNOWBALL.get();
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult p_37258_) {
        super.onHitBlock(p_37258_);
        handleExplosion(this, 1.5F);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        handleExplosion(this, 1.5F);
    }*/
}
