package com.linngdu664.bsf.entity.goal;

import com.linngdu664.bsf.entity.BSFSnowGolemEntity;
import com.linngdu664.bsf.item.weapon.SnowballShotgunItem;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class BSFGolemRangedAttackGoal extends Goal {
    private final BSFSnowGolemEntity golem;
    private final double speedModifier;
    private final int attackIntervalMin;
    private final int attackIntervalMax;
    private final float attackRadius;
    private final float attackRadiusSqr;
    @Nullable
    private LivingEntity target;
    private int attackTime = -1;
    private int seeTime;

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
        if (livingEntity != null && livingEntity.isAlive() && golem.getStatus() != 1) {
            target = livingEntity;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return canUse() || !golem.getNavigation().isDone();
    }

    @Override
    public void stop() {
        target = null;
        seeTime = 0;
        attackTime = -1;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        float attackRadiusSqr = this.attackRadiusSqr;
        float attackRadius = this.attackRadius;
        if (golem.getInventory().getItem(1).getItem() instanceof SnowballShotgunItem) {
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
    }
}
