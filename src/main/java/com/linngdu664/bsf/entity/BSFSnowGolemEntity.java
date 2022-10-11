package com.linngdu664.bsf.entity;

import com.linngdu664.bsf.entity.ai.goal.BSFGolemFollowOwnerGoal;
import com.linngdu664.bsf.entity.ai.goal.BSFGolemRandomStrollGoal;
import com.linngdu664.bsf.entity.ai.goal.BSFGolemRangedAttackGoal;
import com.linngdu664.bsf.entity.ai.goal.BSFNearestAttackableTargetGoal;
import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.item.tank.AbstractSnowballTankItem;
import com.linngdu664.bsf.item.tank.special.PowderSnowballTank;
import com.linngdu664.bsf.item.tool.SnowGolemModeTweakerItem;
import com.linngdu664.bsf.item.tool.SnowballClampItem;
import com.linngdu664.bsf.item.tool.TargetLocatorItem;
import com.linngdu664.bsf.item.weapon.FreezingSnowballCannonItem;
import com.linngdu664.bsf.item.weapon.PowerfulSnowballCannonItem;
import com.linngdu664.bsf.item.weapon.SnowballCannonItem;
import com.linngdu664.bsf.item.weapon.SnowballShotgunItem;
import com.linngdu664.bsf.util.BSFMthUtil;
import com.linngdu664.bsf.util.LaunchFunc;
import com.linngdu664.bsf.util.SoundRegister;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class BSFSnowGolemEntity extends TamableAnimal implements RangedAttackMob {
    /*
     status flag:
     0: standby
     1: follow
     2: follow & attack
     3: patrol & attack
     4: turret
     */
    private static final EntityDataAccessor<Byte> STATUS_FLAG = SynchedEntityData.defineId(BSFSnowGolemEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> USE_LOCATOR = SynchedEntityData.defineId(BSFSnowGolemEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<ItemStack> WEAPON = SynchedEntityData.defineId(BSFSnowGolemEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<ItemStack> AMMO = SynchedEntityData.defineId(BSFSnowGolemEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Integer> WEAPON_ANG = SynchedEntityData.defineId(BSFSnowGolemEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> STYLE = SynchedEntityData.defineId(BSFSnowGolemEntity.class, EntityDataSerializers.BYTE);


    public BSFSnowGolemEntity(EntityType<? extends TamableAnimal> p_21803_, Level p_21804_) {
        super(p_21803_, p_21804_);
    }

    public static AttributeSupplier setAttributes() {
        return TamableAnimal.createLivingAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.FOLLOW_RANGE, 50.0).add(Attributes.MOVEMENT_SPEED, 0.3).build();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(STATUS_FLAG, (byte) 0);
        entityData.define(USE_LOCATOR, false);
        entityData.define(WEAPON, ItemStack.EMPTY);
        entityData.define(AMMO, ItemStack.EMPTY);
        entityData.define(WEAPON_ANG, 0);
        entityData.define(STYLE, (byte) (BSFMthUtil.randInt(1, 9)));
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putByte("Status", getStatus());
        pCompound.putBoolean("UseLocator", isUseLocator());
        CompoundTag compoundTag = new CompoundTag();
        getWeapon().save(compoundTag);
        pCompound.put("Weapon", compoundTag);
        compoundTag = new CompoundTag();
        getAmmo().save(compoundTag);
        pCompound.put("Ammo", compoundTag);
        pCompound.putInt("WeaponAng", getWeaponAng());
        pCompound.putInt("Style", getStyle());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        setStatus(pCompound.getByte("Status"));
        setUseLocator(pCompound.getBoolean("UseLocator"));
        setWeapon(ItemStack.of(pCompound.getCompound("Weapon")));
        setAmmo(ItemStack.of(pCompound.getCompound("Ammo")));
        setWeaponAng(pCompound.getInt("WeaponAng"));
        setStyle(pCompound.getByte("Style"));
    }

    public byte getStatus() {
        return entityData.get(STATUS_FLAG);
    }

    public void setStatus(byte status) {
        entityData.set(STATUS_FLAG, status);
    }

    public boolean isUseLocator() {
        return entityData.get(USE_LOCATOR);
    }

    public void setUseLocator(boolean useLocator) {
        entityData.set(USE_LOCATOR, useLocator);
    }

    public ItemStack getWeapon() {
        return entityData.get(WEAPON);
    }

    public void setWeapon(ItemStack itemStack) {
        entityData.set(WEAPON, itemStack);
    }

    public ItemStack getAmmo() {
        return entityData.get(AMMO);
    }

    public void setAmmo(ItemStack itemStack) {
        entityData.set(AMMO, itemStack);
    }

    public int getWeaponAng() {
        return entityData.get(WEAPON_ANG);
    }

    public void setWeaponAng(int ang) {
        entityData.set(WEAPON_ANG, ang);
    }

    public byte getStyle() {
        return entityData.get(STYLE);
    }

    public void setStyle(byte style) {
        entityData.set(STYLE, style);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        goalSelector.addGoal(2, new BSFGolemFollowOwnerGoal(this, 1.0, 5.0F, 3.0F));
        goalSelector.addGoal(3, new BSFGolemRangedAttackGoal(this, 1.25, 30, 50.0F));
        goalSelector.addGoal(4, new BSFGolemRandomStrollGoal(this, 1.0, 1.0000001E-5F));
        goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new BSFNearestAttackableTargetGoal(this, Mob.class, 20, true, false, (p_29932_) -> p_29932_ instanceof Enemy));
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player pPlayer, @NotNull InteractionHand pHand) {
        if (!level.isClientSide && pPlayer.equals(getOwner())) {
            ItemStack itemStack = pPlayer.getItemInHand(pHand);
            if (itemStack.getItem() instanceof AbstractSnowballTankItem tank && tank.getSnowball().canBeLaunchedByNormalWeapon() && !(tank instanceof PowderSnowballTank) && getAmmo().isEmpty()) {
                setAmmo(itemStack.copy());
                if (!pPlayer.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
            } else if ((itemStack.getItem() instanceof SnowballCannonItem || itemStack.getItem() instanceof SnowballShotgunItem) && getWeapon().isEmpty()) {
                setWeapon(itemStack.copy());
                if (!pPlayer.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
            } else if (itemStack.isEmpty()) {
                if (pPlayer.isShiftKeyDown()) {
                    pPlayer.getInventory().placeItemBackInInventory(getWeapon(), true);
                    setWeapon(ItemStack.EMPTY);
                } else {
                    pPlayer.getInventory().placeItemBackInInventory(getAmmo(), true);
                    setAmmo(ItemStack.EMPTY);
                }
            } else if (itemStack.getItem() instanceof SnowGolemModeTweakerItem snowGolemModeTweaker) {
                setUseLocator(snowGolemModeTweaker.isUseLocator());
                setTarget(null);
                setStatus(snowGolemModeTweaker.getState());
                setOrderedToSit(getStatus() == 0);
                pPlayer.sendMessage(new TranslatableComponent("import_state.tip"), Util.NIL_UUID);
            } else if (itemStack.getItem() instanceof TargetLocatorItem targetLocator && isUseLocator()) {
                LivingEntity entity = targetLocator.getLivingEntity();
                if (entity != this && getOwner() != null) {
                    getOwner().sendMessage(new TranslatableComponent("snow_golem_locator_tip"), Util.NIL_UUID);
                    setTarget(targetLocator.getLivingEntity());
                }
            } else if (itemStack.getItem() instanceof SnowballClampItem) {
                pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.SMOOTH_SNOWBALL.get(), 1), true);
                itemStack.hurtAndBreak(1, pPlayer, (e) -> e.broadcastBreakEvent(pHand));
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean isSensitiveToWater() {
        return true;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!level.isClientSide) {
            int i = Mth.floor(getX());
            int j = Mth.floor(getY());
            int k = Mth.floor(getZ());
            BlockPos blockpos = new BlockPos(i, j, k);
            Biome biome = level.getBiome(blockpos).value();
            if (biome.shouldSnowGolemBurn(blockpos)) {
                hurt(DamageSource.ON_FIRE, 1.0F);
            }
            if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(level, this)) {
                return;
            }
            BlockState blockstate = Blocks.SNOW.defaultBlockState();
            for (int l = 0; l < 4; ++l) {
                i = Mth.floor(getX() + (double) ((float) (l % 2 * 2 - 1) * 0.25F));
                j = Mth.floor(getY());
                k = Mth.floor(getZ() + (double) ((float) (l / 2 % 2 * 2 - 1) * 0.25F));
                BlockPos blockPos1 = new BlockPos(i, j, k);
                if (level.isEmptyBlock(blockpos) && blockstate.canSurvive(level, blockpos)) {
                    level.setBlockAndUpdate(blockPos1, blockstate);
                }
            }
        }
    }


    @Override
    public void performRangedAttack(@NotNull LivingEntity pTarget, float pDistanceFactor) {
        ItemStack weapon = getWeapon();
        ItemStack ammo = getAmmo();
        float damageChance = 1.0F / (1.0F + EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, weapon));
        float v = 3.0F;
        float accuracy = 1.0F;
        LaunchFunc launchFunc = null;
        if (weapon.getItem() == ItemRegister.SNOWBALL_CANNON.get()) {
            launchFunc = SnowballCannonItem.getLaunchFunc(1);
        } else if (weapon.getItem() == ItemRegister.POWERFUL_SNOWBALL_CANNON.get()) {
            launchFunc = PowerfulSnowballCannonItem.getLaunchFunc(1);
            v = 4.0F;
        } else if (weapon.getItem() == ItemRegister.FREEZING_SNOWBALL_CANNON.get()) {
            launchFunc = FreezingSnowballCannonItem.getLaunchFunc(1);
        } else if (weapon.getItem() == ItemRegister.SNOWBALL_SHOTGUN.get()) {
            launchFunc = SnowballShotgunItem.getLaunchFunc();
            v = 2.0F;
            accuracy = 10.0F;
        }
        if (!weapon.isEmpty() && launchFunc != null) {
            double h = pTarget.getEyeY() - getEyeY();
            double dx = pTarget.getX() - getX();
            double dz = pTarget.getZ() - getZ();
            double x2 = BSFMthUtil.modSqr(dx, dz);
            double d = Math.sqrt(x2 + h * h);
            double x = Math.sqrt(x2);
            // 0.5 * g / 400.0, g = 12
            double k = 0.015 * x2 / (v * v);
            double cosTheta = 0.7071067811865475 / d * Math.sqrt(x2 - 2 * k * h + x * Math.sqrt(x2 - 4 * k * k - 4 * k * h));
            double sinTheta;
            dx /= x;
            dz /= x;
            if (cosTheta > 1) {
                sinTheta = 0;
            } else {
                sinTheta = Math.sqrt(1 - cosTheta * cosTheta);
                dx *= cosTheta;
                dz *= cosTheta;
                if (h < -k) {
                    sinTheta = -sinTheta;
                }
            }
            int j = weapon.getItem() instanceof SnowballShotgunItem ? 4 : 1;
            for (int i = 0; i < j; i++) {
                if (ammo.getItem() instanceof AbstractSnowballTankItem tank) {
                    AbstractBSFSnowballEntity snowball = tank.getSnowball().getCorrespondingEntity(level, this, launchFunc);
                    snowball.shoot(dx, sinTheta, dz, v, accuracy);
                    level.addFreshEntity(snowball);//todo add particles
                    ammo.setDamageValue(ammo.getDamageValue() + 1);
                    if (ammo.getDamageValue() == 96) {
                        setAmmo(new ItemStack(ItemRegister.EMPTY_SNOWBALL_STORAGE_TANK.get()));
                    }
                    if (i == 0) {
                        playSound(weapon.getItem() instanceof SnowballShotgunItem ? SoundRegister.SHOTGUN_FIRE_2.get() : SoundRegister.SNOWBALL_CANNON_SHOOT.get(), 1.0F, 1.0F / (getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
                        if (getRandom().nextFloat() <= damageChance) {
                            weapon.setDamageValue(weapon.getDamageValue() + 1);
                            if (weapon.getDamageValue() == 256) {
                                setWeapon(ItemStack.EMPTY);
                                playSound(SoundEvents.ITEM_BREAK, 1.0F, 1.0F / (getRandom().nextFloat() * 0.4F + 0.8F));
                            }
                        }
                    }
                    setWeaponAng(360);
                } else {
                    break;
                }
            }

        }
    }

    @Override
    public void die(@NotNull DamageSource pCause) {
        super.die(pCause);
        if (!getWeapon().isEmpty() && !EnchantmentHelper.hasVanishingCurse(getWeapon())) {
            spawnAtLocation(getWeapon());
        }
        if (!getAmmo().isEmpty() && !EnchantmentHelper.hasVanishingCurse(getAmmo())) {
            spawnAtLocation(getAmmo());
        }
        spawnAtLocation(new ItemStack(Items.SNOWBALL, BSFMthUtil.randInt(0, 16)));
    }

    @Override
    public void tick() {
        if (getWeaponAng() > 0) {
            setWeaponAng(getWeaponAng() - 72);
        }
        super.tick();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        return SoundEvents.SNOW_GOLEM_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SNOW_GOLEM_DEATH;
    }

    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel p_146743_, @NotNull AgeableMob p_146744_) {
        return null;
    }
}
