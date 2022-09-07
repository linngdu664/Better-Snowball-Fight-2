package com.linngdu664.bsf.entity.snowball.tracking_snowball;

import com.linngdu664.bsf.entity.snowball.TrackingSnowballEntity;
import com.linngdu664.bsf.item.setter.ItemRegister;
import com.linngdu664.bsf.util.LaunchFunc;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class LightPlayerTrackingSnowballEntity extends TrackingSnowballEntity {
    public LightPlayerTrackingSnowballEntity(LivingEntity livingEntity, Level level, LaunchFunc launchFunc) {
        super(livingEntity, level);
        this.setRange(20).setTargetClass(Player.class).setLaunchFrom(launchFunc.getLaunchForm());
        launchFunc.launchProperties(this);
        this.setItem(new ItemStack(ItemRegister.LIGHT_PLAYER_TRACKING_SNOWBALL.get()));
    }

    @Override
    protected Item getRegisterItem() {
        return ItemRegister.LIGHT_PLAYER_TRACKING_SNOWBALL.get();
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
        return ItemRegister.LIGHT_PLAYER_TRACKING_SNOWBALL.get();
    }*/
}