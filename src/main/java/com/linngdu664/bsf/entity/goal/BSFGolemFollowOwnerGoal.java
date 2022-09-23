package com.linngdu664.bsf.entity.goal;

import com.linngdu664.bsf.entity.BSFSnowGolemEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

import java.util.EnumSet;

public class BSFGolemFollowOwnerGoal extends Goal {
    private final BSFSnowGolemEntity golem;
    private final LevelReader level;
    private final double speedModifier;
    private final PathNavigation navigation;
    private final float stopDistance;
    private final float startDistance;
    private LivingEntity owner;
    private int timeToRecalcPath;

    public BSFGolemFollowOwnerGoal(BSFSnowGolemEntity golem, double pSpeedModifier, float pStartDistance, float pStopDistance) {
        this.golem = golem;
        this.level = golem.level;
        this.speedModifier = pSpeedModifier;
        this.navigation = golem.getNavigation();
        this.startDistance = pStartDistance;
        this.stopDistance = pStopDistance;
        golem.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean canUse() {
        LivingEntity livingentity = this.golem.getOwner();
        if (livingentity == null || livingentity.isSpectator() || golem.isOrderedToSit() ||
                golem.distanceToSqr(livingentity) < (double) (startDistance * startDistance) ||
                golem.getStatus() == 3 || golem.getStatus() == 4) {
            return false;
        } else {
            this.owner = livingentity;
            return true;
        }
    }

    public boolean canContinueToUse() {
        if (this.navigation.isDone() || this.golem.isOrderedToSit()) {
            return false;
        } else {
            return !(this.golem.distanceToSqr(this.owner) <= (double) (this.stopDistance * this.stopDistance));
        }
    }

    public void start() {
        this.timeToRecalcPath = 0;
    }

    public void stop() {
        this.owner = null;
        this.navigation.stop();
    }

    public void tick() {
        this.golem.getLookControl().setLookAt(this.owner, 10.0F, (float) this.golem.getMaxHeadXRot());
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            if (!this.golem.isLeashed() && !this.golem.isPassenger()) {
                if (this.golem.distanceToSqr(this.owner) >= 144.0D) {
                    this.teleportToOwner();
                } else {
                    this.navigation.moveTo(this.owner, this.speedModifier);
                }
            }
        }
    }

    private void teleportToOwner() {
        BlockPos blockpos = this.owner.blockPosition();

        for (int i = 0; i < 10; ++i) {
            int j = this.randomIntInclusive(-3, 3);
            int k = this.randomIntInclusive(-1, 1);
            int l = this.randomIntInclusive(-3, 3);
            boolean flag = this.maybeTeleportTo(blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l);
            if (flag) {
                return;
            }
        }
    }

    private boolean maybeTeleportTo(int pX, int pY, int pZ) {
        if (Math.abs((double) pX - this.owner.getX()) < 2.0D && Math.abs((double) pZ - this.owner.getZ()) < 2.0D) {
            return false;
        } else if (!this.canTeleportTo(new BlockPos(pX, pY, pZ))) {
            return false;
        } else {
            this.golem.moveTo(pX + 0.5D, pY, pZ + 0.5D, this.golem.getYRot(), this.golem.getXRot());
            this.navigation.stop();
            return true;
        }
    }

    private boolean canTeleportTo(BlockPos pPos) {
        BlockPathTypes blockpathtypes = WalkNodeEvaluator.getBlockPathTypeStatic(this.level, pPos.mutable());
        if (blockpathtypes != BlockPathTypes.WALKABLE) {
            return false;
        } else {
            BlockState blockstate = this.level.getBlockState(pPos.below());
            if (blockstate.getBlock() instanceof LeavesBlock) {
                return false;
            } else {
                BlockPos blockpos = pPos.subtract(this.golem.blockPosition());
                return this.level.noCollision(this.golem, this.golem.getBoundingBox().move(blockpos));
            }
        }
    }

    private int randomIntInclusive(int pMin, int pMax) {
        return this.golem.getRandom().nextInt(pMax - pMin + 1) + pMin;
    }
}
