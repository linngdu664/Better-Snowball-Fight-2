package com.linngdu664.bsf.entity;

import com.linngdu664.bsf.entity.goal.BSFGolemFollowOwnerGoal;
import com.linngdu664.bsf.entity.goal.BSFGolemRandomStrollGoal;
import com.linngdu664.bsf.entity.goal.BSFGolemRangedAttackGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BSFSnowGolemEntity extends TamableAnimal implements RangedAttackMob {
    private BSFSnowGolemMode mode = BSFSnowGolemMode.STANDBY;

    public BSFSnowGolemEntity(EntityType<? extends TamableAnimal> p_21803_, Level p_21804_) {
        super(p_21803_, p_21804_);
    }

    public BSFSnowGolemEntity(EntityType<? extends TamableAnimal> p_21803_, Level p_21804_, Player player) {
        super(p_21803_, p_21804_);
        setPos(player.getX(), player.getY(), player.getZ());
        setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        setTame(true);
        setOrderedToSit(true);
        setOwnerUUID(player.getUUID());
    }

    public static AttributeSupplier setAttributes() {
        return TamableAnimal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 1.2)
                .add(Attributes.FOLLOW_RANGE, 15.0)
                .build();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Mob.class, 10, true, false, (p_29932_) -> p_29932_ instanceof Enemy));
        this.goalSelector.addGoal(2, new BSFGolemFollowOwnerGoal(this, 1.0D, 6.0F, 3.0F));
        this.goalSelector.addGoal(3, new BSFGolemRangedAttackGoal(this, 1.25D, 20, 20.0F));
        this.goalSelector.addGoal(4, new BSFGolemRandomStrollGoal(this, 1.0D, 1.0000001E-5F));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player pPlayer, @NotNull InteractionHand pHand) {
        if (!level.isClientSide) {
            mode = switch (mode) {
                case STANDBY -> BSFSnowGolemMode.FOLLOW;
                case TURRET -> BSFSnowGolemMode.STANDBY;
                case FOLLOW -> BSFSnowGolemMode.FOLLOW_AND_ATTACK;
                case ATTACK -> BSFSnowGolemMode.TURRET;
                case FOLLOW_AND_ATTACK -> BSFSnowGolemMode.ATTACK;
            };
            this.setOrderedToSit(mode == BSFSnowGolemMode.STANDBY);
        }
        return InteractionResult.SUCCESS;
    }

    public BSFSnowGolemMode getMode() {
        return mode;
    }

    @Override
    public boolean isSensitiveToWater() {
        return true;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel p_146743_, @NotNull AgeableMob p_146744_) {
        return null;
    }

    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide) {
            int i = Mth.floor(this.getX());
            int j = Mth.floor(this.getY());
            int k = Mth.floor(this.getZ());
            BlockPos blockpos = new BlockPos(i, j, k);
            Biome biome = this.level.getBiome(blockpos).value();
            if (biome.shouldSnowGolemBurn(blockpos)) {
                this.hurt(DamageSource.ON_FIRE, 1.0F);
            }

            if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this)) {
                return;
            }

            BlockState blockstate = Blocks.SNOW.defaultBlockState();

            for (int l = 0; l < 4; ++l) {
                i = Mth.floor(this.getX() + (double) ((float) (l % 2 * 2 - 1) * 0.25F));
                j = Mth.floor(this.getY());
                k = Mth.floor(this.getZ() + (double) ((float) (l / 2 % 2 * 2 - 1) * 0.25F));
                BlockPos blockPos1 = new BlockPos(i, j, k);
                if (this.level.isEmptyBlock(blockpos) && blockstate.canSurvive(this.level, blockpos)) {
                    this.level.setBlockAndUpdate(blockPos1, blockstate);
                }
            }
        }
    }

    @Override
    public void performRangedAttack(@NotNull LivingEntity pTarget, float pDistanceFactor) {
        Snowball snowball = new Snowball(this.level, this);
        double d0 = pTarget.getEyeY() - (double)1.1F;
        double d1 = pTarget.getX() - this.getX();
        double d2 = d0 - snowball.getY();
        double d3 = pTarget.getZ() - this.getZ();
        double d4 = Math.sqrt(d1 * d1 + d3 * d3) * (double)0.2F;
        snowball.shoot(d1, d2 + d4, d3, 1.6F, 12.0F);
        this.playSound(SoundEvents.SNOW_GOLEM_SHOOT, 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(snowball);
    }
}
