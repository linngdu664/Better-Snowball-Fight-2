package com.linngdu664.bsf.entity;

import com.linngdu664.bsf.item.setter.ItemRegister;
import com.linngdu664.bsf.particle.ParticleRegister;
import com.linngdu664.bsf.util.SnowballType;
import com.linngdu664.bsf.util.Util;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class AdvancedSnowballEntity extends ThrowableItemProjectile {
    public int weaknessTicks = 0;
    public int frozenTicks = 0;
    public double punch = 0.0;
    public float damage = Float.MIN_VALUE;
    public float blazeDamage = 3.0F;
    public SnowballType type;
    public UUID masterUUID = null;
    public int trackingType = 0;
    public int trackingTarget = 0;
    private Monster target = null;
    private int timer = 0;

    public AdvancedSnowballEntity(Level level, LivingEntity livingEntity, SnowballType type) {
        super(EntityType.EGG, livingEntity, level);
        this.type = type;
    }

    public AdvancedSnowballEntity(Level level, LivingEntity livingEntity, SnowballType type, float damage, float blazeDamage) {
        super(EntityType.EGG, livingEntity, level);
        this.type = type;
        this.damage = damage;
        this.blazeDamage = blazeDamage;
    }

    public AdvancedSnowballEntity(Level level, double x, double y, double z, SnowballType type) {
        super(EntityType.EGG, x, y, z, level);
        this.type = type;
    }

    public AdvancedSnowballEntity(Level level, double x, double y, double z, SnowballType type, float damage, float blazeDamage) {
        super(EntityType.EGG, x, y, z, level);
        this.type = type;
        this.damage = damage;
        this.blazeDamage = blazeDamage;
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult entityHitResult) {
        if (entityHitResult.getEntity() instanceof LivingEntity entity) {
            if (entity instanceof Player player && (player.getOffhandItem().is(ItemRegister.GLOVE.get()) &&
                    player.getUsedItemHand() == InteractionHand.OFF_HAND || player.getMainHandItem().is(ItemRegister.GLOVE.get()) &&
                    player.getUsedItemHand() == InteractionHand.MAIN_HAND) && player.isUsingItem() && Util.isHeadingToSnowball(player, this)) {
                switch (type) {
                    case SMOOTH -> player.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.SMOOTH_SNOWBALL.get(), 1), true);
                    case COMPACTED -> player.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.COMPACTED_SNOWBALL.get(), 1), true);
                    case STONE -> player.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.STONE_SNOWBALL.get(), 1), true);
                    case GLASS -> player.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.GLASS_SNOWBALL.get(), 1), true);
                    case IRON -> player.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.IRON_SNOWBALL.get(), 1), true);
                    case GOLD -> player.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.GOLD_SNOWBALL.get(), 1), true);
                    case OBSIDIAN -> player.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.OBSIDIAN_SNOWBALL.get(), 1), true);
                    case EXPLOSIVE -> player.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.EXPLOSIVE_SNOWBALL.get(), 1), true);
                    case ICE -> player.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.ICE_SNOWBALL.get(), 1), true);
                    case SPECTRAL -> player.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.SPECTRAL_SNOWBALL.get(), 1), true);
                }
                if (player.getMainHandItem().sameItemStackIgnoreDurability(new ItemStack(ItemRegister.GLOVE.get()))) {
                    player.getMainHandItem().hurtAndBreak(1, player, (e) -> e.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                } else if (player.getOffhandItem().sameItemStackIgnoreDurability(new ItemStack(ItemRegister.GLOVE.get()))) {
                    player.getOffhandItem().hurtAndBreak(1, player, (e) -> e.broadcastBreakEvent(EquipmentSlot.OFFHAND));
                }
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOW_BREAK, SoundSource.NEUTRAL, 3F, 0.4F / level.getRandom().nextFloat() * 0.4F + 0.8F);
                ((ServerLevel) level).sendParticles(ParticleTypes.SNOWFLAKE, this.getX(), this.getY(), this.getZ(), 3, 0, 0, 0, 0.04);
                return;
            }
            float i = entity instanceof Blaze ? blazeDamage : damage;
            entity.hurt(DamageSource.thrown(this, this.getOwner()), i);
            if (frozenTicks > 0) {
                entity.setTicksFrozen(frozenTicks);
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 1));
            }
            if (weaknessTicks > 0) {
                entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, weaknessTicks, 1));
            }
            Vec3 vec3d = this.getDeltaMovement().multiply(0.1 * punch, 0.0, 0.1 * punch);
            entity.push(vec3d.x, 0.0, vec3d.z);
            if (type == SnowballType.EXPLOSIVE) {
                if (level.getGameRules().getBoolean((GameRules.RULE_MOBGRIEFING))) {
                    level.explode(null, this.getX(), this.getY(), this.getZ(), 1.5F, Explosion.BlockInteraction.DESTROY);
                } else {
                    level.explode(null, this.getX(), this.getY(), this.getZ(), 1.5F, Explosion.BlockInteraction.NONE);
                }
            }
            if (type == SnowballType.SPECTRAL) {
                entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 0));
            }
            ((ServerLevel) level).sendParticles(ParticleTypes.ITEM_SNOWBALL, this.getX(), this.getY(), this.getZ(), 8, 0, 0, 0, 0);
            ((ServerLevel) level).sendParticles(ParticleTypes.SNOWFLAKE, this.getX(), this.getY(), this.getZ(), 8, 0, 0, 0, 0.04);
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        if (type == SnowballType.EXPLOSIVE) {
            if (level.getGameRules().getBoolean((GameRules.RULE_MOBGRIEFING))) {
                level.explode(null, this.getX(), this.getY(), this.getZ(), 1.5F, Explosion.BlockInteraction.DESTROY);
            } else {
                level.explode(null, this.getX(), this.getY(), this.getZ(), 1.5F, Explosion.BlockInteraction.NONE);
            }
        }
        ((ServerLevel) level).sendParticles(ParticleTypes.ITEM_SNOWBALL, this.getX(), this.getY(), this.getZ(), 8, 0, 0, 0, 0);
        ((ServerLevel) level).sendParticles(ParticleTypes.SNOWFLAKE, this.getX(), this.getY(), this.getZ(), 8, 0, 0, 0, 0.04);
    }

    private Monster getMonster() {
        List<Monster> list = level.getEntitiesOfClass(Monster.class, this.getBoundingBox().inflate(10.0, 10.0, 10.0), (p_186450_) -> true);
        if (!list.isEmpty()) {
            Monster monster = list.get(0);
            for (Monster entity : list) {
                if (this.distanceToSqr(entity) < this.distanceToSqr(monster)) {
                    monster = entity;
                }
            }
            return monster;
        }
        return null;
    }

    @Override
    public void tick() {
        super.tick();
        timer++;
        ((ServerLevel) level).sendParticles(ParticleRegister.SHORT_TIME_SNOWFLAKE.get(), this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
        if (type == SnowballType.SPECTRAL) {
            ((ServerLevel) level).sendParticles(ParticleTypes.INSTANT_EFFECT, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
        }
        if (trackingTarget == 0 && timer > 10) {
            if (target == null) {
                target = getMonster();
            } else {
                //System.out.println("target:" + target.getX() + "," + target.getY() + "," + target.getZ());
                Vec3 delta = new Vec3(target.getX() - this.getX(), target.getY() - this.getY(), target.getZ() - this.getZ());
                Vec3 velocity = this.getDeltaMovement();
                if (!level.isClientSide) {
                    double d1 = delta.x * delta.x + delta.z * delta.z;
                    double vx, vy, vz;
                    double cosTheta = Mth.fastInvSqrt(d1) * Mth.fastInvSqrt(velocity.x * velocity.x + velocity.z * velocity.z) * (delta.x * velocity.x + delta.z * velocity.z);
                    double sinTheta;
                    if (cosTheta < 0.9876883405951377) {
                        cosTheta = 0.9876883405951377;
                        sinTheta = 0.1564344650402309;
                    } else {
                        sinTheta = Math.sqrt(1 - cosTheta * cosTheta);
                    }
                    double d2 = velocity.x * cosTheta - velocity.z * sinTheta;
                    double d3 = velocity.x * sinTheta + velocity.z * cosTheta;
                    double d4 = velocity.x * cosTheta + velocity.z * sinTheta;
                    double d5 = -velocity.x * sinTheta + velocity.z * cosTheta;
                    if (d2 * delta.x + d3 * delta.z > d4 * delta.x + d5 * delta.z) {
                        vx = d2;
                        vz = d3;
                    } else {
                        vx = d4;
                        vz = d5;
                    }
                    vy = velocity.y;
                    double d6 = Math.sqrt(d1);
                    double d7 = Math.sqrt(vx * vx + vz * vz);
                    double t = d7 / d6;
                    System.out.println(this.getY() + (vy * t - 0.015 * t * t));
                    if (this.getY() + (vy * t - 0.015 * t * t) > target.getY()) {
                        System.out.println("too high");
                        vy -= 0.05;
                    }
                    System.out.println("velocity:" + vx + "," + vy + "," + vz);
                    this.setDeltaMovement(vx, vy, vz);
                    this.lerpMotion(vx, vy, vz);
                }
            }
        }
    }

    @Override
    protected void onHit(@NotNull HitResult pResult) {
        super.onHit(pResult);
        if (!level.isClientSide) {
            this.discard();
        }
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return Items.SNOWBALL;
    }
}
