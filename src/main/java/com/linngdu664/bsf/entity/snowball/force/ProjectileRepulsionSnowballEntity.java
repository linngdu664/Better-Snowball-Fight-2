package com.linngdu664.bsf.entity.snowball.force;

import com.linngdu664.bsf.entity.snowball.AbstractForceSnowballEntity;
import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.util.LaunchFunc;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ProjectileRepulsionSnowballEntity extends AbstractForceSnowballEntity {
    public ProjectileRepulsionSnowballEntity(LivingEntity livingEntity, Level level, LaunchFunc launchFunc) {
        super(livingEntity, level);
        this.setRange(15).setTargetClass(Projectile.class).setGM(-2).setBoundaryR2(2).setLaunchFrom(launchFunc.getLaunchForm());
        launchFunc.launchProperties(this);
        this.setItem(new ItemStack(ItemRegister.PROJECTILE_REPULSION_SNOWBALL.get()));
    }

    //public ProjectileRepulsionSnowballEntity(EntityType<ProjectileRepulsionSnowballEntity> entityType, Level level) {
   //     super(entityType, level);
    //}

    @Override
    public Item getCorrespondingItem() {
        return ItemRegister.PROJECTILE_REPULSION_SNOWBALL.get();
    }

//    @Override
//    protected void onHit(@NotNull HitResult pResult) {
//        super.onHit(pResult);
//        if (!level.isClientSide) {
//            this.discard();
//        }
//    }

    public float getPower() {
        return 3f;
    }
}
