package com.linngdu664.bsf.entity;

import com.linngdu664.bsf.entity.ai.goal.BSFGolemFollowOwnerGoal;
import com.linngdu664.bsf.entity.ai.goal.BSFGolemRandomStrollGoal;
import com.linngdu664.bsf.entity.ai.goal.BSFGolemRangedAttackGoal;
import com.linngdu664.bsf.entity.ai.goal.BSFNearestAttackableTargetGoal;
import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.item.tank.SnowballStorageTankItem;
import com.linngdu664.bsf.item.tank.normal.PowderSnowballStorageTank;
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
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
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
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class BSFSnowGolemEntity extends TamableAnimal implements RangedAttackMob {
    // status flag:
    // 0: standby
    // 1: follow
    // 2: follow & attack
    // 3: patrol & attack
    // 4: turret
    private static final EntityDataAccessor<Byte> STATUS_FLAG = SynchedEntityData.defineId(BSFSnowGolemEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> USE_LOCATOR = SynchedEntityData.defineId(BSFSnowGolemEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> WEAPON_FLAG = SynchedEntityData.defineId(BSFSnowGolemEntity.class, EntityDataSerializers.BOOLEAN);
    private final Container inventory = new BSFSnowGolemContainer();

    public BSFSnowGolemEntity(EntityType<? extends TamableAnimal> p_21803_, Level p_21804_) {
        super(p_21803_, p_21804_);
    }

    public static AttributeSupplier setAttributes() {
        return TamableAnimal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.FOLLOW_RANGE, 50.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .build();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(STATUS_FLAG, (byte) 0);
        entityData.define(USE_LOCATOR, false);
        entityData.define(WEAPON_FLAG, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putByte("Status", getStatus());
        pCompound.putBoolean("UseLocator", getUseLocator());
        pCompound.putBoolean("WeaponFlag", hasWeaponFlag());
        CompoundTag compoundTag = new CompoundTag();
        inventory.getItem(1).save(compoundTag);
        pCompound.put("Weapon", compoundTag);
        compoundTag = new CompoundTag();
        inventory.getItem(0).save(compoundTag);
        pCompound.put("Ammo", compoundTag);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("Status")) {
            setStatus(pCompound.getByte("Status"));
        }
        if (pCompound.contains("UseLocator")) {
            setUseLocator(pCompound.getBoolean("UseLocator"));
        }
        if (pCompound.contains("WeaponFlag")) {
            setWeaponFlag(pCompound.getBoolean("WeaponFlag"));
        }
        if (pCompound.contains("Weapon")) {
            inventory.setItem(1, ItemStack.of(pCompound.getCompound("Weapon")));
        }
        if (pCompound.contains("Ammo")) {
            inventory.setItem(0, ItemStack.of(pCompound.getCompound("Ammo")));
        }
    }

    public byte getStatus() {
        return entityData.get(STATUS_FLAG);
    }

    public void setStatus(byte status) {
        entityData.set(STATUS_FLAG, status);
    }

    public boolean getUseLocator() {
        return entityData.get(USE_LOCATOR);
    }

    public void setUseLocator(boolean useLocator) {
        entityData.set(USE_LOCATOR, useLocator);
    }

    public Container getInventory() {
        return inventory;
    }

    public boolean hasWeaponFlag() {
        return entityData.get(WEAPON_FLAG);
    }

    public void setWeaponFlag(boolean flag) {
        entityData.set(WEAPON_FLAG, flag);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        goalSelector.addGoal(2, new BSFGolemFollowOwnerGoal(this, 1.0D, 5.0F, 3.0F));
        goalSelector.addGoal(3, new BSFGolemRangedAttackGoal(this, 1.25D, 30, 50.0F));
        goalSelector.addGoal(4, new BSFGolemRandomStrollGoal(this, 1.0D, 1.0000001E-5F));
        goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new BSFNearestAttackableTargetGoal(this, Mob.class, 20, true, false, (p_29932_) -> p_29932_ instanceof Enemy));
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player pPlayer, @NotNull InteractionHand pHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pHand);
        if (!level.isClientSide) {
            if (itemStack.getItem() instanceof SnowballStorageTankItem tank && tank.getSnowball().canBeLaunchedByNormalWeapon() && !(tank instanceof PowderSnowballStorageTank) && inventory.getItem(0).isEmpty()) {
                inventory.setItem(0, itemStack.copy());
                if (!pPlayer.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
            } else if ((itemStack.getItem() instanceof SnowballCannonItem || itemStack.getItem() instanceof SnowballShotgunItem) && inventory.getItem(1).isEmpty()) {
                inventory.setItem(1, itemStack.copy());
                if (!pPlayer.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
                setWeaponFlag(true);
            } else if (itemStack.isEmpty()) {
                if (pPlayer.isShiftKeyDown()) {
                    pPlayer.getInventory().placeItemBackInInventory(inventory.getItem(1), true);
                    inventory.removeItem(1, 1);
                    setWeaponFlag(false);
                } else {
                    pPlayer.getInventory().placeItemBackInInventory(inventory.getItem(0), true);
                    inventory.removeItem(0, 1);
                }
            } else if (itemStack.is(ItemRegister.SNOW_GOLEM_MODE_TWEAKER.get())) {
                if (pPlayer.isShiftKeyDown()) {
                    setUseLocator(!getUseLocator());
                    if (getOwner() != null) {
                        getOwner().sendMessage(getUseLocator() ? new TranslatableComponent("snow_golem_locator_true.tip") : new TranslatableComponent("snow_golem_locator_false.tip"), Util.NIL_UUID);
                    }
                    setTarget(null);
                } else {
                    if (getStatus() == 4) {
                        setStatus((byte) 0);
                    } else {
                        setStatus((byte) (getStatus() + 1));
                    }
                    setOrderedToSit(getStatus() == 0);
                    if (getOwner() != null) {
                        getOwner().sendMessage(new TranslatableComponent(switch (getStatus()) {
                            case 0 -> "snow_golem_standby.tip";
                            case 1 -> "snow_golem_follow.tip";
                            case 2 -> "snow_golem_follow_and_attack.tip";
                            case 3 -> "snow_golem_attack.tip";
                            default -> "snow_golem_turret.tip";
                        }), Util.NIL_UUID);
                    }
                }
            } else if (itemStack.getItem() instanceof TargetLocatorItem targetLocator && getUseLocator()) {
                LivingEntity entity = targetLocator.getLivingEntity();
                if (entity != this) {
                    setTarget(targetLocator.getLivingEntity());
                }
            }
        }
        return InteractionResult.SUCCESS;
    }
/*
    void printInfo() {
        System.out.println("print info:");
        System.out.println(this);
        System.out.println("    inventory:" + inventory.getItem(0) + " " + inventory.getItem(1));
        System.out.println("    target:" + getTarget());
        System.out.println("    target mode:" + getUseLocator());
        System.out.println("    behavior:" + getStatus());
    }
*/
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

    /*
        public BSFSnowballEntity itemToEntity(ItemStack tank, LaunchFunc launchFunc) {
            Item item = tank.getItem();
            if (item == ItemRegister.COMPACTED_SNOWBALL_STORAGE_TANK.get()) {
                return new CompactedSnowballEntity(this, level, launchFunc);
            } else if (item == ItemRegister.STONE_SNOWBALL_STORAGE_TANK.get()) {
                return new StoneSnowballEntity(this, level, launchFunc);
            } else if (item == ItemRegister.GLASS_SNOWBALL_STORAGE_TANK.get()) {
                return new GlassSnowballEntity(this, level, launchFunc);
            } else if (item == ItemRegister.IRON_SNOWBALL_STORAGE_TANK.get()) {
                return new IronSnowballEntity(this, level, launchFunc);
            } else if (item == ItemRegister.ICE_SNOWBALL_STORAGE_TANK.get()) {
                return new IceSnowballEntity(this, level, launchFunc);
            } else if (item == ItemRegister.GOLD_SNOWBALL_STORAGE_TANK.get()) {
                return new GoldSnowballEntity(this, level, launchFunc);
            } else if (item == ItemRegister.OBSIDIAN_SNOWBALL_STORAGE_TANK.get()) {
                return new ObsidianSnowballEntity(this, level, launchFunc);
            } else if (item == ItemRegister.EXPLOSIVE_SNOWBALL_STORAGE_TANK.get()) {
                return new ExplosiveSnowballEntity(this, level, launchFunc);
            } else if (item == ItemRegister.SPECTRAL_SNOWBALL_STORAGE_TANK.get()) {
                return new SpectralSnowballEntity(this, level, launchFunc);
            } else if (item == ItemRegister.FROZEN_SNOWBALL_STORAGE_TANK.get()) {
                return new FrozenSnowballEntity(this, level, launchFunc);
            } else if (item == ItemRegister.LIGHT_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get()) {
                return new LightMonsterTrackingSnowballEntity(this, level, launchFunc);
            } else if (item == ItemRegister.HEAVY_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get()) {
                return new HeavyMonsterTrackingSnowballEntity(this, level, launchFunc);
            } else if (item == ItemRegister.EXPLOSIVE_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get()) {
                return new ExplosiveMonsterTrackingSnowballEntity(this, level, launchFunc);
            } else if (item == ItemRegister.LIGHT_PLAYER_TRACKING_SNOWBALL_STORAGE_TANK.get()) {
                return new LightPlayerTrackingSnowballEntity(this, level, launchFunc);
            } else if (item == ItemRegister.HEAVY_PLAYER_TRACKING_SNOWBALL_STORAGE_TANK.get()) {
                return new HeavyPlayerTrackingSnowballEntity(this, level, launchFunc);
            } else if (item == ItemRegister.EXPLOSIVE_PLAYER_TRACKING_SNOWBALL_STORAGE_TANK.get()) {
                return new ExplosivePlayerTrackingSnowballEntity(this, level, launchFunc);
            }
            return null;
        }
    */
    @Override
    public void performRangedAttack(@NotNull LivingEntity pTarget, float pDistanceFactor) {
        ItemStack weapon = inventory.getItem(1);
        ItemStack ammo = inventory.getItem(0);
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
                if (ammo.getItem() instanceof SnowballStorageTankItem tank) {
                    BSFSnowballEntity snowball = tank.getSnowball().getCorrespondingEntity(level, this, launchFunc);
                    snowball.shoot(dx, sinTheta, dz, v, accuracy);
                    level.addFreshEntity(snowball);
                    ammo.setDamageValue(ammo.getDamageValue() + 1);
                    if (ammo.getDamageValue() == 96) {
                        inventory.setItem(0, new ItemStack(ItemRegister.EMPTY_SNOWBALL_STORAGE_TANK.get()));
                    }
                    if (i == 0) {
                        playSound(weapon.getItem() instanceof SnowballShotgunItem ? SoundRegister.SHOTGUN_FIRE_2.get() : SoundRegister.SNOWBALL_CANNON_SHOOT.get(), 1.0F, 1.0F / (getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
                        if (getRandom().nextFloat() <= damageChance) {
                            weapon.setDamageValue(weapon.getDamageValue() + 1);
                            if (weapon.getDamageValue() == 256) {
                                inventory.removeItem(1, 1);
                                playSound(SoundEvents.ITEM_BREAK, 1.0F, 1.0F / (getRandom().nextFloat() * 0.4F + 0.8F));
                            }
                        }
                    }
                } else {
                    break;
                }
            }
        }
    }

    @Override
    public void die(@NotNull DamageSource pCause) {
        super.die(pCause);
        for (int i = 0; i <= 1; i++) {
            ItemStack itemstack = inventory.getItem(i);
            if (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack)) {
                spawnAtLocation(itemstack);
            }
        }
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

    static class BSFSnowGolemContainer extends SimpleContainer {
        private BSFSnowGolemContainer() {
            super(2);
        }
    }
}
