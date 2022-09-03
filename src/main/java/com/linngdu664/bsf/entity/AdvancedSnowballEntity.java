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
    private Entity target = null;
    private double v0;
    private int timer = 0;

    private int trackingMode = 0;
    private Class<? extends Entity> targetClass;
    private double trackingRange;
    private boolean angleRestriction;
    private double GM;
    private boolean trackingMultipleTargets;
    private boolean selfAttraction;
    private boolean attraction;

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
    public <T extends Entity> void setGravityTracking(Class<T> targetClass, double trackingRange, double GM, boolean angleRestriction, boolean trackingMultipleTargets, boolean selfAttraction , boolean attraction){
        trackingMode=2;
        this.targetClass=targetClass;
        this.trackingRange=trackingRange;
        this.GM=GM;
        this.angleRestriction=angleRestriction;
        this.trackingMultipleTargets=trackingMultipleTargets;
        this.selfAttraction=selfAttraction;
        this.attraction=attraction;
    }
    public <T extends Entity> void setMissilesTracking(Class<T> targetClass, double trackingRange, boolean angleRestriction){
        trackingMode=1;
        this.targetClass=targetClass;
        this.trackingRange=trackingRange;
        this.angleRestriction=angleRestriction;
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

    private <T extends Entity> Entity getTarget(Class<T> t, boolean angleRestriction, double trackingRange) {
        Entity entity1 = null;

        List<T> list = level.getEntitiesOfClass(t, this.getBoundingBox().inflate(trackingRange, trackingRange, trackingRange), (p_186450_) -> true);
        if(t == AdvancedSnowballEntity.class){
            list.remove(this);
        }
        if (!list.isEmpty()) {
            entity1 = list.get(0);
            for (T entity : list) {
                if (this.distanceToSqr(entity) < this.distanceToSqr(entity1)) {
                    entity1 = entity;
                }
            }
        }

        if (entity1 != null) {
            if(angleRestriction) {
                double d1 = entity1.getX() - this.getX();
                double d2 = entity1.getZ() - this.getZ();
                double d3 = this.getDeltaMovement().x;
                double d4 = this.getDeltaMovement().z;
                if (Mth.fastInvSqrt(d1 * d1 + d2 * d2) * Mth.fastInvSqrt(d3 * d3 + d4 * d4) * (d1 * d3 + d2 * d4) > 0.5) {
                    return entity1;
                }
            }else{
                return entity1;
            }

        }
        return null;
    }

    private <T extends Entity> List<T> getTargetList(Class<T> t,double trackingRange) {
        List<T> list = level.getEntitiesOfClass(t, this.getBoundingBox().inflate(trackingRange, trackingRange, trackingRange), (p_186450_) -> true);
        if(t == AdvancedSnowballEntity.class){
            list.remove(this);
        }
        if (!list.isEmpty()) {
            return list;
        }else{
            return null;
        }

    }
    @Override
    public void tick() {
        ((ServerLevel) level).sendParticles(ParticleRegister.SHORT_TIME_SNOWFLAKE.get(), this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
        if (type == SnowballType.SPECTRAL) {
            ((ServerLevel) level).sendParticles(ParticleTypes.INSTANT_EFFECT, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
        }
        if(trackingMode>0){
            tracking();
        }
        timer++;
        super.tick();
    }

    private void tracking(){
        switch (trackingMode){
            case 1 -> missilesTracking(targetClass,trackingRange,angleRestriction);
            case 2 -> gravityTracking(targetClass,trackingRange,GM,angleRestriction,trackingMultipleTargets,selfAttraction,attraction);
        }
    }
    private <T extends Entity> void gravityTracking(Class<T> targetClass, double trackingRange, double GM, boolean angleRestriction, boolean trackingMultipleTargets, boolean selfAttraction , boolean attraction){
        if(trackingMultipleTargets){
            List<T> list = getTargetList(targetClass,trackingRange);
            if (list!=null && !list.isEmpty()) {
                for (T entity : list) {
                    if(angleRestriction){
                        double d1 = entity.getX() - this.getX();
                        double d2 = entity.getZ() - this.getZ();
                        double d3 = this.getDeltaMovement().x;
                        double d4 = this.getDeltaMovement().z;
                        if (Mth.fastInvSqrt(d1 * d1 + d2 * d2) * Mth.fastInvSqrt(d3 * d3 + d4 * d4) * (d1 * d3 + d2 * d4) < 0.5) {
                            continue;
                        }
                    }
                    Vec3 rVec = new Vec3(entity.getX() - this.getX(), entity.getY()+2 - this.getY(), entity.getZ() - this.getZ());
                    double r = rVec.length();
                    double a = GM / (r * r);
                    Vec3 aVec = new Vec3(a * rVec.x / r, a * rVec.y / r, a * rVec.z / r);
                    if (selfAttraction){
                        Vec3 vVec = this.getDeltaMovement();
                        this.lerpMotion(vVec.x+aVec.x,vVec.y+aVec.y,vVec.z+aVec.z);
                    }
                    if (attraction){
                        Vec3 vVec2 = entity.getDeltaMovement();
                        entity.lerpMotion(vVec2.x-aVec.x,vVec2.y-aVec.y,vVec2.z-aVec.z);
                    }

                }
            }
        }else{
            target = getTarget(targetClass,angleRestriction,trackingRange);
            if(target!=null) {
                Vec3 rVec = new Vec3(target.getX() - this.getX(), target.getY()+2 - this.getY(), target.getZ() - this.getZ());
                double r = rVec.length();
                double a = GM / (r * r);
                Vec3 aVec = new Vec3(a * rVec.x / r, a * rVec.y / r, a * rVec.z / r);
                if (selfAttraction){
                    Vec3 vVec = this.getDeltaMovement();
                    this.lerpMotion(vVec.x+aVec.x,vVec.y+aVec.y,vVec.z+aVec.z);
                }
                if (attraction){
                    Vec3 vVec2 = target.getDeltaMovement();
                    target.lerpMotion(vVec2.x-aVec.x,vVec2.y-aVec.y,vVec2.z-aVec.z);
                }
            }
        }

    }
    private <T extends Entity> void missilesTracking(Class<T> targetClass, double trackingRange, boolean angleRestriction){
        if (timer == 0) {
            Vec3 vec3 = this.getDeltaMovement();
            v0 = Math.sqrt(vec3.x * vec3.x + vec3.z * vec3.z + vec3.y * vec3.y);
        }
        if (timer > (int) (5 / v0)) {
            if (target == null) {
                target = getTarget(targetClass,angleRestriction,trackingRange);
                this.setNoGravity(false);
            } else if (!level.isClientSide) {
                this.setNoGravity(true);
                Vec3 delta = new Vec3(target.getX() - this.getX(), target.getEyeY() - this.getY(), target.getZ() - this.getZ());
                Vec3 velocity = this.getDeltaMovement();
                double cosTheta = vec2AngleCos(delta.x, delta.z, velocity.x, velocity.z);
                double sinTheta;
                if (cosTheta < Mth.cos((float) (8 * v0 * Mth.DEG_TO_RAD))) {
                    cosTheta = Mth.cos((float) (8 * v0 * Mth.DEG_TO_RAD));
                    sinTheta = Mth.sin((float) (8 * v0 * Mth.DEG_TO_RAD));
                } else {
                    sinTheta = Math.sqrt(1 - cosTheta * cosTheta);
                }
                double vx, vz, vy;
                double d0 = velocity.x * cosTheta - velocity.z * sinTheta;
                double d1 = velocity.x * sinTheta + velocity.z * cosTheta;
                double d2 = velocity.x * cosTheta + velocity.z * sinTheta;
                double d3 = -velocity.x * sinTheta + velocity.z * cosTheta;
                if (d0 * delta.x + d1 * delta.z > d2 * delta.x + d3 * delta.z) {
                    vx = d0;
                    vz = d1;
                } else {
                    vx = d2;
                    vz = d3;
                }
                double vNewX = Math.sqrt(vx * vx + vz * vz);
                double deltaNewX = Math.sqrt(delta.x * delta.x + delta.z * delta.z);
                cosTheta = vec2AngleCos(vNewX, velocity.y, deltaNewX, delta.y);
                if (cosTheta < Mth.cos((float) (8 * v0 * Mth.DEG_TO_RAD))) {
                    cosTheta = Mth.cos((float) (8 * v0 * Mth.DEG_TO_RAD));
                    sinTheta = Mth.sin((float) (8 * v0 * Mth.DEG_TO_RAD));
                } else {
                    sinTheta = Math.sqrt(1 - cosTheta * cosTheta);
                }
                d0 = vNewX * cosTheta - velocity.y * sinTheta;
                d1 = vNewX * sinTheta + velocity.y * cosTheta;
                d2 = vNewX * cosTheta + velocity.y * sinTheta;
                d3 = -vNewX * sinTheta + velocity.y * cosTheta;
                double adjusted;
                if (d0 * deltaNewX + d1 * delta.y > d2 * deltaNewX + d3 * delta.y) {
                    adjusted = d0;
                    vy = d1;
                } else {
                    adjusted = d2;
                    vy = d3;
                }
                vx *= adjusted / vNewX;
                vz *= adjusted / vNewX;
                this.setDeltaMovement(vx, vy, vz);
            }
        }
    }

    public double vec2AngleCos(double x1, double y1, double x2, double y2) {
        return Mth.fastInvSqrt(x1 * x1 + y1 * y1) * Mth.fastInvSqrt(x2 * x2 + y2 * y2) * (x1 * x2 + y1 * y2);
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
