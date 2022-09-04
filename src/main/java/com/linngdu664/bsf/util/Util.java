package com.linngdu664.bsf.util;

import com.linngdu664.bsf.entity.AdvancedSnowballEntity;
import com.linngdu664.bsf.item.setter.ItemRegister;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class Util {
    public static boolean isHeadingToSnowball(Player player, AdvancedSnowballEntity snowballEntity) {
        float pitch = player.getXRot() * 0.01745329F;
        float yaw = player.getYRot() * 0.01745329F;
        Vec3 speedVec = snowballEntity.getDeltaMovement().normalize();
        Vec3 cameraVec = new Vec3(-Mth.cos(pitch) * Mth.sin(yaw), -Mth.sin(pitch), Mth.cos(pitch) * Mth.cos(yaw));
        return Mth.abs((float) (cameraVec.dot(speedVec) + 1.0F)) < 0.2F;
    }

    public static boolean isAmmoTank(ItemStack stack, boolean allowTracking) {
        return stack.getItem() == ItemRegister.COMPACTED_SNOWBALL_STORAGE_TANK.get() || stack.getItem() == ItemRegister.EXPLOSIVE_SNOWBALL_STORAGE_TANK.get() ||
                stack.getItem() == ItemRegister.GLASS_SNOWBALL_STORAGE_TANK.get() || stack.getItem() == ItemRegister.GOLD_SNOWBALL_STORAGE_TANK.get() ||
                stack.getItem() == ItemRegister.ICE_SNOWBALL_STORAGE_TANK.get() || stack.getItem() == ItemRegister.IRON_SNOWBALL_STORAGE_TANK.get() ||
                stack.getItem() == ItemRegister.OBSIDIAN_SNOWBALL_STORAGE_TANK.get() || stack.getItem() == ItemRegister.STONE_SNOWBALL_STORAGE_TANK.get() ||
                stack.getItem() == ItemRegister.SPECTRAL_SNOWBALL_STORAGE_TANK.get() || (stack.getItem() == ItemRegister.PLAYER_TRACKING_SNOWBALL_STORAGE_TANK.get() ||
                stack.getItem() == ItemRegister.PLAYER_TRACKING_SNOWBALL_WITH_DAMAGE_STORAGE_TANK.get() || stack.getItem() == ItemRegister.EXPLOSIVE_PLAYER_TRACKING_SNOWBALL_STORAGE_TANK.get() ||
                stack.getItem() == ItemRegister.MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get() || stack.getItem() == ItemRegister.MONSTER_TRACKING_SNOWBALL_WITH_DAMAGE_STORAGE_TANK.get() ||
                stack.getItem() == ItemRegister.EXPLOSIVE_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get()) && allowTracking;
    }

    public static boolean isAmmo(ItemStack stack) {
        return stack.getItem() == ItemRegister.COMPACTED_SNOWBALL.get() || stack.getItem() == ItemRegister.EXPLOSIVE_SNOWBALL.get() ||
                stack.getItem() == ItemRegister.GLASS_SNOWBALL.get() || stack.getItem() == ItemRegister.GOLD_SNOWBALL.get() ||
                stack.getItem() == ItemRegister.ICE_SNOWBALL.get() || stack.getItem() == ItemRegister.IRON_SNOWBALL.get() ||
                stack.getItem() == ItemRegister.OBSIDIAN_SNOWBALL.get() || stack.getItem() == ItemRegister.STONE_SNOWBALL.get() ||
                stack.getItem() == ItemRegister.SPECTRAL_SNOWBALL.get() || stack.getItem() == ItemRegister.PLAYER_TRACKING_SNOWBALL.get() ||
                stack.getItem() == ItemRegister.PLAYER_TRACKING_SNOWBALL_WITH_DAMAGE.get() || stack.getItem() == ItemRegister.EXPLOSIVE_PLAYER_TRACKING_SNOWBALL.get() ||
                stack.getItem() == ItemRegister.MONSTER_TRACKING_SNOWBALL.get() || stack.getItem() == ItemRegister.MONSTER_TRACKING_SNOWBALL_WITH_DAMAGE.get() ||
                stack.getItem() == ItemRegister.EXPLOSIVE_MONSTER_TRACKING_SNOWBALL.get();
    }

    public static ItemStack findAmmo(Player player, boolean onlyTank) {
        for (int j = 0; j < player.getInventory().getContainerSize(); j++) {
            if (isAmmoTank(player.getInventory().getItem(j), !onlyTank)) {
                return player.getInventory().getItem(j);
            }
        }
        if (!onlyTank) {
            for (int j = 0; j < player.getInventory().getContainerSize(); j++) {
                if (isAmmo(player.getInventory().getItem(j))) {
                    return player.getInventory().getItem(j);
                }
            }
        }
        return null;
    }

    public static double vec2AngleCos(double x1, double y1, double x2, double y2) {
        return Mth.fastInvSqrt(x1 * x1 + y1 * y1) * Mth.fastInvSqrt(x2 * x2 + y2 * y2) * (x1 * x2 + y1 * y2);
    }

    public static void shootFromRotation(Projectile projectile, float pX, float pY, float pZ, float pVelocity, float pInaccuracy) {
        float f = -Mth.sin(pY * ((float)Math.PI / 180F)) * Mth.cos(pX * ((float)Math.PI / 180F));
        float f1 = -Mth.sin((pX + pZ) * ((float)Math.PI / 180F));
        float f2 = Mth.cos(pY * ((float)Math.PI / 180F)) * Mth.cos(pX * ((float)Math.PI / 180F));
        projectile.shoot((double)f, (double)f1, (double)f2, pVelocity, pInaccuracy);
    }
}
