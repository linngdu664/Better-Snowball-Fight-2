package com.linngdu664.bsf.entity;

import com.linngdu664.bsf.util.TankType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.level.Level;

public abstract class AdvancedSnowballEntity extends ThrowableItemProjectile {
    public TankType type;
    public AdvancedSnowballEntity(Level level, LivingEntity livingEntity, TankType type) {
        super(EntityType.EGG, livingEntity, level);
        this.type = type;
    }/*
    public int weaknessTicks = 0;
    public int frozenTicks = 0;
    public double punch = 0.0;
    public float damage = Float.MIN_VALUE;
    public float blazeDamage = 3.0F;
    public TankType type;
    //private Entity target = null;
    private double v0;
    private float maxTurningAngleCos;
    private float maxTurningAngleSin;
    private int timer = 0;
    private int trackingMode;
    private Class<? extends Entity> targetClass;
    private double trackingRange;
    private boolean angleRestriction;
    private double GM;
    private boolean trackingMultipleTargets;
    private boolean selfAttraction;
    private boolean attraction;

    public AdvancedSnowballEntity(Level level, LivingEntity livingEntity, TankType type, float damage, float blazeDamage) {
        super(EntityType.EGG, livingEntity, level);
        this.type = type;
        this.damage = damage;
        this.blazeDamage = blazeDamage;
    }

    public AdvancedSnowballEntity(Level level, double x, double y, double z, TankType type) {
        super(EntityType.EGG, x, y, z, level);
        this.type = type;
    }

    public AdvancedSnowballEntity(Level level, double x, double y, double z, TankType type, float damage, float blazeDamage) {
        super(EntityType.EGG, x, y, z, level);
        this.type = type;
        this.damage = damage;
        this.blazeDamage = blazeDamage;
    }

    public <T extends Entity> void setGravityTracking(Class<T> targetClass, double trackingRange, double GM, boolean angleRestriction, boolean trackingMultipleTargets, boolean selfAttraction, boolean attraction) {
        trackingMode = 2;
        this.targetClass = targetClass;
        this.trackingRange = trackingRange;
        this.GM = GM;
        this.angleRestriction = angleRestriction;
        this.trackingMultipleTargets = trackingMultipleTargets;
        this.selfAttraction = selfAttraction;
        this.attraction = attraction;
    }

    public <T extends Entity> void setMissilesTracking(Class<T> targetClass, double trackingRange, boolean angleRestriction) {
        trackingMode = 1;
        this.targetClass = targetClass;
        this.trackingRange = trackingRange;
        this.angleRestriction = angleRestriction;
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult entityHitResult) {
        if (entityHitResult.getEntity() instanceof LivingEntity entity) {
            if (entity instanceof Player player && (player.getOffhandItem().is(ItemRegister.GLOVE.get()) &&
                    player.getUsedItemHand() == InteractionHand.OFF_HAND || player.getMainHandItem().is(ItemRegister.GLOVE.get()) &&
                    player.getUsedItemHand() == InteractionHand.MAIN_HAND) && player.isUsingItem() && BSFMthUtil.isHeadingToSnowball(player, null)) {
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
                    case TRACKING_MONSTER -> player.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.LIGHT_MONSTER_TRACKING_SNOWBALL.get(), 1), true);
                    case TRACKING_MONSTER_DAMAGE -> player.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.HEAVY_MONSTER_TRACKING_SNOWBALL.get(), 1), true);
                    case TRACKING_MONSTER_EXPLOSIVE -> player.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.EXPLOSIVE_MONSTER_TRACKING_SNOWBALL.get(), 1), true);
                    case TRACKING_PLAYER -> player.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.LIGHT_PLAYER_TRACKING_SNOWBALL.get(), 1), true);
                    case TRACKING_PLAYER_DAMAGE -> player.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.HEAVY_PLAYER_TRACKING_SNOWBALL.get(), 1), true);
                    case TRACKING_PLAYER_EXPLOSIVE -> player.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.EXPLOSIVE_PLAYER_TRACKING_SNOWBALL.get(), 1), true);
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
            if (type == TankType.EXPLOSIVE || type == TankType.TRACKING_PLAYER_EXPLOSIVE || type == TankType.TRACKING_MONSTER_EXPLOSIVE) {
                if (level.getGameRules().getBoolean((GameRules.RULE_MOBGRIEFING))) {
                    level.explode(null, this.getX(), this.getY(), this.getZ(), 1.5F, Explosion.BlockInteraction.DESTROY);
                } else {
                    level.explode(null, this.getX(), this.getY(), this.getZ(), 1.5F, Explosion.BlockInteraction.NONE);
                }
            }
            if (type == TankType.SPECTRAL) {
                entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 0));
            }
            ((ServerLevel) level).sendParticles(ParticleTypes.ITEM_SNOWBALL, this.getX(), this.getY(), this.getZ(), 8, 0, 0, 0, 0);
            ((ServerLevel) level).sendParticles(ParticleTypes.SNOWFLAKE, this.getX(), this.getY(), this.getZ(), 8, 0, 0, 0, 0.04);
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        if (type == TankType.EXPLOSIVE || type == TankType.TRACKING_PLAYER_EXPLOSIVE || type == TankType.TRACKING_MONSTER_EXPLOSIVE) {
            if (level.getGameRules().getBoolean((GameRules.RULE_MOBGRIEFING))) {
                level.explode(null, this.getX(), this.getY(), this.getZ(), 1.5F, Explosion.BlockInteraction.DESTROY);
            } else {
                level.explode(null, this.getX(), this.getY(), this.getZ(), 1.5F, Explosion.BlockInteraction.NONE);
            }
        } else if (type == TankType.BLACK_HOLE) {
            this.discard();
        }
        ((ServerLevel) level).sendParticles(ParticleTypes.ITEM_SNOWBALL, this.getX(), this.getY(), this.getZ(), 8, 0, 0, 0, 0);
        ((ServerLevel) level).sendParticles(ParticleTypes.SNOWFLAKE, this.getX(), this.getY(), this.getZ(), 8, 0, 0, 0, 0.04);
    }

    @Override
    protected void onHit(@NotNull HitResult pResult) {
        super.onHit(pResult);
        if (!level.isClientSide && type != TankType.BLACK_HOLE) {
            this.discard();
        }
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return Items.SNOWBALL;
    }

    @Override
    public void tick() {
        ((ServerLevel) level).sendParticles(ParticleRegister.SHORT_TIME_SNOWFLAKE.get(), this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
        if (type == TankType.SPECTRAL) {
            ((ServerLevel) level).sendParticles(ParticleTypes.INSTANT_EFFECT, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
        }
        if (timer == 0) {
            Vec3 vec3 = this.getDeltaMovement();
            v0 = Math.sqrt(vec3.x * vec3.x + vec3.z * vec3.z + vec3.y * vec3.y);
            maxTurningAngleCos = Mth.cos(7.1619724F * (float) v0 * Mth.DEG_TO_RAD);
            maxTurningAngleSin = Mth.sin(7.1619724F * (float) v0 * Mth.DEG_TO_RAD);
        }
        if (timer == 100 && type == TankType.BLACK_HOLE) {
            this.discard();
        }
        tracking();
        timer++;
        super.tick();
    }

    private void tracking() {
        if (trackingMode == 1 && timer > (int) (5 / v0)) {
            MovingAlgorithm.missilesTracking(this, targetClass, trackingRange, angleRestriction, maxTurningAngleCos, maxTurningAngleSin);
        } else if (trackingMode == 2) {
            MovingAlgorithm.gravityTracking(this, targetClass, trackingRange, GM, angleRestriction, trackingMultipleTargets, selfAttraction, attraction);
        }*/
        /*
        switch (trackingMode) {
            case 1 -> missilesTracking(targetClass, trackingRange, angleRestriction);
            case 2 -> gravityTracking(targetClass, trackingRange, GM, angleRestriction, trackingMultipleTargets, selfAttraction, attraction);
        }
    }*/
}
/*
    private <T extends Entity> Entity getTarget(Class<T> t, boolean angleRestriction, double trackingRange) {
        Entity entity1 = null;
        List<T> list = level.getEntitiesOfClass(t, this.getBoundingBox().inflate(trackingRange, trackingRange, trackingRange), (p_186450_) -> true);
        if (t == Projectile.class) {
            list.remove(this);
        } else if (t == Player.class) {
            list.remove(this.getOwner());
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
            if (angleRestriction) {
                double d1 = entity1.getX() - this.getX();
                double d2 = entity1.getZ() - this.getZ();
                double d3 = this.getDeltaMovement().x;
                double d4 = this.getDeltaMovement().z;
                if (Util.vec2AngleCos(d1, d2, d3, d4) > 0.5) {
                    return entity1;
                }
            } else {
                return entity1;
            }
        }
        return null;
    }

    private <T extends Entity> List<T> getTargetList(Class<T> t, double trackingRange) {
        List<T> list = level.getEntitiesOfClass(t, this.getBoundingBox().inflate(trackingRange, trackingRange, trackingRange), (p_186450_) -> true);
        if (t == Projectile.class) {
            list.remove(this);
        } else if (t == Player.class) {
            list.remove(this.getOwner());
        }
        if (!list.isEmpty()) {
            return list;
        }
        return null;
    }

    //This is not used for tracking in fact, but it has some funny effects.
    private <T extends Entity> void gravityTracking(Class<T> targetClass, double trackingRange, double GM, boolean angleRestriction, boolean trackingMultipleTargets, boolean selfAttraction, boolean attraction) {
        if (trackingMultipleTargets) {
            List<T> list = getTargetList(targetClass, trackingRange);
            if (list != null && !list.isEmpty()) {
                for (T entity : list) {
                    if (angleRestriction) {
                        double d1 = entity.getX() - this.getX();
                        double d2 = entity.getZ() - this.getZ();
                        double d3 = this.getDeltaMovement().x;
                        double d4 = this.getDeltaMovement().z;
                        if (Util.vec2AngleCos(d1, d2, d3, d4) < 0.5) {
                            continue;
                        }
                    }
                    Vec3 rVec = new Vec3(entity.getX() - this.getX(), entity.getEyeY() - this.getY(), entity.getZ() - this.getZ());
                    double r2 = rVec.x * rVec.x + rVec.y + rVec.y + rVec.z * rVec.z;
                    double ir2 = Mth.fastInvSqrt(r2);
                    double a = GM / r2;
                    Vec3 aVec = new Vec3(a * rVec.x * ir2, a * rVec.y * ir2, a * rVec.z * ir2);
                    if (selfAttraction) {
                        Vec3 vVec = this.getDeltaMovement();
                        this.lerpMotion(vVec.x + aVec.x, vVec.y + aVec.y, vVec.z + aVec.z);
                    }
                    if (attraction) {
                        Vec3 vVec2 = entity.getDeltaMovement();
                        entity.lerpMotion(vVec2.x - aVec.x, vVec2.y - aVec.y, vVec2.z - aVec.z);
                    }
                }
            }
        } else {
            target = getTarget(targetClass, angleRestriction, trackingRange);
            if (target != null) {
                Vec3 rVec = new Vec3(target.getX() - this.getX(), target.getEyeY() - this.getY(), target.getZ() - this.getZ());
                double r2 = rVec.x * rVec.x + rVec.y + rVec.y + rVec.z * rVec.z;
                double ir2 = Mth.fastInvSqrt(r2);
                double a = GM / r2;
                Vec3 aVec = new Vec3(a * rVec.x * ir2, a * rVec.y * ir2, a * rVec.z * ir2);
                if (selfAttraction) {
                    Vec3 vVec = this.getDeltaMovement();
                    this.lerpMotion(vVec.x + aVec.x, vVec.y + aVec.y, vVec.z + aVec.z);
                }
                if (attraction) {
                    Vec3 vVec2 = target.getDeltaMovement();
                    target.lerpMotion(vVec2.x - aVec.x, vVec2.y - aVec.y, vVec2.z - aVec.z);
                }
            }
        }
    }

    private <T extends Entity> void missilesTracking(Class<T> targetClass, double trackingRange, boolean angleRestriction) {
        if (timer == 0) {
            Vec3 vec3 = this.getDeltaMovement();
            v0 = Math.sqrt(vec3.x * vec3.x + vec3.z * vec3.z + vec3.y * vec3.y);
            maxTurningAngleCos = Mth.cos((float) (8 * v0 * Mth.DEG_TO_RAD));
            maxTurningAngleSin = Mth.sin((float) (8 * v0 * Mth.DEG_TO_RAD));
        }
        if (timer > (int) (5 / v0)) {
            if (target == null || !target.isAlive()) {
                this.setNoGravity(false);
                target = getTarget(targetClass, angleRestriction, trackingRange);
            } else if (!level.isClientSide) {
                this.setNoGravity(true);
                Vec3 delta = new Vec3(target.getX() - this.getX(), target.getEyeY() - this.getY(), target.getZ() - this.getZ());
                Vec3 velocity = this.getDeltaMovement();
                double cosTheta = Util.vec2AngleCos(delta.x, delta.z, velocity.x, velocity.z);
                double sinTheta;
                if (cosTheta < maxTurningAngleCos) {
                    cosTheta = maxTurningAngleCos;
                    sinTheta = maxTurningAngleSin;
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
                cosTheta = Util.vec2AngleCos(vNewX, velocity.y, deltaNewX, delta.y);
                if (cosTheta < maxTurningAngleCos) {
                    cosTheta = maxTurningAngleCos;
                    sinTheta = maxTurningAngleSin;
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
}
*/