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

public class ObsidianSnowballEntity extends AbstractBSFSnowballEntity {
    public ObsidianSnowballEntity(LivingEntity livingEntity, Level level, LaunchFunc launchFunc) {
        super(EntityRegister.OBSIDIAN_SNOWBALL.get(), livingEntity, level);
        this.setLaunchFrom(launchFunc.getLaunchForm()).setDamage(6).setBlazeDamage(8);
        launchFunc.launchProperties(this);
        this.setItem(new ItemStack(ItemRegister.OBSIDIAN_SNOWBALL.get()));

    }

    //This is only used for dispenser
    public ObsidianSnowballEntity(Level level, double x, double y, double z) {
        super(EntityRegister.OBSIDIAN_SNOWBALL.get(), level, x, y, z);
        this.setDamage(6).setBlazeDamage(8);
        this.setItem(new ItemStack(ItemRegister.OBSIDIAN_SNOWBALL.get()));
    }

    public ObsidianSnowballEntity(EntityType<ObsidianSnowballEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public Item getCorrespondingItem() {
        return ItemRegister.OBSIDIAN_SNOWBALL.get();
    }

    @Override
    protected void onHit(@NotNull HitResult pResult) {
        super.onHit(pResult);
        if (!level.isClientSide) {
            this.discard();
        }
    }

    public float getPower() {
        return 2.25f;
    }
}
