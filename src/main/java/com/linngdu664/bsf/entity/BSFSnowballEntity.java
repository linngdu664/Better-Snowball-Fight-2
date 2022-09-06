package com.linngdu664.bsf.entity;

import com.linngdu664.bsf.item.setter.ItemRegister;
import com.linngdu664.bsf.particle.ParticleRegister;
import com.linngdu664.bsf.util.BSFUtil;
import com.linngdu664.bsf.util.LaunchFrom;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class BSFSnowballEntity extends ThrowableItemProjectile {

    public double punch = 0.0;
    public float damage = Float.MIN_VALUE;
    public float blazeDamage = 3.0F;
    public LaunchFrom launchFrom;
    public int frozenTime = 0;
    public int weaknessTime = 0;
    //You need to distinguish between LaunchFrom and LaunchFunc
    //LaunchFrom is an Enum, LaunchFunc is an Interface

    public BSFSnowballEntity(LivingEntity livingEntity, Level level) {
        super(EntityType.EGG, livingEntity, level);
        this.setItem(new ItemStack(getDefaultItem()));
    }

    public BSFSnowballEntity(LivingEntity livingEntity, Level level, double punch, float damage, float blazeDamage, LaunchFrom launchFrom) {
        super(EntityType.EGG, livingEntity, level);
        this.punch = punch;
        this.damage = damage;
        this.blazeDamage = blazeDamage;
        this.launchFrom = launchFrom;
        this.setItem(new ItemStack(getDefaultItem()));
    }

    /**
     * Triggered when hit something
     * @param pResult HitResult
     */
    @Override
    protected void onHit(@NotNull HitResult pResult) {
        //hit particle
        ((ServerLevel) level).sendParticles(ParticleTypes.ITEM_SNOWBALL, this.getX(), this.getY(), this.getZ(), 8, 0, 0, 0, 0);
        ((ServerLevel) level).sendParticles(ParticleTypes.SNOWFLAKE, this.getX(), this.getY(), this.getZ(), 8, 0, 0, 0, 0.04);
        super.onHit(pResult);
    }

    /**
     * Triggered when an entity is encountered
     * @param pResult EntityHitResult
     */
    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        if(pResult.getEntity() instanceof LivingEntity entity){
            //Handling the catch
            if(catchOnGlove(entity)){return;}

            //Damage entity
            float hurt = entity instanceof Blaze ? blazeDamage : damage;
            entity.hurt(DamageSource.thrown(this, this.getOwner()), hurt);

            //Push entity
            Vec3 vec3d = this.getDeltaMovement().multiply(0.1 * punch, 0.0, 0.1 * punch);
            entity.push(vec3d.x, 0.0, vec3d.z);
        }
    }

    /**
     * This method will be called every tick
     */
    @Override
    public void tick() {
        //tracks particle
        ((ServerLevel) level).sendParticles(ParticleRegister.SHORT_TIME_SNOWFLAKE.get(), this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
        super.tick();
    }

    /**
     * You must override this fucking function!
     * You need to write the entity's corresponding item in each subclass
     * @return Bound item
     */
    @Override
    protected @NotNull Item getDefaultItem() {
        return null;
    }

    /**
     *
     * @param entity Target
     * @return If the glove catches return true
     */
    protected boolean catchOnGlove(LivingEntity entity){
        if (entity instanceof Player player && (player.getOffhandItem().is(ItemRegister.GLOVE.get()) &&
                player.getUsedItemHand() == InteractionHand.OFF_HAND || player.getMainHandItem().is(ItemRegister.GLOVE.get()) &&
                player.getUsedItemHand() == InteractionHand.MAIN_HAND) && player.isUsingItem() && BSFUtil.isHeadingToSnowball(player, this)) {
            player.getInventory().placeItemBackInInventory(new ItemStack(getDefaultItem()));
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


    /**
     * This method is used for overriding, there is no need to write anything inside
     */
    public void setting() {/*The special properties of the snowball will be set here in subclass*/}



    //Setters with builder style
    //Like this:  snowballEntity.setPunch(2.0).setDamage(2.5f)
    public BSFSnowballEntity setPunch(double punch) {
        this.punch = punch;
        return this;
    }

    public BSFSnowballEntity setDamage(float damage) {
        this.damage = damage;
        return this;
    }

    public BSFSnowballEntity setBlazeDamage(float blazeDamage) {
        this.blazeDamage = blazeDamage;
        return this;
    }

    public BSFSnowballEntity setLaunchFrom(LaunchFrom launchFrom) {
        this.launchFrom = launchFrom;
        return this;
    }

    public BSFSnowballEntity setFrozenTime(int frozenTime) {
        this.frozenTime = frozenTime;
        return this;
    }

    public BSFSnowballEntity setWeaknessTime(int weaknessTime) {
        this.weaknessTime = weaknessTime;
        return this;
    }
}
