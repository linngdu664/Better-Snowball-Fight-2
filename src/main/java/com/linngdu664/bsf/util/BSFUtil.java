package com.linngdu664.bsf.util;

import com.linngdu664.bsf.entity.BSFSnowballEntity;
import com.linngdu664.bsf.item.ItemRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

//You will see the deepest secrets of our code that we use this class to hide our shits.
public class BSFUtil {
    public static boolean isAmmoTank(Item item, boolean allowTracking) {
        return item == ItemRegister.COMPACTED_SNOWBALL_STORAGE_TANK.get() || item == ItemRegister.EXPLOSIVE_SNOWBALL_STORAGE_TANK.get() ||
                item == ItemRegister.GLASS_SNOWBALL_STORAGE_TANK.get() || item == ItemRegister.GOLD_SNOWBALL_STORAGE_TANK.get() ||
                item == ItemRegister.ICE_SNOWBALL_STORAGE_TANK.get() || item == ItemRegister.IRON_SNOWBALL_STORAGE_TANK.get() ||
                item == ItemRegister.OBSIDIAN_SNOWBALL_STORAGE_TANK.get() || item == ItemRegister.STONE_SNOWBALL_STORAGE_TANK.get() ||
                item == ItemRegister.SPECTRAL_SNOWBALL_STORAGE_TANK.get() || (item == ItemRegister.LIGHT_PLAYER_TRACKING_SNOWBALL_STORAGE_TANK.get() ||
                item == ItemRegister.HEAVY_PLAYER_TRACKING_SNOWBALL_STORAGE_TANK.get() || item == ItemRegister.EXPLOSIVE_PLAYER_TRACKING_SNOWBALL_STORAGE_TANK.get() ||
                item == ItemRegister.LIGHT_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get() || item == ItemRegister.HEAVY_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get() ||
                item == ItemRegister.EXPLOSIVE_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get()) && allowTracking;
    }

    public static boolean isAmmo(Item item, boolean allowTracking) {
        return item == ItemRegister.COMPACTED_SNOWBALL.get() || item == ItemRegister.EXPLOSIVE_SNOWBALL.get() ||
                item == ItemRegister.GLASS_SNOWBALL.get() || item == ItemRegister.GOLD_SNOWBALL.get() ||
                item == ItemRegister.ICE_SNOWBALL.get() || item == ItemRegister.IRON_SNOWBALL.get() ||
                item == ItemRegister.OBSIDIAN_SNOWBALL.get() || item == ItemRegister.STONE_SNOWBALL.get() ||
                item == ItemRegister.SPECTRAL_SNOWBALL.get() || (item == ItemRegister.LIGHT_PLAYER_TRACKING_SNOWBALL.get() ||
                item == ItemRegister.HEAVY_PLAYER_TRACKING_SNOWBALL.get() || item == ItemRegister.EXPLOSIVE_PLAYER_TRACKING_SNOWBALL.get() ||
                item == ItemRegister.LIGHT_MONSTER_TRACKING_SNOWBALL.get() || item == ItemRegister.HEAVY_MONSTER_TRACKING_SNOWBALL.get() ||
                item == ItemRegister.EXPLOSIVE_MONSTER_TRACKING_SNOWBALL.get()) && allowTracking;
    }
    /*
    public static boolean isAmmoTank2(Item stack, boolean allowTracking) {
        return stack == ItemRegister.COMPACTED_SNOWBALL_STORAGE_TANK.get() || stack == ItemRegister.EXPLOSIVE_SNOWBALL_STORAGE_TANK.get() ||
                stack == ItemRegister.GLASS_SNOWBALL_STORAGE_TANK.get() || stack == ItemRegister.GOLD_SNOWBALL_STORAGE_TANK.get() ||
                stack == ItemRegister.ICE_SNOWBALL_STORAGE_TANK.get() || stack == ItemRegister.IRON_SNOWBALL_STORAGE_TANK.get() ||
                stack == ItemRegister.OBSIDIAN_SNOWBALL_STORAGE_TANK.get() || stack == ItemRegister.STONE_SNOWBALL_STORAGE_TANK.get() ||
                stack == ItemRegister.SPECTRAL_SNOWBALL_STORAGE_TANK.get() && allowTracking;
    }*/

//    public static boolean isAmmo2(Item stack) {
//        return stack == ItemRegister.COMPACTED_SNOWBALL.get() || stack == ItemRegister.EXPLOSIVE_SNOWBALL.get() ||
//                stack == ItemRegister.GLASS_SNOWBALL.get() || stack == ItemRegister.GOLD_SNOWBALL.get() ||
//                stack == ItemRegister.ICE_SNOWBALL.get() || stack == ItemRegister.IRON_SNOWBALL.get() ||
//                stack == ItemRegister.OBSIDIAN_SNOWBALL.get() || stack == ItemRegister.STONE_SNOWBALL.get() ||
//                stack == ItemRegister.SPECTRAL_SNOWBALL.get();
//    }

    /**
     * This method is used to handle the storage of the snowballs.
     * @param pPlayer The player who uses snowball.
     * @param itemStack The snowball itemstack.
     * @param tank The storage tank with the specific type.
     * @return If the method stores snowballs in the tank successfully, it will return true, else return false.
     */
    public static boolean storageInTank(Player pPlayer, ItemStack itemStack, Item tank) {
        if (pPlayer.getOffhandItem().getItem() == ItemRegister.EMPTY_SNOWBALL_STORAGE_TANK.get()) {
            pPlayer.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(tank));
            pPlayer.getOffhandItem().setDamageValue(96 - pPlayer.getMainHandItem().getCount());
            if (!pPlayer.getAbilities().instabuild) {
                itemStack.shrink(pPlayer.getMainHandItem().getCount());
            }
            return true;
        }
        if (pPlayer.getOffhandItem().getItem() == tank && pPlayer.getOffhandItem().getDamageValue() != 0) {
            if (pPlayer.getOffhandItem().getDamageValue() >= pPlayer.getMainHandItem().getCount()) {
                pPlayer.getOffhandItem().setDamageValue(pPlayer.getOffhandItem().getDamageValue() - pPlayer.getMainHandItem().getCount());
                if (!pPlayer.getAbilities().instabuild) {
                    itemStack.shrink(pPlayer.getMainHandItem().getCount());
                }
            } else {
                if (!pPlayer.getAbilities().instabuild) {
                    itemStack.shrink(pPlayer.getOffhandItem().getDamageValue());
                }
                pPlayer.getOffhandItem().setDamageValue(0);
            }
            return true;
        }
        return false;
    }

    /**
     * This method is to find the ammo of the weapon in player's inventory. It will search tanks first, and then it will
     * search bulk snowballs if "onlyTank" is false.
     * @param player The user of the weapon.
     * @param onlyTank Whether the weapon can only use the snowball in tanks.
     * @param allowTracking Whether the weapon can shoot tracking snowball.
     * @return The first valid ammo itemstack. If the method can't find a proper itemstack, it will return null.
     */
    public static ItemStack findAmmo(Player player, boolean onlyTank, boolean allowTracking) {
        /*if (onlyTank) {
            for (int j = 0; j < player.getInventory().getContainerSize(); j++) {
                if (isAmmoTank(player.getInventory().getItem(j).getItem(), allowTracking)) {
                    return player.getInventory().getItem(j);
                }
            }
        } else {
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
        return null;*/

        for (int j = 0; j < player.getInventory().getContainerSize(); j++) {
            if (isAmmoTank(player.getInventory().getItem(j).getItem(), allowTracking)) {
                return player.getInventory().getItem(j);
            }
        }
        if (!onlyTank) {
            for (int j = 0; j < player.getInventory().getContainerSize(); j++) {
                if (isAmmo(player.getInventory().getItem(j).getItem(), allowTracking)) {
                    return player.getInventory().getItem(j);
                }
            }
        }
        return null;
    }

    //Calculate the cosine of the angle between 2 vectors using fast algorithm.
    public static double vec2AngleCos(double x1, double y1, double x2, double y2) {
        return Mth.fastInvSqrt(modSqr(x1, y1)) * Mth.fastInvSqrt(modSqr(x2, y2)) * (x1 * x2 + y1 * y2);
    }

    public static double vec3AngleCos(Vec3 a, Vec3 b) {
        return Mth.fastInvSqrt(modSqr(a)) * Mth.fastInvSqrt(modSqr(b)) * (a.x * b.x + a.y * b.y + a.z * b.z);
    }

    //Calculate the square of the modulus(length) of a vector.
    public static double modSqr(double x1, double y1) {
        return x1 * x1 + y1 * y1;
    }

    public static double modSqr(Vec3 a) {
        return a.x * a.x + a.y * a.y + a.z * a.z;
    }

    public static double modSqr(double x, double y, double z) {
        return x * x + y * y + z * z;
    }

    //Rewrite vanilla "shootFromRotation" method to remove the influence of player's velocity.
    public static void shootFromRotation(Projectile projectile, float pX, float pY, float pZ, float pVelocity, float pInaccuracy) {
        float f = -Mth.sin(pY * Mth.DEG_TO_RAD) * Mth.cos(pX * Mth.DEG_TO_RAD);
        float f1 = -Mth.sin((pX + pZ) * Mth.DEG_TO_RAD);
        float f2 = Mth.cos(pY * Mth.DEG_TO_RAD) * Mth.cos(pX * Mth.DEG_TO_RAD);
        projectile.shoot(f, f1, f2, pVelocity, pInaccuracy);
    }

    //Fucking Minecraft anti-human coordinate system.
    public static Vec3 SphericalToCartesian(float pitch, float yaw) {
        return new Vec3(-Mth.cos(pitch) * Mth.sin(yaw), -Mth.sin(pitch), Mth.cos(pitch) * Mth.cos(yaw));
    }

    //Check weather the player can catch the snowball
    public static boolean isHeadingToSnowball(Player player, BSFSnowballEntity snowballEntity) {
        float pitch = player.getXRot() * Mth.DEG_TO_RAD;
        float yaw = player.getYRot() * Mth.DEG_TO_RAD;
        Vec3 speedVec = snowballEntity.getDeltaMovement().normalize();
        Vec3 cameraVec = SphericalToCartesian(pitch, yaw);
        return Math.abs(cameraVec.dot(speedVec) + 1.0) < 0.2;
    }

    public static void handleExplosion(BSFSnowballEntity snowball, float radius) {
        Level level = snowball.getLevel();
        if (level.getGameRules().getBoolean((GameRules.RULE_MOBGRIEFING))) {
            level.explode(null, snowball.getX(), snowball.getY(), snowball.getZ(), radius, Explosion.BlockInteraction.DESTROY);
        } else {
            level.explode(null, snowball.getX(), snowball.getY(), snowball.getZ(), radius, Explosion.BlockInteraction.NONE);
        }
    }

    //todo:check more blocks

    /**
     * This method can check whether there are blocks on head-head and head-feet line segments (See param).
     * Specially designed for basin of snow/powder snow.
     * @param rVec The vector from attacker's head to target's head.
     * @param rVec1 The vector from attacker's head to target's feet.
     * @param player The attacker.
     * @param level The attacker's level.
     * @return Whether both rVec and rVec1 are blocked by block.
     */
    public static boolean isBlocked(Vec3 rVec, Vec3 rVec1, Player player, Level level) {
        double offsetX = 0.25 * rVec.z * Mth.fastInvSqrt(modSqr(rVec.x, rVec.z));
        double offsetZ = 0.25 * rVec.x * Mth.fastInvSqrt(modSqr(rVec.x, rVec.z));
        double x = player.getX();
        double y = player.getEyeY();
        double z = player.getZ();
        Vec3 n = rVec.normalize().scale(0.25);
        int l = (int) (4 * Math.sqrt(modSqr(rVec)));
        boolean flag = false;
        for (int i = 0; i < l; i++) {
            int k = 0;
            for (int j = -1; j <= 1; j++) {
                BlockPos blockPos = new BlockPos(x - offsetX * j, y, z + offsetZ * j);
                BlockState blockState = level.getBlockState(blockPos);
                if (blockState.is(Blocks.AIR)) {
                    k++;
                }
            }
            if (k < 2) {
                System.out.println("failed eye");//todo:delete
                flag = true;
                break;
            }
            x += n.x;
            y += n.y;
            z += n.z;
        }
        x = player.getX();
        y = player.getEyeY();
        z = player.getZ();
        n = rVec1.normalize().scale(0.25);
        l = (int) (4 * Math.sqrt(modSqr(rVec1)));
        for (int i = 0; i < l; i++) {
            int k = 0;
            for (int j = -1; j <= 1; j++) {
                BlockPos blockPos = new BlockPos(x - offsetX * j, y, z + offsetZ * j);
                BlockState blockState = level.getBlockState(blockPos);
                if (blockState.is(Blocks.AIR)) {
                    k++;
                }
            }
            if (k < 2) {
                System.out.println("failed feet");//todo:delete
                return flag;
            }
            x += n.x;
            y += n.y;
            z += n.z;
        }
        return false;
    }
}
