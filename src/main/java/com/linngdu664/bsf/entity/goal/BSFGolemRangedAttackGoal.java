package com.linngdu664.bsf.entity.goal;

import com.linngdu664.bsf.entity.BSFSnowGolemEntity;
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

    public boolean canUse() {
        LivingEntity livingentity = this.golem.getTarget();
        if (livingentity != null && livingentity.isAlive() && golem.getStatus() != 1) {
            this.target = livingentity;
            return true;
        } else {
            return false;
        }
    }

    public boolean canContinueToUse() {
        return this.canUse() || !this.golem.getNavigation().isDone();
    }

    public void stop() {
        this.target = null;
        this.seeTime = 0;
        this.attackTime = -1;
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        assert this.target != null;
        double d0 = this.golem.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
        boolean flag = this.golem.getSensing().hasLineOfSight(this.target);
        if (flag) {
            ++this.seeTime;
        } else {
            this.seeTime = 0;
        }

        if (!(d0 > (double) this.attackRadiusSqr) && this.seeTime >= 5 || golem.getStatus() == 4) {
            this.golem.getNavigation().stop();
        } else {
            this.golem.getNavigation().moveTo(this.target, this.speedModifier);
        }
        this.golem.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        if (--this.attackTime == 0) {
            if (!flag) {
                return;
            }
            float f = (float) Math.sqrt(d0) / this.attackRadius;
            float f1 = Mth.clamp(f, 0.1F, 1.0F);
            this.golem.performRangedAttack(this.target, f1);
            this.attackTime = Mth.floor(f * (float) (this.attackIntervalMax - this.attackIntervalMin) + (float) this.attackIntervalMin);
        } else if (this.attackTime < 0) {
            this.attackTime = Mth.floor(Mth.lerp(Math.sqrt(d0) / (double) this.attackRadius, this.attackIntervalMin, this.attackIntervalMax));
        }
    }
}
