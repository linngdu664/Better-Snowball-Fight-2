package com.linngdu664.bsf.mixin;

import com.linngdu664.bsf.entity.AdvancedSnowballEntity;
import com.linngdu664.bsf.entity.SnowballType;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SnowballItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(SnowballItem.class)
public class SnowballItemMixin extends Item {
    public SnowballItemMixin() {
        super (new Properties());
    }

    /**
     * @author zx1316
     * @reason Now glove can catch vanilla snowball
     */
    @Overwrite
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!pLevel.isClientSide) {
            float i = pPlayer.hasEffect(MobEffects.WEAKNESS) ? 0.2F : 1.0F;
            AdvancedSnowballEntity snowballEntity = new AdvancedSnowballEntity(pLevel, pPlayer, SnowballType.VANILLA);
            snowballEntity.setItem(itemStack);
            snowballEntity.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 1.5F * i, 1.0F);
            pLevel.addFreshEntity(snowballEntity);
        }
        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        if (!pPlayer.getAbilities().instabuild) {
            itemStack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(itemStack, pLevel.isClientSide());
    }
/*
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
    }*/
}
