package com.linngdu664.bsf.item;

import com.linngdu664.bsf.entity.AdvancedSnowballEntity;
import com.linngdu664.bsf.entity.SnowballType;
import com.linngdu664.bsf.item.setter.ItemRegister;
import com.linngdu664.bsf.item.setter.ModGroup;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SmoothSnowballItem extends Item {
    public SmoothSnowballItem() {
        super(new Properties().tab(ModGroup.group).stacksTo(16));
        DispenserBlock.registerBehavior(this, new AbstractProjectileDispenseBehavior() {
            protected Projectile getProjectile(Level p_123476_, Position p_123477_, ItemStack p_123478_) {
                return Util.make(new AdvancedSnowballEntity(p_123476_, p_123477_.x(), p_123477_.y(), p_123477_.z(), SnowballType.SMOOTH), (p_123474_) -> {
                    p_123474_.setItem(p_123478_);
                });
            }
        });
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        if (pUsedHand == InteractionHand.MAIN_HAND) {
            pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
            if (!pLevel.isClientSide) {
                float i = pPlayer.hasEffect(MobEffects.WEAKNESS) ? 0.75F : 1.0F;
                AdvancedSnowballEntity snowballEntity = new AdvancedSnowballEntity(pLevel, pPlayer, SnowballType.SMOOTH);
                snowballEntity.setItem(itemStack);
                snowballEntity.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 1.25F * i, 1.0F);
                pLevel.addFreshEntity(snowballEntity);
            }
            if (!pPlayer.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
        } else if (pPlayer.getMainHandItem().isEmpty()) {
            pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.COMPACTED_SNOWBALL.get()), true);
            if (!pPlayer.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
        }
        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(itemStack, pLevel.isClientSide());
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pTarget.setTicksFrozen(180);
        pTarget.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 1));
        pTarget.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 30, 1));
        if (!((Player) pAttacker).getAbilities().instabuild) {
            pAttacker.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
        }
        for (int i = 0; i < 16; i++) {
            pAttacker.getLevel().addParticle(ParticleTypes.ITEM_SNOWBALL, pTarget.getX(), pTarget.getEyeY(), pTarget.getZ(), 0, 0, 0);
        }
        if (pTarget instanceof Blaze) {
            pTarget.hurt(DamageSource.playerAttack((Player) pAttacker), 4);
        }
        return true;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TranslatableComponent("smooth_snowball.tooltip").withStyle(ChatFormatting.GRAY));
    }
}
