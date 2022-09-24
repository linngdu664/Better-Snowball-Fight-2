package com.linngdu664.bsf.entity;

import com.linngdu664.bsf.entity.goal.BSFGolemFollowOwnerGoal;
import com.linngdu664.bsf.entity.goal.BSFGolemRandomStrollGoal;
import com.linngdu664.bsf.entity.goal.BSFGolemRangedAttackGoal;
import com.linngdu664.bsf.entity.snowball.nomal_snowball.*;
import com.linngdu664.bsf.entity.snowball.tracking_snowball.*;
import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.item.tank.SnowballStorageTankItem;
import com.linngdu664.bsf.item.weapon.FreezingSnowballCannonItem;
import com.linngdu664.bsf.item.weapon.PowerfulSnowballCannonItem;
import com.linngdu664.bsf.item.weapon.SnowballCannonItem;
import com.linngdu664.bsf.item.weapon.SnowballShotgunItem;
import com.linngdu664.bsf.util.BSFMthUtil;
import com.linngdu664.bsf.util.LaunchFunc;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BSFSnowGolemEntity extends TamableAnimal implements RangedAttackMob {
    // flag:
    // 0: standby
    // 1: follow
    // 2: follow and attack
    // 3: attack
    // 4: turret
    private static final EntityDataAccessor<Byte> STATUS_FLAG = SynchedEntityData.defineId(BSFSnowGolemEntity.class, EntityDataSerializers.BYTE);
    private final Container inventory = new BSFSnowGolemContainer();

    public BSFSnowGolemEntity(EntityType<? extends TamableAnimal> p_21803_, Level p_21804_) {
        super(p_21803_, p_21804_);
    }

    public BSFSnowGolemEntity(EntityType<? extends TamableAnimal> p_21803_, Level p_21804_, Player player) {
        super(p_21803_, p_21804_);
        setPos(player.getX(), player.getY(), player.getZ());
        setTame(true);
        setOrderedToSit(true);
        setOwnerUUID(player.getUUID());
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
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putByte("Status", getStatus());
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
        if (pCompound.contains("Weapon")) {
            inventory.setItem(1, ItemStack.of(pCompound.getCompound("Weapon")));
        }
        if (pCompound.contains("Ammo")) {
            inventory.setItem(0, ItemStack.of(pCompound.getCompound("Ammo")));
        }
    }

    public byte getStatus() {
        return this.entityData.get(STATUS_FLAG);
    }

    public void setStatus(byte status) {
        this.entityData.set(STATUS_FLAG, status);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Mob.class, 20, true, false, (p_29932_) -> p_29932_ instanceof Enemy));
        this.goalSelector.addGoal(2, new BSFGolemFollowOwnerGoal(this, 1.0D, 5.0F, 3.0F));
        this.goalSelector.addGoal(3, new BSFGolemRangedAttackGoal(this, 1.25D, 30, 50.0F));
        this.goalSelector.addGoal(4, new BSFGolemRandomStrollGoal(this, 1.0D, 1.0000001E-5F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player pPlayer, @NotNull InteractionHand pHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pHand);
        if (!level.isClientSide) {
            if (itemStack.is(ItemRegister.SNOW_GOLEM_MODE_TWEAKER.get())) {
                if (pPlayer.isShiftKeyDown()) {
                    // todo: change attack mode
                } else {
                    if (getStatus() == 4) {
                        setStatus((byte) 0);
                    } else {
                        setStatus((byte) (getStatus() + 1));
                    }
                    setOrderedToSit(getStatus() == 0);
                }
            } else if (itemStack.getItem() instanceof SnowballStorageTankItem) {
                if (inventory.getItem(0).isEmpty()) {
                    inventory.setItem(0, itemStack.copy());
                }
                if (!pPlayer.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
            } else if (itemStack.getItem() instanceof SnowballCannonItem || itemStack.getItem() instanceof SnowballShotgunItem) {
                if (inventory.getItem(1).isEmpty()) {
                    inventory.setItem(1, itemStack.copy());
                }
                if (!pPlayer.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
            } else {
                if (pPlayer.isShiftKeyDown()) {
                    pPlayer.getInventory().placeItemBackInInventory(inventory.getItem(1), true);
                    inventory.removeItem(1, 1);
                } else {
                    pPlayer.getInventory().placeItemBackInInventory(inventory.getItem(0), true);
                    inventory.removeItem(0, 1);
                }
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

    @Override
    public void performRangedAttack(@NotNull LivingEntity pTarget, float pDistanceFactor) {
        ItemStack weapon = inventory.getItem(1);
        ItemStack ammo = inventory.getItem(0);
        float damageChance = 1.0F / (1.0F + EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, weapon));
        float v = 1.0F;
        LaunchFunc launchFunc = null;
        if (weapon.getItem() == ItemRegister.SNOWBALL_CANNON.get()) {
            launchFunc = SnowballCannonItem.getLaunchFunc(1);
            v = 3.0F;
        } else if (weapon.getItem() == ItemRegister.POWERFUL_SNOWBALL_CANNON.get()) {
            launchFunc = PowerfulSnowballCannonItem.getLaunchFunc(1);
            v = 4.0F;
        } else if (weapon.getItem() == ItemRegister.FREEZING_SNOWBALL_CANNON.get()) {
            launchFunc = FreezingSnowballCannonItem.getLaunchFunc(1);
            v = 3.0F;
        } else if (weapon.getItem() == ItemRegister.SNOWBALL_SHOTGUN.get()) {
            launchFunc = SnowballShotgunItem.getLaunchFunc();
            v = 2.0F;
        }
        if (!weapon.isEmpty() && launchFunc != null) {
            BSFSnowballEntity snowball = itemToEntity(ammo, launchFunc);
            if (snowball != null) {
                double h = pTarget.getEyeY() - getEyeY();
                double dx = pTarget.getX() - getX();
                double dz = pTarget.getZ() - getZ();
                double x2 = BSFMthUtil.modSqr(dx, dz);
                double d = Math.sqrt(x2 + h * h);
                double x = Math.sqrt(x2);
                // 0.5 * g / 400.0
                double k = 0.0125 * x2 / (v * v);
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
                snowball.shoot(dx, sinTheta, dz, v, 1.0F);
                level.addFreshEntity(snowball);
                playSound(SoundEvents.SNOW_GOLEM_SHOOT, 1.0F, 0.4F / (getRandom().nextFloat() * 0.4F + 0.8F));
                ammo.setDamageValue(ammo.getDamageValue() + 1);
                if (ammo.getDamageValue() == 96) {
                    inventory.setItem(0, new ItemStack(ItemRegister.EMPTY_SNOWBALL_STORAGE_TANK.get()));
                }
                if (getRandom().nextFloat() <= damageChance) {
                    weapon.setDamageValue(weapon.getDamageValue() + 1);
                    if (weapon.getDamageValue() == 256) {
                        inventory.removeItem(1, 1);
                        level.playSound(null, getX(), getY(), getZ(), SoundEvents.ITEM_BREAK, SoundSource.NEUTRAL, 1.0F, 1.0F / (getRandom().nextFloat() * 0.4F + 0.8F));
                    }
                }
            }
        }
    }

    @Nullable
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
