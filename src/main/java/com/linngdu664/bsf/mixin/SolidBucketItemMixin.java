package com.linngdu664.bsf.mixin;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SolidBucketItem.class)
public abstract class SolidBucketItemMixin extends BlockItem implements DispensibleContainerItem {
    @Mutable
    @Final
    @Shadow
    private final SoundEvent placeSound;

    public SolidBucketItemMixin(Block pBlock, SoundEvent pPlaceSound, Item.Properties pProperties) {
        super(pBlock, pProperties);
        this.placeSound = pPlaceSound;
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pTarget.setTicksFrozen(240);
        pTarget.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
        pTarget.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2));
        pTarget.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 150, 1));
        if (pAttacker instanceof Player player) {
            if (!player.getAbilities().instabuild) {
                player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
                player.getInventory().placeItemBackInInventory(new ItemStack(Items.BUCKET, 1), true);
            }
            for (int i = 0; i < 32; i++) {
                pTarget.level.addParticle(ParticleTypes.ITEM_SNOWBALL, pTarget.getX(), pTarget.getY() + 1, pTarget.getZ(), 0, 0, 0);
            }
            if (pTarget instanceof Blaze) {
                pTarget.hurt(DamageSource.playerAttack(player), 8);
            }
        }
        return true;
    }
}
