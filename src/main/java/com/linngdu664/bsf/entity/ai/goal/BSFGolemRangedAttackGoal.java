package com.linngdu664.bsf.entity.ai.goal;

import com.linngdu664.bsf.entity.BSFSnowGolemEntity;
import com.linngdu664.bsf.item.weapon.SnowballShotgunItem;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

import java.util.EnumSet;

public class BSFGolemRangedAttackGoal extends Goal {
    private final BSFSnowGolemEntity golem;
    private final double speedModifier;
    private final int attackIntervalMin;
    private final int attackIntervalMax;
    private final float attackRadius;
    private final float attackRadiusSqr;
    //private LivingEntity target;
    private int attackTime = -1;
    private int seeTime;
    private int strafingTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;

    public BSFGolemRangedAttackGoal(BSFSnowGolemEntity golem, double pSpeedModifier, int pAttackInterval, float pAttackRadius) {
        this.golem = golem;
        this.speedModifier = pSpeedModifier;
        this.attackIntervalMin = pAttackInterval;
        this.attackIntervalMax = pAttackInterval;
        this.attackRadius = pAttackRadius;
        this.attackRadiusSqr = pAttackRadius * pAttackRadius;
        golem.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity livingEntity = golem.getTarget();
        return livingEntity != null && livingEntity.isAlive() && golem.getStatus() != 1;
    }

    @Override
    public boolean canContinueToUse() {
        return canUse() || !golem.getNavigation().isDone();
    }

    @Override
    public void stop() {
        //target = null;
        seeTime = 0;
        attackTime = -1;
        strafingTime = 0;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
/*
    @Override
    public void tick() {
        float attackRadiusSqr = this.attackRadiusSqr;
        float attackRadius = this.attackRadius;
        if (golem.getWeapon().getItem() instanceof SnowballShotgunItem) {
            attackRadius *= 0.2;
            attackRadiusSqr *= 0.04;
        }
        double d0 = golem.distanceToSqr(target.getX(), target.getY(), target.getZ());
        boolean flag = golem.getSensing().hasLineOfSight(target);
        if (flag) {
            ++seeTime;
        } else {
            seeTime = 0;
        }

        if (!(d0 > attackRadiusSqr) && seeTime >= 5 || golem.getStatus() == 4) {
            golem.getNavigation().stop();
        } else {
            golem.getNavigation().moveTo(target, speedModifier);
        }
        golem.getLookControl().setLookAt(target, 30.0F, 30.0F);
        if (--attackTime == 0) {
            if (!flag) {
                return;
            }
            float f = (float) Math.sqrt(d0) / attackRadius;
            float f1 = Mth.clamp(f, 0.1F, 1.0F);
            golem.performRangedAttack(target, f1);
            attackTime = Mth.floor(f * (float) (attackIntervalMax - attackIntervalMin) + (float) attackIntervalMin);
        } else if (attackTime < 0) {
            attackTime = Mth.floor(Mth.lerp(Math.sqrt(d0) / attackRadius, attackIntervalMin, attackIntervalMax));
        }
    }*/

    public void tick() {
        LivingEntity target = golem.getTarget();
        float attackRadiusSqr = this.attackRadiusSqr;
        float attackRadius = this.attackRadius;
        if (golem.getWeapon().getItem() instanceof SnowballShotgunItem) {
            attackRadius *= 0.2;
            attackRadiusSqr *= 0.04;
        }
        if (target != null) {
            double d0 = golem.distanceToSqr(target.getX(), target.getY(), target.getZ());
            boolean flag = golem.getSensing().hasLineOfSight(target);
            boolean flag1 = seeTime > 0;
            if (flag != flag1) {
                seeTime = 0;
            }
            if (flag) {
                ++seeTime;
            } else {
                --seeTime;
            }
            if (d0 <= attackRadiusSqr && seeTime >= 20 || golem.getStatus() == 4) {
                golem.getNavigation().stop();
                ++strafingTime;
            } else {
                golem.getNavigation().moveTo(target, speedModifier);
                strafingTime = -1;
            }
            if (strafingTime >= 20) {
                if (golem.getRandom().nextFloat() < 0.3F) {
                    strafingClockwise = !strafingClockwise;
                }
                if (golem.getRandom().nextFloat() < 0.3F) {
                    strafingBackwards = !strafingBackwards;
                }
                strafingTime = 0;
            }
            if (strafingTime > -1 && golem.getStatus() != 4) {
                if (d0 > (double) (this.attackRadiusSqr * 0.75F)) {
                    strafingBackwards = false;
                } else if (d0 < (double) (this.attackRadiusSqr * 0.25F)) {
                    strafingBackwards = true;
                }
                golem.getMoveControl().strafe(strafingBackwards ? -0.5F : 0.5F, strafingClockwise ? 0.5F : -0.5F);
                golem.lookAt(target, 30.0F, 30.0F);
            } else {
                golem.getLookControl().setLookAt(target, 30.0F, 30.0F);
            }
            if (--attackTime == 0) {
                if (!flag) {
                    return;
                }
                float f = (float) Math.sqrt(d0) / attackRadius;
                float f1 = Mth.clamp(f, 0.1F, 1.0F);
                golem.performRangedAttack(target, f1);
                attackTime = Mth.floor(f * (float) (attackIntervalMax - attackIntervalMin) + (float) attackIntervalMin);
            } else if (attackTime < 0) {
                attackTime = Mth.floor(Mth.lerp(Math.sqrt(d0) / attackRadius, attackIntervalMin, attackIntervalMax));
            }
        }
    }
}
