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

public class IceSnowballEntity extends BSFSnowballEntity {
    public IceSnowballEntity(LivingEntity livingEntity, Level level, LaunchFunc launchFunc) {
        super(livingEntity, level);
        this.setFrozenTime(60).setLaunchFrom(launchFunc.getLaunchForm()).setDamage(3).setBlazeDamage(6);
        launchFunc.launchProperties(this);
        this.setItem(new ItemStack(ItemRegister.ICE_SNOWBALL.get()));

    }

    //This is only used for dispenser
    public IceSnowballEntity(Level level, double x, double y, double z) {
        super(level, x, y, z);
        this.setFrozenTime(60).setDamage(3).setBlazeDamage(6);
        this.setItem(new ItemStack(ItemRegister.ICE_SNOWBALL.get()));
    }

    @Override
    protected Item getRegisterItem() {
        return ItemRegister.ICE_SNOWBALL.get();
    }

    @Override
    protected void onHit(@NotNull HitResult pResult) {
        super.onHit(pResult);
        if (!level.isClientSide) {
            this.discard();
        }
    }
}
