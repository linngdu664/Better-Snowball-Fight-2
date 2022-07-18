package com.linngdu664.bsf.item;

import com.linngdu664.bsf.entity.AdvancedSnowballEntity;
import com.linngdu664.bsf.entity.SnowballType;
import com.linngdu664.bsf.item.setter.ItemRegister;
import com.linngdu664.bsf.item.setter.ModGroup;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GlassSnowballItem extends Item {
    public GlassSnowballItem() {
        super(new Properties().tab(ModGroup.group).stacksTo(16));
        DispenserBlock.registerBehavior(this, new AbstractProjectileDispenseBehavior() {
            protected Projectile getProjectile(Level p_123476_, Position p_123477_, ItemStack p_123478_) {
                return Util.make(new AdvancedSnowballEntity(p_123476_, p_123477_.x(), p_123477_.y(), p_123477_.z(), SnowballType.GLASS, 3.0F, 5.0F), (p_123474_) -> {
                    p_123474_.setItem(p_123478_);
                });
            }
        });
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        if (pPlayer.getOffhandItem().getItem() == ItemRegister.EMPTY_SNOWBALL_STORAGE_TANK.get()) {
            pPlayer.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(ItemRegister.GLASS_SNOWBALL_STORAGE_TANK.get()));
            pPlayer.getOffhandItem().setDamageValue(96 - pPlayer.getMainHandItem().getCount());
            if (!pPlayer.getAbilities().instabuild) {
                itemStack.shrink(pPlayer.getMainHandItem().getCount());
            }
        } else if (pPlayer.getOffhandItem().getItem() == ItemRegister.GLASS_SNOWBALL_STORAGE_TANK.get() && pPlayer.getOffhandItem().getDamageValue() != 0) {
            if (pPlayer.getOffhandItem().getDamageValue() >= pPlayer.getMainHandItem().getCount()) {
                pPlayer.getOffhandItem().setDamageValue(pPlayer.getOffhandItem().getDamageValue() - pPlayer.getMainHandItem().getCount());
                if (!pPlayer.getAbilities().instabuild) {
                    itemStack.shrink(pPlayer.getMainHandItem().getCount());
                }
            } else {
                if (!pPlayer.getAbilities().instabuild) {
                    itemStack.shrink(pPlayer.getOffhandItem().getDamageValue());
                }
                pPlayer.getOffhandItem().setDamageValue(0);
            }
        } else {
            pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
            if (!pLevel.isClientSide) {
                float i = pPlayer.hasEffect(MobEffects.WEAKNESS) ? 0.75F : 1.0F;
                float j = pPlayer.hasEffect(MobEffects.WEAKNESS) ? 0.5F : 1.0F;
                AdvancedSnowballEntity snowballEntity = new AdvancedSnowballEntity(pLevel, pPlayer, SnowballType.GLASS, 3.0F * j, 5.0F * j);
                snowballEntity.setItem(itemStack);
                snowballEntity.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 1.125F * i, 1.0F);
                pLevel.addFreshEntity(snowballEntity);
            }
            if (!pPlayer.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
        }
        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(itemStack, pLevel.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TranslatableComponent("glass_snowball.tooltip").withStyle(ChatFormatting.GRAY));
    }
}
