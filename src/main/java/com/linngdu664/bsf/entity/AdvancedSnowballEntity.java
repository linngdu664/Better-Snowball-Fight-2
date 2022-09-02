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
import net.minecraft.world.entity.Entity;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdvancedSnowballEntity extends ThrowableItemProjectile {
    public int weaknessTicks = 0;
    public int frozenTicks = 0;
    public double punch = 0.0;
    public float damage = Float.MIN_VALUE;
    public float blazeDamage = 3.0F;
    public SnowballType type;
    public int trackingTarget = 0;
    private Entity target = null;
    private double v0;
    private boolean init = false;

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

    private Entity getTarget() {
        Entity entity1 = null;
        if (trackingTarget == 1) {
            List<Monster> list = level.getEntitiesOfClass(Monster.class, this.getBoundingBox().inflate(10.0, 10.0, 10.0), (p_186450_) -> true);
            if (!list.isEmpty()) {
                entity1 = list.get(0);
                for (Monster entity : list) {
                    if (this.distanceToSqr(entity) < this.distanceToSqr(entity1)) {
                        entity1 = entity;
                    }
                }
            }
        } else {
            List<Player> list = level.getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(10.0, 10.0, 10.0), (p_186450_) -> true);
            if (!list.isEmpty()) {
                entity1 = list.get(0);
                for (Player entity : list) {
                    if (this.distanceToSqr(entity) < this.distanceToSqr(entity1)) {
                        entity1 = entity;
                    }
                }
            }
        }
        if (entity1 != null) {
            double d1 = entity1.getX() - this.getX();
            double d2 = entity1.getZ() - this.getZ();
            double d3 = this.getDeltaMovement().x;
            double d4 = this.getDeltaMovement().z;
            if (Mth.fastInvSqrt(d1 * d1 + d2 * d2) * Mth.fastInvSqrt(d3 * d3 + d4 * d4) * (d1 * d3 + d2 * d4) > 0.5) {
                return entity1;
            }
        }
        return null;
    }

    @Override
    public void tick() {
        super.tick();
        ((ServerLevel) level).sendParticles(ParticleRegister.SHORT_TIME_SNOWFLAKE.get(), this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
        if (type == SnowballType.SPECTRAL) {
            ((ServerLevel) level).sendParticles(ParticleTypes.INSTANT_EFFECT, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
        }
        if (!init) {
            Vec3 vec3 = this.getDeltaMovement();
            v0 = Math.sqrt(vec3.x * vec3.x + vec3.z * vec3.z + vec3.y * vec3.y);
            System.out.println(v0);
            init = true;
        }
        if (trackingTarget != 0) {
            if (target == null) {
                target = getTarget();
            } else {
                Vec3 delta = new Vec3(target.getX() - this.getX(), target.getY() - this.getY(), target.getZ() - this.getZ());
                Vec3 velocity = this.getDeltaMovement();
                delta.add(target.getDeltaMovement().scale(5));
                if (!level.isClientSide) {
                    double d1 = delta.x * delta.x + delta.z * delta.z;
                    double cosTheta = Mth.fastInvSqrt(d1) * Mth.fastInvSqrt(velocity.x * velocity.x + velocity.z * velocity.z) * (delta.x * velocity.x + delta.z * velocity.z);
                    double sinTheta;
                    if (cosTheta < Mth.cos((float) (8 * v0 * Mth.DEG_TO_RAD))) {
                        cosTheta = Mth.cos((float) (8 * v0 * Mth.DEG_TO_RAD));
                        sinTheta = Mth.sin((float) (8 * v0 * Mth.DEG_TO_RAD));
                    } else {
                        sinTheta = Math.sqrt(1 - cosTheta * cosTheta);
                    }
                    double vx, vy, vz;
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
                    double d6 = vx * vx + vz * vz;
                    double t2 = d1 / d6;
                    double d7 = this.getY() + vy * Math.sqrt(t2) - 0.015 * t2 - target.getEyeY();
                    if (d7 > 0.1) {
                        if (d6 > 1) {
                            vy -= d7 * (1.5 * d6 - 0.5) * 0.02;
                        } else {
                            vy -= d7 * Math.sqrt(d6) * 0.02;
                        }
                    }
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
