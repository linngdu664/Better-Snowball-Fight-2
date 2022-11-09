package com.linngdu664.bsf.entity.snowball.nomal;

import com.linngdu664.bsf.entity.AbstractBSFSnowballEntity;
import com.linngdu664.bsf.entity.EntityRegister;
import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.util.LaunchFunc;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class IronSnowballEntity extends AbstractBSFSnowballEntity {
    public IronSnowballEntity(LivingEntity livingEntity, Level level, LaunchFunc launchFunc) {
        super(EntityRegister.IRON_SNOWBALL.get(), livingEntity, level);
        this.setLaunchFrom(launchFunc.getLaunchForm()).setDamage(4).setBlazeDamage(6);
        launchFunc.launchProperties(this);
        this.setItem(new ItemStack(ItemRegister.IRON_SNOWBALL.get()));
    }

    //This is only used for dispenser
    public IronSnowballEntity(Level level, double x, double y, double z) {
        super(EntityRegister.IRON_SNOWBALL.get(), level, x, y, z);
        this.setDamage(4).setBlazeDamage(6);
        this.setItem(new ItemStack(ItemRegister.IRON_SNOWBALL.get()));
    }

    public IronSnowballEntity(EntityType<IronSnowballEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public Item getCorrespondingItem() {
        return ItemRegister.IRON_SNOWBALL.get();
    }

    @Override
    protected void onHit(@NotNull HitResult pResult) {
        super.onHit(pResult);
        if (!level.isClientSide) {
            this.discard();
        }
    }

    public float getPower() {
        return 1.4f;
    }
}
