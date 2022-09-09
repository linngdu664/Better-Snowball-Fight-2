package com.linngdu664.bsf.item.snowball.force_snowball;

import com.linngdu664.bsf.entity.snowball.force_snowball.BlackHoleSnowballEntity;
import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.item.snowball.BSFSnowballItem;
import com.linngdu664.bsf.util.BSFUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import com.linngdu664.bsf.util.ItemGroup;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlackHoleSnowballItem extends BSFSnowballItem {
    public BlackHoleSnowballItem() {
        super(new Properties().tab(ItemGroup.MAIN).stacksTo(16));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        if (!BSFUtil.storageInTank(pPlayer, itemStack, ItemRegister.BLACK_HOLE_SNOWBALL_STORAGE_TANK.get())) {
            pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
            if (!pLevel.isClientSide) {
                float i = pPlayer.hasEffect(MobEffects.WEAKNESS) ? 0.75F : 1.0F;
                BlackHoleSnowballEntity snowballEntity = new BlackHoleSnowballEntity(pPlayer, pLevel, getLaunchFunc(1));
                if(pPlayer.isShiftKeyDown()){
                    snowballEntity.startTime=10;
                }
                snowballEntity.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 1.5F * i, 1.0F);
                pLevel.addFreshEntity(snowballEntity);
            }
            if (!pPlayer.getAbilities().instabuild) {
                itemStack.shrink(1);
                pPlayer.getCooldowns().addCooldown(this, 100);
            }
        }
        pPlayer.awardStat(Stats.ITEM_USED.get(this));//Feedback effect
        return InteractionResultHolder.sidedSuccess(itemStack, pLevel.isClientSide());
    }
    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TranslatableComponent("lunch_yes_hand.tooltip").withStyle(ChatFormatting.DARK_GREEN));
        pTooltipComponents.add(new TranslatableComponent("lunch_no_cannon.tooltip").withStyle(ChatFormatting.DARK_RED));
        pTooltipComponents.add(new TranslatableComponent("lunch_no_machine_gun.tooltip").withStyle(ChatFormatting.DARK_RED));
        pTooltipComponents.add(new TranslatableComponent("lunch_no_shotgun.tooltip").withStyle(ChatFormatting.DARK_RED));
        pTooltipComponents.add(new TranslatableComponent("black_hole_snowball1.tooltip").withStyle(ChatFormatting.BLUE));
        pTooltipComponents.add(new TranslatableComponent("black_hole_snowball.tooltip").withStyle(ChatFormatting.OBFUSCATED));
        pTooltipComponents.add(new TranslatableComponent("black_hole_snowball.tooltip").withStyle(ChatFormatting.DARK_PURPLE));
    }
}
