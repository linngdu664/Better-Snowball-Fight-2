package com.linngdu664.bsf;

import com.linngdu664.bsf.item.misc.IceSkatesItem;
import com.linngdu664.bsf.item.snowball.normal_snowball.SmoothSnowballItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

public class EventHandler {
    public static final UUID SKATES_SPEED_ID = UUID.fromString("00a3641b-33e0-4022-8d92-1c7b74c380b0");

    private void clearSpeedEffect(Player player) {
        if (player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).getModifier(SKATES_SPEED_ID) != null) {
            player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).removeModifier(SKATES_SPEED_ID);
            player.maxUpStep = 0.6f;
        }
    }

    private void addSpeedGoodEffect(Player player) {
        AttributeModifier skatesSpeed = new AttributeModifier(SKATES_SPEED_ID, "skates_speed", 0.15, AttributeModifier.Operation.ADDITION);
        if (!player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).hasModifier(skatesSpeed)) {
            player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).addPermanentModifier(skatesSpeed);
            player.maxUpStep = 2;
        }
    }

    private void addSpeedBadEffect(Player player) {
        AttributeModifier skatesSpeed = new AttributeModifier(SKATES_SPEED_ID, "skates_speed", -0.25, AttributeModifier.Operation.MULTIPLY_BASE);
        if (!player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).hasModifier(skatesSpeed)) {
            player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).addPermanentModifier(skatesSpeed);
            player.maxUpStep = 0.5f;
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            Player player = event.player;
            ItemStack shoes = player.getItemBySlot(EquipmentSlot.FEET);
            if (!shoes.isEmpty()) {
                if (shoes.getItem() instanceof IceSkatesItem && player.isSprinting() && player.isOnGround()) {
                    Level level = player.getLevel();
                    BlockPos pos = player.blockPosition().below();
                    if (level.getBlockState(pos).is(BlockTags.ICE)) {
                        level.addParticle(ParticleTypes.SNOWFLAKE, player.getX(), player.getEyeY() - 1.4, player.getZ(), 0, 0, 0);
                        addSpeedGoodEffect(player);
                    } else {
                        addSpeedBadEffect(player);
                    }
                } else {
                    clearSpeedEffect(player);
                }
            }
        }
    }

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
