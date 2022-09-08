package com.linngdu664.bsf.item.snowball.force_snowball;

import com.linngdu664.bsf.entity.snowball.force_snowball.ProjectileGravitySnowballEntity;
import com.linngdu664.bsf.entity.snowball.force_snowball.ProjectileRepulsionSnowballEntity;
import com.linngdu664.bsf.item.setter.ItemRegister;
import com.linngdu664.bsf.item.snowball.BSFSnowballItem;
import com.linngdu664.bsf.util.BSFUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ProjectileGravitySnowballItem extends BSFSnowballItem {
    public ProjectileGravitySnowballItem() {
        super(new Properties().tab(ItemRegister.GROUP).stacksTo(16));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        if (pPlayer.isShiftKeyDown()) {
            ItemStack newStack = new ItemStack(ItemRegister.MONSTER_GRAVITY_SNOWBALL.get(), itemStack.getCount());
            pPlayer.setItemInHand(pUsedHand, newStack);
        } else if (!BSFUtil.storageInTank(pPlayer, itemStack, ItemRegister.PROJECTILE_GRAVITY_SNOWBALL_STORAGE_TANK.get())) {
            pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
            if (!pLevel.isClientSide) {
                float i = pPlayer.hasEffect(MobEffects.WEAKNESS) ? 0.75F : 1.0F;
                float j = pPlayer.hasEffect(MobEffects.WEAKNESS) ? 0.5F : 1.0F;
                ProjectileGravitySnowballEntity snowballEntity = new ProjectileGravitySnowballEntity(pPlayer, pLevel, getLaunchFunc(j));
                snowballEntity.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 1.5F * i, 1.0F);
                pLevel.addFreshEntity(snowballEntity);
            }
            if (!pPlayer.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
        }
        pPlayer.awardStat(Stats.ITEM_USED.get(this));//Feedback effect
        return InteractionResultHolder.sidedSuccess(itemStack, pLevel.isClientSide());
    }
}
