package com.linngdu664.bsf.entity;

import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.particle.ParticleRegister;
import com.linngdu664.bsf.util.LaunchFrom;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.monster.Blaze;
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
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractBSFSnowballEntity extends ThrowableItemProjectile {
    public boolean isCaught = false;
    public double punch = 0.0;
    public float damage = Float.MIN_NORMAL;
    public float blazeDamage = 3.0F;
    public LaunchFrom launchFrom;
    public int frozenTicks = 0;
    public int weaknessTicks = 0;
    // You need to distinguish between LaunchFrom and LaunchFunc
    // LaunchFrom is an Enum, LaunchFunc is an Interface

    // EntityType should not be SNOWBALL, because if we do so, it will call snowball mixin and make a mess.
    public AbstractBSFSnowballEntity(LivingEntity livingEntity, Level level) {
        super(EntityType.EGG, livingEntity, level);
    }

    public AbstractBSFSnowballEntity(Level level, double x, double y, double z) {
        super(EntityType.EGG, x, y, z, level);
    }

    /**
     * Triggered when an entity hits an entity
     *
     * @param pResult EntityHitResult
     */
    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        if (pResult.getEntity() instanceof LivingEntity entity) {
            // Handling the catch
            if (catchOnGlove(entity)) {
                ((ServerLevel) level).sendParticles(ParticleTypes.SNOWFLAKE, this.getX(), this.getY(), this.getZ(), 3, 0, 0, 0, 0.04);
                isCaught = true;
                return;
            }

            // Damage entity
            float hurt = entity instanceof Blaze ? blazeDamage : damage;
            entity.hurt(DamageSource.thrown(this, this.getOwner()), hurt);

            // Handle frozen and weakness effects
            if (frozenTicks > 0 && !(entity instanceof BSFSnowGolemEntity) && !(entity instanceof SnowGolem)) {
                if (entity.getTicksFrozen() < frozenTicks) {
                    entity.setTicksFrozen(frozenTicks);
                }
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 1));
            }
            if (weaknessTicks > 0) {
                entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, weaknessTicks, 1));
            }

            // Push entity
            Vec3 vec3d = this.getDeltaMovement().multiply(0.1 * punch, 0.0, 0.1 * punch);
            entity.push(vec3d.x, 0.0, vec3d.z);
        }
        spawnBasicParticles(level);
    }

    /**
     * Triggered when an entity hits a block.
     *
     * @param p_37258_ blockHitResult
     */
    @Override
    protected void onHitBlock(@NotNull BlockHitResult p_37258_) {
        super.onHitBlock(p_37258_);
        spawnBasicParticles(level);
    }

    /**
     * This method will be called every tick.
     */
    @Override
    public void tick() {
        // Spawn trace particles
        ((ServerLevel) level).sendParticles(ParticleRegister.SHORT_TIME_SNOWFLAKE.get(), this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
        super.tick();
    }

    /**
     * Do not touch/override this magical fucking method, or the texture of the snowball will become egg!
     *
     * @return I don't understand.
     */
    @Override
    protected @NotNull Item getDefaultItem() {
        return Items.SNOWBALL;
    }

    /**
     * You should override this fucking method if you want to catch the snowball!
     *
     * @return The corresponding item.
     */
    protected Item getCorrespondingItem() {
        return null;
    }

    /**
     * @param entity The player who is using the glove.
     * @return If the glove catches return true.
     */
    protected boolean catchOnGlove(LivingEntity entity) {
        if (entity instanceof Player player && (player.getOffhandItem().is(ItemRegister.GLOVE.get()) &&
                player.getUsedItemHand() == InteractionHand.OFF_HAND || player.getMainHandItem().is(ItemRegister.GLOVE.get()) &&
                player.getUsedItemHand() == InteractionHand.MAIN_HAND) && player.isUsingItem() && isHeadingToSnowball(player)) {
            player.getInventory().placeItemBackInInventory(new ItemStack(getCorrespondingItem()));
            if (player.getMainHandItem().sameItemStackIgnoreDurability(new ItemStack(ItemRegister.GLOVE.get()))) {
                player.getMainHandItem().hurtAndBreak(1, player, (e) -> e.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            } else if (player.getOffhandItem().sameItemStackIgnoreDurability(new ItemStack(ItemRegister.GLOVE.get()))) {
                player.getOffhandItem().hurtAndBreak(1, player, (e) -> e.broadcastBreakEvent(EquipmentSlot.OFFHAND));
            }
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOW_BREAK, SoundSource.NEUTRAL, 3F, 0.4F / level.getRandom().nextFloat() * 0.4F + 0.8F);
            ((ServerLevel) level).sendParticles(ParticleTypes.SNOWFLAKE, this.getX(), this.getY(), this.getZ(), 3, 0, 0, 0, 0.04);
            return true;
        }
        return false;
    }

    // Check whether the player can catch the snowball
    protected boolean isHeadingToSnowball(Player player) {
        Vec3 speedVec = this.getDeltaMovement().normalize();
        Vec3 cameraVec = Vec3.directionFromRotation(player.getXRot(), player.getYRot());
        return Math.abs(cameraVec.dot(speedVec) + 1.0) < 0.2;
    }

    protected void handleExplosion(float radius) {
        Level level = this.getLevel();
        if (level.getGameRules().getBoolean((GameRules.RULE_MOBGRIEFING))) {
            level.explode(null, this.getX(), this.getY(), this.getZ(), radius, Explosion.BlockInteraction.DESTROY);
        } else {
            level.explode(null, this.getX(), this.getY(), this.getZ(), radius, Explosion.BlockInteraction.NONE);
        }
    }

    protected void spawnBasicParticles(Level level) {
        if (!level.isClientSide) {
            ((ServerLevel) level).sendParticles(ParticleTypes.ITEM_SNOWBALL, this.getX(), this.getY(), this.getZ(), 8, 0, 0, 0, 0);
            ((ServerLevel) level).sendParticles(ParticleTypes.SNOWFLAKE, this.getX(), this.getY(), this.getZ(), 8, 0, 0, 0, 0.04);
        }
    }

    // Setters with builder style
    // Like this:  snowballEntity.setPunch(2.0).setDamage(2.5f)
    // If necessary, add setWeaknessTime.
    public AbstractBSFSnowballEntity setPunch(double punch) {
        this.punch = punch;
        return this;
    }

    public AbstractBSFSnowballEntity setDamage(float damage) {
        this.damage = damage;
        return this;
    }

    public AbstractBSFSnowballEntity setBlazeDamage(float blazeDamage) {
        this.blazeDamage = blazeDamage;
        return this;
    }

    public AbstractBSFSnowballEntity setLaunchFrom(LaunchFrom launchFrom) {
        this.launchFrom = launchFrom;
        return this;
    }

    public AbstractBSFSnowballEntity setFrozenTicks(int frozenTicks) {
        this.frozenTicks = frozenTicks;
        return this;
    }
}
