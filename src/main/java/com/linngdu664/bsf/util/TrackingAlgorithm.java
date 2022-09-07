package com.linngdu664.bsf.util;

import com.linngdu664.bsf.entity.AdvancedSnowballEntity;
import com.linngdu664.bsf.entity.BSFSnowballEntity;
import com.linngdu664.bsf.entity.snowball.tracking_snowball.ForceSnowballEntity;
import com.linngdu664.bsf.entity.snowball.tracking_snowball.MissileSnowballEntity;
import com.linngdu664.bsf.util.BSFUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static com.linngdu664.bsf.util.BSFUtil.modSqr;

public class TrackingAlgorithm {
    private static <T extends Entity> Entity getTarget(BSFSnowballEntity snowball, Class<T> t, boolean angleRestriction, double trackingRange) {
        Level level = snowball.level;
        List<T> list = level.getEntitiesOfClass(t, snowball.getBoundingBox().inflate(trackingRange, trackingRange, trackingRange), (p_186450_) -> true);
        if (t == Projectile.class) {
            list.remove(snowball);
        } else if (t == Player.class) {
            list.remove(snowball.getOwner());
        }
        if (list.isEmpty()) {
            return null;
        }
        Entity entity1 = list.get(0);
        for (T entity : list) {
            if (snowball.distanceToSqr(entity) < snowball.distanceToSqr(entity1)) {
                entity1 = entity;
            }
        }
        if (angleRestriction) {
            Vec3 vec3 = new Vec3(entity1.getX() - snowball.getX(), entity1.getY() - snowball.getY(), entity1.getZ() - snowball.getZ());
            Vec3 velocity = snowball.getDeltaMovement();
            if (BSFUtil.vec3AngleCos(vec3, velocity) < 0.5 || modSqr(vec3) > trackingRange * trackingRange) {
                return null;
            }
        }
        return entity1;
    }

    private static <T extends Entity> List<T> getTargetList(BSFSnowballEntity snowball, Class<T> t, double trackingRange) {
        Level level = snowball.level;
        List<T> list = level.getEntitiesOfClass(t, snowball.getBoundingBox().inflate(trackingRange, trackingRange, trackingRange), (p_186450_) -> true);
        if (t == Projectile.class) {
            list.remove(snowball);
        } else if (t == Player.class) {
            list.remove(snowball.getOwner());
        }
        if (!list.isEmpty()) {
            return list;
        }
        return null;
    }

    //This is not used for tracking in fact, but it has some funny effects.
    public static <T extends Entity> void gravityTracking(BSFSnowballEntity snowball, Class<T> targetClass, double trackingRange, double GM, boolean angleRestriction, boolean trackingMultipleTargets, boolean selfAttraction, boolean attraction) {
        if (trackingMultipleTargets) {
            List<T> list = getTargetList(snowball, targetClass, trackingRange);
            if (list != null && !list.isEmpty()) {
                for (T entity : list) {
                    if (angleRestriction) {
                        Vec3 vec3 = new Vec3(entity.getX() - snowball.getX(), entity.getY() - snowball.getY(), entity.getZ() - snowball.getZ());
                        Vec3 velocity = snowball.getDeltaMovement();
                        if (BSFUtil.vec3AngleCos(vec3, velocity) < 0.5 || modSqr(vec3) > trackingRange * trackingRange) {
                            continue;
                        }
                    }
                    Vec3 rVec = new Vec3(entity.getX() - snowball.getX(), entity.getEyeY() - snowball.getY(), entity.getZ() - snowball.getZ());
                    double r2 = rVec.x * rVec.x + rVec.y + rVec.y + rVec.z * rVec.z;
                    Vec3 aVec;
                    if (r2 > 0.01) {
                        double ir2 = Mth.fastInvSqrt(r2);
                        double a = GM / r2;
                        aVec = new Vec3(a * rVec.x * ir2, a * rVec.y * ir2, a * rVec.z * ir2);
                    } else {
                        aVec = Vec3.ZERO;
                    }
                    if (selfAttraction) {
                        Vec3 vVec = snowball.getDeltaMovement();
                        snowball.lerpMotion(vVec.x + aVec.x, vVec.y + aVec.y, vVec.z + aVec.z);
                    }
                    if (attraction) {
                        Vec3 vVec2 = entity.getDeltaMovement();
                        entity.lerpMotion(vVec2.x - aVec.x, vVec2.y - aVec.y, vVec2.z - aVec.z);
                    }
                }
            }
        } else {
            Entity target = getTarget(snowball, targetClass, angleRestriction, trackingRange);
            if (target != null) {
                Vec3 rVec = new Vec3(target.getX() - snowball.getX(), target.getEyeY() - snowball.getY(), target.getZ() - snowball.getZ());
                double r2 = rVec.x * rVec.x + rVec.y + rVec.y + rVec.z * rVec.z;
                Vec3 aVec;
                if (r2 > 0.01) {
                    double ir2 = Mth.fastInvSqrt(r2);
                    double a = GM / r2;
                    aVec = new Vec3(a * rVec.x * ir2, a * rVec.y * ir2, a * rVec.z * ir2);
                } else {
                    aVec = Vec3.ZERO;
                }
                if (selfAttraction) {
                    Vec3 vVec = snowball.getDeltaMovement();
                    snowball.lerpMotion(vVec.x + aVec.x, vVec.y + aVec.y, vVec.z + aVec.z);
                }
                if (attraction) {
                    Vec3 vVec2 = target.getDeltaMovement();
                    target.lerpMotion(vVec2.x - aVec.x, vVec2.y - aVec.y, vVec2.z - aVec.z);
                }
            }
        }
    }

    public static <T extends Entity> void missilesTracking(BSFSnowballEntity snowball, Class<T> targetClass, double trackingRange, boolean angleRestriction, double maxTurningAngleCos, double maxTurningAngleSin) {
        Level level = snowball.level;
        Entity target = getTarget(snowball, targetClass, angleRestriction, trackingRange);
        Vec3 velocity = snowball.getDeltaMovement();
        if (target == null || !target.isAlive() || modSqr(velocity) < 0.25) {
            snowball.setNoGravity(false);
        } else if (!level.isClientSide) {
            snowball.setNoGravity(true);
            Vec3 delta = new Vec3(target.getX() - snowball.getX(), target.getEyeY() - snowball.getY(), target.getZ() - snowball.getZ());
            double cosTheta = BSFUtil.vec2AngleCos(delta.x, delta.z, velocity.x, velocity.z);
            double sinTheta;
            if (cosTheta < maxTurningAngleCos) {
                cosTheta = maxTurningAngleCos;
                sinTheta = maxTurningAngleSin;
            } else {
                sinTheta = Math.sqrt(1 - cosTheta * cosTheta);
            }
            double vx, vz, vy;
            double d0 = velocity.x * cosTheta - velocity.z * sinTheta;
            double d1 = velocity.x * sinTheta + velocity.z * cosTheta;
            double d2 = velocity.x * cosTheta + velocity.z * sinTheta;
            double d3 = -velocity.x * sinTheta + velocity.z * cosTheta;
            if (d0 * delta.x + d1 * delta.z > d2 * delta.x + d3 * delta.z) {
                vx = d0;
                vz = d1;
            } else {
                vx = d2;
                vz = d3;
            }
            double vNewX = Math.sqrt(vx * vx + vz * vz);
            double deltaNewX = Math.sqrt(delta.x * delta.x + delta.z * delta.z);
            cosTheta = BSFUtil.vec2AngleCos(vNewX, velocity.y, deltaNewX, delta.y);
            if (cosTheta < maxTurningAngleCos) {
                cosTheta = maxTurningAngleCos;
                sinTheta = maxTurningAngleSin;
            } else {
                sinTheta = Math.sqrt(1 - cosTheta * cosTheta);
            }
            d0 = vNewX * cosTheta - velocity.y * sinTheta;
            d1 = vNewX * sinTheta + velocity.y * cosTheta;
            d2 = vNewX * cosTheta + velocity.y * sinTheta;
            d3 = -vNewX * sinTheta + velocity.y * cosTheta;
            double adjusted;
            if (d0 * deltaNewX + d1 * delta.y > d2 * deltaNewX + d3 * delta.y) {
                adjusted = d0;
                vy = d1;
            } else {
                adjusted = d2;
                vy = d3;
            }
            vx *= adjusted / vNewX;
            vz *= adjusted / vNewX;
            snowball.setDeltaMovement(vx, vy, vz);
        }
    }
}
