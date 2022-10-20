package com.linngdu664.bsf.entity.snowball.special;

import com.linngdu664.bsf.entity.AbstractBSFSnowballEntity;
import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.item.tool.TargetLocatorItem;
import com.linngdu664.bsf.util.LaunchFrom;
import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class GPSSnowballEntity extends AbstractBSFSnowballEntity {
    private final ItemStack targetLocator;

    public GPSSnowballEntity(LivingEntity livingEntity, Level level, ItemStack targetLocator) {
        super(livingEntity, level);
        this.targetLocator = targetLocator;
        this.setPunch(2.0).setLaunchFrom(LaunchFrom.HAND);
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
            ((TargetLocatorItem) targetLocator.getItem()).setLivingEntity(livingEntity);
            if (getOwner() instanceof Player player) {
                player.sendMessage(new TranslatableComponent("target.tip").append(livingEntity.getName().getString() + " ID:" + livingEntity.getId()), Util.NIL_UUID);
                pResult.getEntity().sendMessage(new TranslatableComponent("targeted.tip"), Util.NIL_UUID);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.7F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
            }
            targetLocator.setHoverName(new TranslatableComponent("item.bsf.target_locator").append(":").append(new TranslatableComponent("target.tip")).append(livingEntity.getName().getString() + " ID:" + livingEntity.getId()));
        }
    }
}
