package com.linngdu664.bsf.util;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ParticleUtil {
    /**
     * Forward spraying cone-shaped particles
     * @param pLevel
     * @param pPlayer
     * @param cameraVec
     * @param particleOptions
     * @param r radius
     * @param deg angle
     * @param d angle step length
     * @param loweredVision
     */
    public static void spawnForwardParticles(Level pLevel, Entity pPlayer, Vec3 cameraVec, ParticleOptions particleOptions, float r, float deg, float d, float loweredVision) {//particleOptions=ParticleTypes.SNOWFLAKE r=4.5 deg=30 d=0.5f
        if (pLevel.isClientSide) {
            Vec3 vecA = cameraVec.cross(new Vec3(0, 1, 0)).normalize();
            if (vecA == Vec3.ZERO) {
                vecA = cameraVec.cross(new Vec3(1, 0, 0)).normalize();
            }
            Vec3 vecB = cameraVec.cross(vecA).normalize();
            for (float ri = 0.5F; ri <= r; ri += d) {
                float rand = pLevel.getRandom().nextFloat() * Mth.DEG_TO_RAD * deg;
                for (float theta = rand; theta < Mth.TWO_PI + rand; theta += Mth.DEG_TO_RAD * deg) {
                    double x = 8.0F * cameraVec.x + ri * (Mth.cos(theta) * vecA.x + Mth.sin(theta) * vecB.x);
                    double y = 8.0F * cameraVec.y + ri * (Mth.cos(theta) * vecA.y + Mth.sin(theta) * vecB.y);
                    double z = 8.0F * cameraVec.z + ri * (Mth.cos(theta) * vecA.z + Mth.sin(theta) * vecB.z);
                    double inverseL = Mth.fastInvSqrt(BSFMthUtil.modSqr(x, y, z));
                    double rand1 = Math.sqrt(pLevel.getRandom().nextDouble() * 0.9 + 0.1);
                    pLevel.addParticle(particleOptions, pPlayer.getX(), pPlayer.getEyeY() - loweredVision, pPlayer.getZ(), x * inverseL * rand1, y * inverseL * rand1, z * inverseL * rand1);
                }
            }
        }
    }
}
