package com.linngdu664.bsf.entity.snowball.tracking;

import com.linngdu664.bsf.entity.snowball.AbstractTrackingSnowballEntity;
import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.util.LaunchFunc;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class ExplosivePlayerTrackingSnowballEntity extends AbstractTrackingSnowballEntity {
    public ExplosivePlayerTrackingSnowballEntity(LivingEntity livingEntity, Level level, LaunchFunc launchFunc) {
        super(livingEntity, level);
        this.setLockFeet(true).setRange(10).setTargetClass(Player.class).setDamage(3).setBlazeDamage(5).setLaunchFrom(launchFunc.getLaunchForm());
        launchFunc.launchProperties(this);
        this.setItem(new ItemStack(ItemRegister.EXPLOSIVE_PLAYER_TRACKING_SNOWBALL.get()));
    }

   // public ExplosivePlayerTrackingSnowballEntity(EntityType<ExplosivePlayerTrackingSnowballEntity> entityType, Level level) {
    //    super(entityType, level);
    //}

    @Override
    public Item getCorrespondingItem() {
        return ItemRegister.EXPLOSIVE_PLAYER_TRACKING_SNOWBALL.get();
    }

    @Override
    protected void onHit(@NotNull HitResult pResult) {
        super.onHit(pResult);
        if (!level.isClientSide) {
            if (!isCaught) {
                handleExplosion(1.5F);
            }
            this.discard();
        }
    }

    public float getPower() {
        return 3.25f;
    }
}
