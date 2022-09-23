package com.linngdu664.bsf.entity.goal;

import com.linngdu664.bsf.entity.BSFSnowGolemEntity;
import com.linngdu664.bsf.entity.BSFSnowGolemMode;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;

public class BSFGolemRandomStrollGoal extends RandomStrollGoal {
    protected final float probability;
    private final BSFSnowGolemEntity golem;

    public BSFGolemRandomStrollGoal(BSFSnowGolemEntity golem, double pSpeedModifier, float pProbability) {
        super(golem, pSpeedModifier);
        this.probability = pProbability;
        this.golem = golem;
    }

    @Override
    public boolean canUse() {
        if (golem.getMode() == BSFSnowGolemMode.ATTACK) {
            return super.canUse();
        }
        return false;
    }

    protected Vec3 getPosition() {
        if (this.mob.isInWaterOrBubble()) {
            Vec3 vec3 = LandRandomPos.getPos(this.mob, 15, 7);
            return vec3 == null ? super.getPosition() : vec3;
        } else {
            return this.mob.getRandom().nextFloat() >= this.probability ? LandRandomPos.getPos(this.mob, 10, 7) : super.getPosition();
        }
    }
}
