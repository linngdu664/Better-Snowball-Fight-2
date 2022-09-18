package com.linngdu664.bsf.item.misc;

import com.linngdu664.bsf.effect.EffectRegister;
import com.linngdu664.bsf.util.ItemGroup;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class VodkaItem extends Item {
    public VodkaItem() {
        super(new Properties().tab(ItemGroup.MAIN).stacksTo(1));
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull LivingEntity pEntityLiving) {
        Player player = (Player) pEntityLiving;
        if (player instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)player, pStack);
        }
        if (!pLevel.isClientSide) {
            int t;
            if (player.hasEffect(EffectRegister.COLD_RESISTANCE_EFFECT.get())) {
                t = Objects.requireNonNull(pEntityLiving.getEffect(EffectRegister.COLD_RESISTANCE_EFFECT.get())).getDuration();
            } else {
                t = 0;
            }
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100 + t));
            player.addEffect(new MobEffectInstance(EffectRegister.COLD_RESISTANCE_EFFECT.get(), 600));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600));
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            pStack.shrink(1);
            player.getInventory().placeItemBackInInventory(new ItemStack(Items.GLASS_BOTTLE), true);
        }
        pLevel.gameEvent(pEntityLiving, GameEvent.DRINKING_FINISH, pEntityLiving.eyeBlockPosition());
        return pStack;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack pStack) {
        return 32;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack pStack) {
        return UseAnim.DRINK;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pHand) {
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pHand);
    }
}
