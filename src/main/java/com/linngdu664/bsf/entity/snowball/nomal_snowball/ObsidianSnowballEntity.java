package com.linngdu664.bsf.entity.snowball.nomal_snowball;

import com.linngdu664.bsf.entity.BSFSnowballEntity;
import com.linngdu664.bsf.item.setter.ItemRegister;
import com.linngdu664.bsf.util.LaunchFunc;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class ObsidianSnowballEntity extends BSFSnowballEntity {
    public ObsidianSnowballEntity(LivingEntity livingEntity, Level level, LaunchFunc launchFunc) {
        super(livingEntity, level);
        this.setLaunchFrom(launchFunc.getLaunchForm()).setDamage(6).setBlazeDamage(8);
        launchFunc.launchProperties(this);
        this.setItem(new ItemStack(ItemRegister.OBSIDIAN_SNOWBALL.get()));

    }

    //This is only used for dispenser
    public ObsidianSnowballEntity(Level level, double x, double y, double z) {
        super(level, x, y, z);
        this.setDamage(6).setBlazeDamage(8);
        this.setItem(new ItemStack(ItemRegister.OBSIDIAN_SNOWBALL.get()));

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
        return ItemRegister.OBSIDIAN_SNOWBALL.get();
    }*/
}