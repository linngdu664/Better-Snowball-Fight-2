package com.linngdu664.bsf.util;

import com.linngdu664.bsf.entity.AbstractBSFSnowballEntity;
import com.linngdu664.bsf.entity.BSFSnowGolemEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.UUID;

// I wonder why IDEA warns list.remove.
@SuppressWarnings("all")
public class TargetGetter {
    /**
     * Get the nearest available target.
     *
     * @param snowball         The snowball entity.
     * @param t                The class of specific targets.
     * @param trackingRange    Only gets target within the range. See AABB.inflate().
     * @param <T>              Extends entity class.
     * @param angleRestriction Whether only return the target within 60 degrees.
     * @return The target.
     */
    public static <T extends Entity> Entity getTarget(AbstractBSFSnowballEntity snowball, Class<T> t, boolean angleRestriction, double trackingRange) {
        Level level = snowball.level;
        List<T> list = level.getEntitiesOfClass(t, snowball.getBoundingBox().inflate(trackingRange, trackingRange, trackingRange), (p_186450_) -> true);
        if (t == BSFSnowGolemEntity.class) {
            for (T entity : list) {
                if (((BSFSnowGolemEntity) entity).getOwner().equals(snowball.getOwner())) {
                    list.remove(entity);
                }
            }
        }
        list.remove(snowball);
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
            if (BSFMthUtil.vec3AngleCos(vec3, velocity) < 0.5 || vec3.lengthSqr() > trackingRange * trackingRange) {
                return null;
            }
        }
        return entity1;
    }

    /**
     * Get a list that contains nearby targets.
     *
     * @param entity The center entity.
     * @param t      The class of specific targets.
     * @param range  Only gets target within the range. See AABB.inflate().
     * @param <T>    Extends entity class.
     * @return The target list.
     */
    public static <T extends Entity> List<T> getTargetList(Entity entity, Class<T> t, double range) {
        Level level = entity.getLevel();
        List<T> list = level.getEntitiesOfClass(t, entity.getBoundingBox().inflate(range, range, range), (p_186450_) -> true);
        list.remove(entity);
        return list;
    }

    public static LivingEntity getLivingEntityByUUID(Entity entity, UUID uuid, double range) {
        List<Mob> list = getTargetList(entity, Mob.class, range);
        for (Mob mob : list) {
            if (mob.getUUID().equals(uuid)) {
                return mob;
            }
        }
        return entity.getLevel().getPlayerByUUID(uuid);
    }
}
