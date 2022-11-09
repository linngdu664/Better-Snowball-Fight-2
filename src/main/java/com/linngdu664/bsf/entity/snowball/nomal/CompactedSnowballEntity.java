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

public class CompactedSnowballEntity extends AbstractBSFSnowballEntity {
    public CompactedSnowballEntity(LivingEntity livingEntity, Level level, LaunchFunc launchFunc) {
        super(EntityRegister.COMPACTED_SNOWBALL.get(), livingEntity, level);
        this.setPunch(2.0).setLaunchFrom(launchFunc.getLaunchForm());
        launchFunc.launchProperties(this);
        this.setItem(new ItemStack(ItemRegister.COMPACTED_SNOWBALL.get()));
    }

    //This is only used for dispenser
    public CompactedSnowballEntity(Level level, double x, double y, double z) {
        super(EntityRegister.COMPACTED_SNOWBALL.get(), level, x, y, z);
        this.setPunch(2.0);
        this.setItem(new ItemStack(ItemRegister.COMPACTED_SNOWBALL.get()));
    }

    public CompactedSnowballEntity(EntityType<CompactedSnowballEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public Item getCorrespondingItem() {
        return ItemRegister.COMPACTED_SNOWBALL.get();
    }

    @Override
    protected void onHit(@NotNull HitResult pResult) {
        super.onHit(pResult);
        if (!level.isClientSide) {
            this.discard();
        }
    }
}
