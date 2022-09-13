package com.linngdu664.bsf.entity.snowball.nomal_snowball;

import com.linngdu664.bsf.entity.BSFSnowballEntity;
import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.util.LaunchFunc;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class FrozenSnowballEntity extends BSFSnowballEntity {
    public float frozenRange=2.5f;
    public FrozenSnowballEntity(LivingEntity livingEntity, Level level, LaunchFunc launchFunc) {
        super(livingEntity, level);
        this.setFrozenTime(60).setLaunchFrom(launchFunc.getLaunchForm()).setDamage(3).setBlazeDamage(8);
        launchFunc.launchProperties(this);
        this.setItem(new ItemStack(ItemRegister.FROZEN_SNOWBALL.get()));
    }

    //This is only used for dispenser
    public FrozenSnowballEntity(Level level, double x, double y, double z) {
        super(level, x, y, z);
        this.setDamage(3).setBlazeDamage(8);
        this.setItem(new ItemStack(ItemRegister.FROZEN_SNOWBALL.get()));
    }

    @Override
    protected Item getRegisterItem() {
        return ItemRegister.FROZEN_SNOWBALL.get();
    }

    @Override
    protected void onHit(@NotNull HitResult pResult) {
        super.onHit(pResult);
        if (!isCaught) {
            //todo complete here
        }
        if (!level.isClientSide) {
            this.discard();
        }
    }
}
