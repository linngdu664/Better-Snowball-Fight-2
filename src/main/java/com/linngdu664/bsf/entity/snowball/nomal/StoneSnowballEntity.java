package com.linngdu664.bsf.entity.snowball.nomal;

import com.linngdu664.bsf.entity.AbstractBSFSnowballEntity;
import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.util.LaunchFunc;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class StoneSnowballEntity extends AbstractBSFSnowballEntity {
    public StoneSnowballEntity(LivingEntity livingEntity, Level level, LaunchFunc launchFunc) {
        super(livingEntity, level);
        this.setPunch(2.0).setLaunchFrom(launchFunc.getLaunchForm()).setDamage(2).setBlazeDamage(4);
        launchFunc.launchProperties(this);
        this.setItem(new ItemStack(ItemRegister.STONE_SNOWBALL.get()));
    }

    //This is only used for dispenser
    public StoneSnowballEntity(Level level, double x, double y, double z) {
        super(level, x, y, z);
        this.setDamage(2).setBlazeDamage(4);
        this.setItem(new ItemStack(ItemRegister.STONE_SNOWBALL.get()));
    }

    @Override
    protected Item getCorrespondingItem() {
        return ItemRegister.STONE_SNOWBALL.get();
    }

    @Override
    protected void onHit(@NotNull HitResult pResult) {
        super.onHit(pResult);
        if (!level.isClientSide) {
            this.discard();
        }
    }
}
