package com.linngdu664.bsf.util;

import com.linngdu664.bsf.entity.BSFSnowballEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static com.linngdu664.bsf.util.BSFUtil.modSqr;

public class TargetGetter {
    /**
     * This method can get the nearest available target.
     * @param snowball The snowball entity.
     * @param t The class of specific targets.
     * @param trackingRange Only gets target within the range. See AABB.inflate().
     * @param <T> Extends entity class.
     * @param angleRestriction Whether only return the target within 60 degrees.
     * @return The target.
     */
    public static <T extends Entity> Entity getTarget(BSFSnowballEntity snowball, Class<T> t, boolean angleRestriction, double trackingRange) {
        Level level = snowball.level;
        List<T> list = level.getEntitiesOfClass(t, snowball.getBoundingBox().inflate(trackingRange, trackingRange, trackingRange), (p_186450_) -> true);
        if (list.contains(snowball)) {
            list.remove(snowball);
        }
//        if (list.contains(snowball.getOwner())) {
//            list.remove(snowball.getOwner());
//        }
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

    /**
     * This method can get a target list.
     * @param entity The center entity.
     * @param t The class of specific targets.
     * @param range Only gets target within the range. See AABB.inflate().
     * @param <T> Extends entity class.
     * @return The target list.
     */
    public static <T extends Entity> List<T> getTargetList(Entity entity, Class<T> t, double range) {
        Level level = entity.level;
        List<T> list = level.getEntitiesOfClass(t, entity.getBoundingBox().inflate(range, range, range), (p_186450_) -> true);
        if (list.contains(entity)) {
            list.remove(entity);
        }
        return list;
//        if (list.contains(snowball.getOwner())) {
//            list.remove(snowball.getOwner());
//        }
        /*
        if (!list.isEmpty()) {
            return list;
        }
        return null;*/
    }
}