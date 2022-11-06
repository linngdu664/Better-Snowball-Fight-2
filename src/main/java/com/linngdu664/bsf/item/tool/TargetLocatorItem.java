package com.linngdu664.bsf.item.tool;

import com.linngdu664.bsf.entity.AbstractBSFSnowballEntity;
import com.linngdu664.bsf.entity.snowball.special.GPSSnowballEntity;
import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.item.snowball.normal.IronSnowballItem;
import com.linngdu664.bsf.item.tank.normal.IronSnowballTank;
import com.linngdu664.bsf.util.SoundRegister;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TargetLocatorItem extends AbstractBSFEnhanceableToolItem {
 //   private LivingEntity livingEntity;

    public TargetLocatorItem() {
        super(Rarity.UNCOMMON, 514);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        if (pPlayer.isShiftKeyDown()) {
            if (!pLevel.isClientSide) {
                CompoundTag compoundTag = itemStack.getOrCreateTag();
                compoundTag.remove("UUID");
                itemStack.setTag(compoundTag);
  //              livingEntity = null;
                pPlayer.sendMessage(new TranslatableComponent("targeted_clear.tip"), Util.NIL_UUID);
                pPlayer.getItemInHand(pUsedHand).setHoverName(new TranslatableComponent("item.bsf.target_locator"));
            }
        } else {
            ItemStack stack = null;
            int i;
            for (i = 0; i < pPlayer.getInventory().getContainerSize(); i++) {
                stack = pPlayer.getInventory().getItem(i);
                if (stack.getItem() instanceof IronSnowballItem || stack.getItem() instanceof IronSnowballTank) {
                    break;
                }
            }
            if (i != pPlayer.getInventory().getContainerSize() || pPlayer.getAbilities().instabuild) {
                pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundRegister.SNOWBALL_CANNON_SHOOT.get(), SoundSource.NEUTRAL, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
                if (!pLevel.isClientSide) {
                    AbstractBSFSnowballEntity snowballEntity = new GPSSnowballEntity(pPlayer, pLevel, itemStack);
                    snowballEntity.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 2.0F, 1.0F);
                    pLevel.addFreshEntity(snowballEntity);
                }
                if (!pPlayer.getAbilities().instabuild) {
                    if (stack.getItem() instanceof IronSnowballItem) {
                        stack.shrink(1);
                        if (stack.isEmpty()) {
                            pPlayer.getInventory().removeItem(stack);
                        }
                    } else {
                        stack.hurtAndBreak(1, pPlayer, p -> p.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.EMPTY_SNOWBALL_STORAGE_TANK.get()), true));
                    }
                    itemStack.hurtAndBreak(1, pPlayer, p -> p.broadcastBreakEvent(pUsedHand));
                }
            }
        }
        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.pass(itemStack);
    }

    //public LivingEntity getLivingEntity() {
    //    return livingEntity;
   // }

    //public void setLivingEntity(LivingEntity livingEntity) {
   //     this.livingEntity = livingEntity;
    //}

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TranslatableComponent("target_locator.tooltip").withStyle(ChatFormatting.BLUE));
        pTooltipComponents.add(new TranslatableComponent("target_locator1.tooltip").withStyle(ChatFormatting.BLUE));
        pTooltipComponents.add(new TranslatableComponent("target_locator2.tooltip").withStyle(ChatFormatting.BLUE));
    }
}
