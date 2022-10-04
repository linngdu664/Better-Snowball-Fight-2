package com.linngdu664.bsf.entity.snowball.nomal_snowball;

import com.linngdu664.bsf.entity.BSFSnowballEntity;
import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.item.tool.TargetLocatorItem;
import com.linngdu664.bsf.util.LaunchFrom;
import net.minecraft.Util;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class GPSSnowballEntity extends BSFSnowballEntity {
    private final TargetLocatorItem targetLocator;

    public GPSSnowballEntity(LivingEntity livingEntity, Level level, TargetLocatorItem targetLocator) {
        super(livingEntity, level);
        this.targetLocator = targetLocator;
        this.setPunch(2.0).setDamage(2).setLaunchFrom(LaunchFrom.HAND);
        this.setItem(new ItemStack(ItemRegister.GPS_SNOWBALL.get()));
    }

    @Override
    protected Item getCorrespondingItem() {
        return ItemRegister.IRON_SNOWBALL.get();
    }

    @Override
    protected void onHit(@NotNull HitResult pResult) {
        super.onHit(pResult);
        if (!level.isClientSide) {
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (!isCaught && pResult.getEntity() instanceof LivingEntity livingEntity) {
            targetLocator.setLivingEntity(livingEntity);
            if (getOwner() instanceof Player) {
                getOwner().sendMessage(new TextComponent(livingEntity.toString()), Util.NIL_UUID);
            }
        }
    }
}
