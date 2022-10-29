package com.linngdu664.bsf.util;

import com.linngdu664.bsf.Main;
import com.linngdu664.bsf.network.ParticleHandler;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

public class ParticleUtil {
    /**
     * Using the parametric equation of circles to spray cone-shaped particles forward, in which the distance from plane
     * to origin is 8m and the normal vector is sightVec, and the maximum radius needs to be specified.
     *
     * @param pLevel          Entity's Level.
     * @param entity          Entity.
     * @param sightVec        The entity's sight vector.
     * @param particleOptions The type of the particle.
     * @param r               The max radius.
     * @param deg             Angle step length.
     * @param d               Radius step length.
     * @param loweredVision   The y offset of the spawning point.
     */
    public static void spawnForwardParticles(Level pLevel, Entity entity, Vec3 sightVec, ParticleOptions particleOptions, float r, float deg, float d, float loweredVision, boolean useNetwork) {//particleOptions=ParticleTypes.SNOWFLAKE r=4.5 deg=30 d=0.5f
        Vec3 vecA = sightVec.cross(new Vec3(0, 1, 0)).normalize();
        if (vecA == Vec3.ZERO) {
            vecA = sightVec.cross(new Vec3(1, 0, 0)).normalize();
        }
        Vec3 vecB = sightVec.cross(vecA).normalize();
        for (float ri = 0.5F; ri <= r; ri += d) {
            float rand = pLevel.getRandom().nextFloat() * Mth.DEG_TO_RAD * deg;
            for (float theta = rand; theta < Mth.TWO_PI + rand; theta += Mth.DEG_TO_RAD * deg) {
                double x = 8.0F * sightVec.x + ri * (Mth.cos(theta) * vecA.x + Mth.sin(theta) * vecB.x);
                double y = 8.0F * sightVec.y + ri * (Mth.cos(theta) * vecA.y + Mth.sin(theta) * vecB.y);
                double z = 8.0F * sightVec.z + ri * (Mth.cos(theta) * vecA.z + Mth.sin(theta) * vecB.z);
                double inverseL = Mth.fastInvSqrt(BSFMthUtil.modSqr(x, y, z));
                double rand1 = Math.sqrt(pLevel.getRandom().nextDouble() * 0.9 + 0.1);
                if (useNetwork) {
                    Main.PACKET_HANDLER.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new ParticleHandler(entity.getX(), entity.getEyeY() - loweredVision, entity.getZ(), x * inverseL * rand1, y * inverseL * rand1, z * inverseL * rand1));
                } else {
                    pLevel.addParticle(particleOptions, entity.getX(), entity.getEyeY() - loweredVision, entity.getZ(), x * inverseL * rand1, y * inverseL * rand1, z * inverseL * rand1);
                }
            }
        }
    }
}
