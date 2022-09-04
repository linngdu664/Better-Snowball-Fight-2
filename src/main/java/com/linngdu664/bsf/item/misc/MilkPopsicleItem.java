package com.linngdu664.bsf.item.misc;

import com.linngdu664.bsf.item.setter.ItemRegister;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MilkPopsicleItem extends Item {
    private static final FoodProperties food = new FoodProperties.Builder().alwaysEat().build();

    public MilkPopsicleItem() {
        super(new Properties().food(food).tab(ItemRegister.GROUP));
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemStack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack itemStack) {
        return 64;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, Level level, @NotNull LivingEntity user) {
        if (!level.isClientSide) {
            user.setRemainingFireTicks(0);
            user.removeAllEffects();
            user.setTicksFrozen(40);
            user.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 1));
        }
        CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) user, stack);
        if (!((Player) user).getAbilities().instabuild) {
            stack.shrink(1);
        }
        return stack;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TranslatableComponent("milk_popsicle.tooltip").withStyle(ChatFormatting.GRAY));
    }
}
