package com.linngdu664.bsf.entity.snowball.tracking_snowball.missiles_snowball;

import com.linngdu664.bsf.entity.snowball.tracking_snowball.MissileSnowballEntity;
import com.linngdu664.bsf.item.setter.ItemRegister;
import com.linngdu664.bsf.util.LaunchFunc;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class HeavyPlayerTrackingSnowballEntity extends MissileSnowballEntity {
    public HeavyPlayerTrackingSnowballEntity(LivingEntity livingEntity, Level level, LaunchFunc launchFunc) {
        super(livingEntity, level);
        this.setRange(20).setTargetClass(Player.class).setDamage(4).setBlazeDamage(6).setLaunchFrom(launchFunc.getLaunchForm());
        launchFunc.launchProperties(this);
    }

    @Override
    protected void onHit(@NotNull HitResult pResult) {
        super.onHit(pResult);
        this.discard();
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return ItemRegister.PLAYER_TRACKING_SNOWBALL_WITH_DAMAGE.get();
    }
}
