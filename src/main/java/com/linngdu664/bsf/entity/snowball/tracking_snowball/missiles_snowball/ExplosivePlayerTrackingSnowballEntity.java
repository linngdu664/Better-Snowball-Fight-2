package com.linngdu664.bsf.entity.snowball.tracking_snowball.missiles_snowball;

import com.linngdu664.bsf.entity.snowball.tracking_snowball.MissileSnowballEntity;
import com.linngdu664.bsf.item.setter.ItemRegister;
import com.linngdu664.bsf.util.LaunchFunc;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class ExplosivePlayerTrackingSnowballEntity extends MissileSnowballEntity {
    public ExplosivePlayerTrackingSnowballEntity(LivingEntity livingEntity, Level level, LaunchFunc launchFunc) {
        super(livingEntity, level);
        this.setRange(20).setTargetClass(Player.class);
        this.setDamage(3).setBlazeDamage(5).setLaunchFrom(launchFunc.getLaunchForm());
        launchFunc.launchProperties(this);
    }

    @Override
    protected void onHit(@NotNull HitResult pResult) {
        super.onHit(pResult);
        this.discard();
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return ItemRegister.EXPLOSIVE_PLAYER_TRACKING_SNOWBALL.get();
    }

    @Override
    public void setting() {
        if (level.getGameRules().getBoolean((GameRules.RULE_MOBGRIEFING))) {
            level.explode(null, this.getX(), this.getY(), this.getZ(), 1.5F, Explosion.BlockInteraction.DESTROY);
        } else {
            level.explode(null, this.getX(), this.getY(), this.getZ(), 1.5F, Explosion.BlockInteraction.NONE);
        }
    }
}
