package com.linngdu664.bsf.entity;

import com.linngdu664.bsf.item.setter.ItemRegister;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class AdvancedSnowballEntity extends Snowball {
    public int weaknessTicks = 0;
    public int frozenTicks = 0;
    public double punch = 0.0;
    public float damage = Float.MIN_VALUE;
    public float blazeDamage = 3.0F;
    public boolean explode = false;
    public SnowballType type;

    public AdvancedSnowballEntity(Level level, LivingEntity livingEntity,SnowballType type) {
        super(level, livingEntity);
        this.type=type;
    }
    public AdvancedSnowballEntity(Level level, LivingEntity livingEntity,SnowballType type,float damage,float blazeDamage) {
        super(level, livingEntity);
        this.type=type;
        this.damage = damage;
        this.blazeDamage = blazeDamage;
    }

    public AdvancedSnowballEntity(Level level, double x, double y, double z, SnowballType type) {
        super(level, x, y, z);
        this.type = type;
    }
    public AdvancedSnowballEntity(Level level, double x, double y, double z,SnowballType type,float damage,float blazeDamage) {
        super(level, x, y, z);
        this.type=type;
        this.damage = damage;
        this.blazeDamage = blazeDamage;
    }

    private boolean isHeadingToEntity(Player player) {
        float pitch = player.getXRot() * 0.01745329F;
        float yaw = player.getYRot() * 0.01745329F;
        Vec3 speedVec = this.getDeltaMovement().normalize();
        Vec3 cameraVec = new Vec3(-Mth.cos(pitch) * Mth.sin(yaw), -Mth.sin(pitch), Mth.cos(pitch) * Mth.cos(yaw));
        return Mth.abs((float) (cameraVec.dot(speedVec)+1.0F))<0.2F;
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        if(entityHitResult.getEntity() instanceof LivingEntity entity){
            if(
                    entity instanceof Player player &&(
                            player.getOffhandItem().sameItemStackIgnoreDurability(new ItemStack(ItemRegister.GLOVE.get())) &&
                            player.getUsedItemHand()== InteractionHand.OFF_HAND ||
                            player.getMainHandItem().sameItemStackIgnoreDurability(new ItemStack(ItemRegister.GLOVE.get())) &&
                            player.getUsedItemHand()== InteractionHand.MAIN_HAND)&&
                    player.isUsingItem() &&
                    isHeadingToEntity(player)
            ){
                switch (type){
                    case SMOOTH ->player.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.SMOOTH_SNOWBALL.get(),1),true);
                    case COMPACTED ->player.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.COMPACTED_SNOWBALL.get(),1),true);
                    case STONE ->player.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.STONE_SNOWBALL.get(),1),true);
                    case GLASS ->player.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.GLASS_SNOWBALL.get(),1),true);
                    case IRON ->player.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.IRON_SNOWBALL.get(),1),true);
                    case GOLD ->player.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.GOLD_SNOWBALL.get(),1),true);
                    case OBSIDIAN ->player.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.OBSIDIAN_SNOWBALL.get(),1),true);
                    case EXPLOSIVE ->player.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.EXPLOSIVE_SNOWBALL.get(),1),true);
                    case VANILLA ->player.getInventory().placeItemBackInInventory(new ItemStack(Items.SNOWBALL,1),true);
                    case ICE ->player.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.ICE_SNOWBALL.get(),1),true);
                }
                if(player.getMainHandItem().sameItemStackIgnoreDurability(new ItemStack(ItemRegister.GLOVE.get()))){
                    player.getMainHandItem().hurtAndBreak(1,player,(e)->e.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                }else if(player.getOffhandItem().sameItemStackIgnoreDurability(new ItemStack(ItemRegister.GLOVE.get()))){
                    player.getMainHandItem().hurtAndBreak(1,player,(e)->e.broadcastBreakEvent(EquipmentSlot.OFFHAND));
                }
                level.playSound(null,player.getX(),player.getY(),player.getZ(), SoundEvents.SNOW_BREAK, SoundSource.NEUTRAL,3F,0.4F/level.getRandom().nextFloat() * 0.4F + 0.8F);
                return;
            }
            float i = entity instanceof Blaze ? blazeDamage : damage;
            entity.hurt(DamageSource.thrown(this,this.getOwner()),i);
            if(frozenTicks>0){
                entity.setTicksFrozen(frozenTicks);
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,20,1));
            }
            if(weaknessTicks>0){
                entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,weaknessTicks,1));
            }
            Vec3 vec3d = this.getDeltaMovement().multiply(0.1 * punch, 0.0, 0.1 * punch);
            entity.push(vec3d.x,0.0,vec3d.z);
            if(explode){
                if(level.getGameRules().getBoolean((GameRules.RULE_MOBGRIEFING))){
                    level.explode(null,this.getX(),this.getY(),this.getZ(),1.5F, Explosion.BlockInteraction.DESTROY);
                }else {
                    level.explode(null,this.getX(),this.getY(),this.getZ(),1.5F, Explosion.BlockInteraction.NONE);
                }
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        if (explode){
            if (level.getGameRules().getBoolean((GameRules.RULE_MOBGRIEFING))){
                level.explode(null,this.getX(),this.getY(),this.getZ(),1.5F, Explosion.BlockInteraction.DESTROY);
            } else {
                level.explode(null,this.getX(),this.getY(),this.getZ(),1.5F, Explosion.BlockInteraction.NONE);
            }
        }
    }
}
