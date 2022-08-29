package com.linngdu664.bsf;

import com.linngdu664.bsf.item.SmoothSnowballItem;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {
    @SubscribeEvent
    public void attackEntity(AttackEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getTarget();
        Level level = player.getLevel();
        Item item = player.getMainHandItem().getItem();
        if (!player.isSpectator() && entity instanceof LivingEntity target) {
            if (item instanceof SolidBucketItem) {
                target.setTicksFrozen(240);
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2));
                target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 150, 1));
                for (int i = 0; i < 32; i++) {
                    level.addParticle(ParticleTypes.ITEM_SNOWBALL, target.getX(), target.getEyeY(), target.getZ(), 0, 0, 0);
                }
                if (target instanceof Blaze) {
                    target.hurt(DamageSource.playerAttack(player), 8);
                }
                if (!player.getAbilities().instabuild) {
                    player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
                    player.getInventory().placeItemBackInInventory(new ItemStack(Items.BUCKET), true);
                }
            } else if (item instanceof SnowballItem || item instanceof SmoothSnowballItem) {
                target.setTicksFrozen(180);
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 1));
                target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 30, 1));
                if (!player.getAbilities().instabuild) {
                    player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
                }
                for (int i = 0; i < 16; i++) {
                    level.addParticle(ParticleTypes.ITEM_SNOWBALL, target.getX(), target.getEyeY(), target.getZ(), 0, 0, 0);
                }
                if (target instanceof Blaze) {
                    target.hurt(DamageSource.playerAttack(player), 4);
                }
            }
        }
    }
}
