package com.linngdu664.bsf.util;

import com.linngdu664.bsf.entity.BSFSnowballEntity;
import com.linngdu664.bsf.item.setter.ItemRegister;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class BSFUtil {
    public static boolean isHeadingToSnowball(Player player, BSFSnowballEntity snowballEntity) {
        float pitch = player.getXRot() * Mth.DEG_TO_RAD;
        float yaw = player.getYRot() * Mth.DEG_TO_RAD;
        Vec3 speedVec = snowballEntity.getDeltaMovement().normalize();
        Vec3 cameraVec = new Vec3(-Mth.cos(pitch) * Mth.sin(yaw), -Mth.sin(pitch), Mth.cos(pitch) * Mth.cos(yaw));
        return Mth.abs((float) (cameraVec.dot(speedVec) + 1.0F)) < 0.2F;
    }

    public static boolean isAmmoTank(Item stack, boolean allowTracking) {
        return stack == ItemRegister.COMPACTED_SNOWBALL_STORAGE_TANK.get() || stack == ItemRegister.EXPLOSIVE_SNOWBALL_STORAGE_TANK.get() ||
                stack == ItemRegister.GLASS_SNOWBALL_STORAGE_TANK.get() || stack == ItemRegister.GOLD_SNOWBALL_STORAGE_TANK.get() ||
                stack == ItemRegister.ICE_SNOWBALL_STORAGE_TANK.get() || stack == ItemRegister.IRON_SNOWBALL_STORAGE_TANK.get() ||
                stack == ItemRegister.OBSIDIAN_SNOWBALL_STORAGE_TANK.get() || stack == ItemRegister.STONE_SNOWBALL_STORAGE_TANK.get() ||
                stack == ItemRegister.SPECTRAL_SNOWBALL_STORAGE_TANK.get() || (stack == ItemRegister.LIGHT_PLAYER_TRACKING_SNOWBALL_STORAGE_TANK.get() ||
                stack == ItemRegister.HEAVY_PLAYER_TRACKING_SNOWBALL_STORAGE_TANK.get() || stack == ItemRegister.EXPLOSIVE_PLAYER_TRACKING_SNOWBALL_STORAGE_TANK.get() ||
                stack == ItemRegister.LIGHT_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get() || stack == ItemRegister.HEAVY_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get() ||
                stack == ItemRegister.EXPLOSIVE_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get()) && allowTracking;
    }

    public static boolean isAmmo(Item stack) {
        return stack == ItemRegister.COMPACTED_SNOWBALL.get() || stack == ItemRegister.EXPLOSIVE_SNOWBALL.get() ||
                stack == ItemRegister.GLASS_SNOWBALL.get() || stack == ItemRegister.GOLD_SNOWBALL.get() ||
                stack == ItemRegister.ICE_SNOWBALL.get() || stack == ItemRegister.IRON_SNOWBALL.get() ||
                stack == ItemRegister.OBSIDIAN_SNOWBALL.get() || stack == ItemRegister.STONE_SNOWBALL.get() ||
                stack == ItemRegister.SPECTRAL_SNOWBALL.get() || stack == ItemRegister.LIGHT_PLAYER_TRACKING_SNOWBALL.get() ||
                stack == ItemRegister.HEAVY_PLAYER_TRACKING_SNOWBALL.get() || stack == ItemRegister.EXPLOSIVE_PLAYER_TRACKING_SNOWBALL.get() ||
                stack == ItemRegister.LIGHT_MONSTER_TRACKING_SNOWBALL.get() || stack == ItemRegister.HEAVY_MONSTER_TRACKING_SNOWBALL.get() ||
                stack == ItemRegister.EXPLOSIVE_MONSTER_TRACKING_SNOWBALL.get();
    }
    public static boolean isAmmoTank2(Item stack, boolean allowTracking) {
        return stack == ItemRegister.COMPACTED_SNOWBALL_STORAGE_TANK.get() || stack == ItemRegister.EXPLOSIVE_SNOWBALL_STORAGE_TANK.get() ||
                stack == ItemRegister.GLASS_SNOWBALL_STORAGE_TANK.get() || stack == ItemRegister.GOLD_SNOWBALL_STORAGE_TANK.get() ||
                stack == ItemRegister.ICE_SNOWBALL_STORAGE_TANK.get() || stack == ItemRegister.IRON_SNOWBALL_STORAGE_TANK.get() ||
                stack == ItemRegister.OBSIDIAN_SNOWBALL_STORAGE_TANK.get() || stack == ItemRegister.STONE_SNOWBALL_STORAGE_TANK.get() ||
                stack == ItemRegister.SPECTRAL_SNOWBALL_STORAGE_TANK.get() && allowTracking;
    }

//    public static boolean isAmmo2(Item stack) {
//        return stack == ItemRegister.COMPACTED_SNOWBALL.get() || stack == ItemRegister.EXPLOSIVE_SNOWBALL.get() ||
//                stack == ItemRegister.GLASS_SNOWBALL.get() || stack == ItemRegister.GOLD_SNOWBALL.get() ||
//                stack == ItemRegister.ICE_SNOWBALL.get() || stack == ItemRegister.IRON_SNOWBALL.get() ||
//                stack == ItemRegister.OBSIDIAN_SNOWBALL.get() || stack == ItemRegister.STONE_SNOWBALL.get() ||
//                stack == ItemRegister.SPECTRAL_SNOWBALL.get();
//    }

    public static ItemStack findAmmo(Player player, boolean onlyTank) {
        if(onlyTank){
            for (int j = 0; j < player.getInventory().getContainerSize(); j++) {
                if (isAmmoTank2(player.getInventory().getItem(j).getItem(), false)) {
                    return player.getInventory().getItem(j);
                }
            }
        }else{
            for (int j = 0; j < player.getInventory().getContainerSize(); j++) {
                if (isAmmoTank(player.getInventory().getItem(j).getItem(), true)) {
                    return player.getInventory().getItem(j);
                }
            }
            for (int j = 0; j < player.getInventory().getContainerSize(); j++) {
                if (isAmmo(player.getInventory().getItem(j).getItem())) {
                    return player.getInventory().getItem(j);
                }
            }
        }
        return null;
    }

    //Calculate the cosine of the angle between 2 2D vectors. High school math.
    public static double vec2AngleCos(double x1, double y1, double x2, double y2) {
        return Mth.fastInvSqrt(modSqr(x1, y1)) * Mth.fastInvSqrt(modSqr(x2, y2)) * (x1 * x2 + y1 * y2);
    }

    public static double vec3AngleCos(Vec3 a, Vec3 b) {
        return Mth.fastInvSqrt(modSqr(a)) * Mth.fastInvSqrt(modSqr(b)) * (a.x * b.x + a.y * b.y + a.z * b.z);
    }

    public static double modSqr(double x1, double y1) {
        return x1 * x1 + y1 * y1;
    }

    public static double modSqr(Vec3 a) {
        return a.x * a.x + a.y * a.y + a.z * a.z;
    }

    //Rewrite vanilla "shootFromRotation" method to remove the influence of player's velocity.
    public static void shootFromRotation(Projectile projectile, float pX, float pY, float pZ, float pVelocity, float pInaccuracy) {
        float f = -Mth.sin(pY * ((float)Math.PI / 180F)) * Mth.cos(pX * ((float)Math.PI / 180F));
        float f1 = -Mth.sin((pX + pZ) * ((float)Math.PI / 180F));
        float f2 = Mth.cos(pY * ((float)Math.PI / 180F)) * Mth.cos(pX * ((float)Math.PI / 180F));
        projectile.shoot(f, f1, f2, pVelocity, pInaccuracy);
    }
}
