package com.linngdu664.bsf.util;

import com.linngdu664.bsf.entity.BSFSnowballEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static com.linngdu664.bsf.util.BSFUtil.modSqr;
import static com.linngdu664.bsf.util.BSFUtil.vec2AngleCos;

public class MovingAlgorithm {
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
        if (list.contains(snowball)) {
            list.remove(snowball);
        }
        if (list.contains(snowball.getOwner())) {
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
                    if (r2 > 0.1) {
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
                if (r2 > 0.1) {
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

    /**
     * This method uses the "inverse square-const-zero" model to simulate gravity/repulsion. If we only use inverse-square
     * law, the dt (0.05s) is too long, resulting in huge velocity errors when the distance is short (acceleration is huge),
     * so we limit the acceleration and even force it to be 0 when the distance is shorter than 0.5m to avoid abnormal
     * movements or game crashes.
     * @param snowball The snowball entity.
     * @param targetClass The class of specific targets.
     * @param range Only calculate the velocity of targets within the range.
     * @param GM The magnitude of force has direct ratio with this param.
     * @param boundaryR2 If the square of distance is smaller than this param, the force will be a const and will not follow an inverse-square law.
     * @param <T> Extends entity class
     */
    public static <T extends Entity> void forceEffect(BSFSnowballEntity snowball, Class<T> targetClass, double range, double GM, double boundaryR2) {
        List<T> list = getTargetList(snowball, targetClass, range);
        if (list != null && !list.isEmpty()) {
            for (T entity : list) {
                Vec3 rVec = new Vec3(snowball.getX() - entity.getX(), snowball.getY() - entity.getEyeY(), snowball.getZ() - entity.getZ());
                double r2 = modSqr(rVec);
                double ir2 = Mth.fastInvSqrt(r2);
                double a;
                if (r2 > boundaryR2) {
                    a = GM / r2;
                } else if (r2 > 0.25) {
                    a = GM / boundaryR2;
                } else {
                    a = 0;
                }
                entity.push(a * rVec.x * ir2, a * rVec.y * ir2, a * rVec.z * ir2);
            }
        }
    }

    public static <T extends Entity> void missilesTracking(BSFSnowballEntity snowball, Class<T> targetClass, double trackingRange, boolean angleRestriction, double maxTurningAngleCos, double maxTurningAngleSin, boolean lockFeet) {
        Level level = snowball.level;
        Entity target = getTarget(snowball, targetClass, angleRestriction, trackingRange);
        Vec3 velocity = snowball.getDeltaMovement();
        if (target == null || !target.isAlive() || modSqr(velocity) < 0.25) {
            snowball.setNoGravity(false);
        } else if (!level.isClientSide) {
            snowball.setNoGravity(true);
            Vec3 delta;
            if (lockFeet) {
                delta = new Vec3(target.getX() - snowball.getX(), target.getY() - snowball.getY(), target.getZ() - snowball.getZ());
            } else {
                delta = new Vec3(target.getX() - snowball.getX(), target.getEyeY() - snowball.getY(), target.getZ() - snowball.getZ());
            }
            double cosTheta = vec2AngleCos(delta.x, delta.z, velocity.x, velocity.z);
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
            cosTheta = vec2AngleCos(vNewX, velocity.y, deltaNewX, delta.y);
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
            //Do not directly use "setDeltaMovement" because it will cause the lagging of texture.
            //Use "push" may avoid this.
            snowball.push(vx - velocity.x, vy - velocity.y, vz - velocity.z);
        }
    }
}
