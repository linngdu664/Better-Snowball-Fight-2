package com.linngdu664.bsf.util;

import com.linngdu664.bsf.entity.AdvancedSnowballEntity;
import com.linngdu664.bsf.item.setter.ItemRegister;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public class Util {
    public static boolean isHeadingToSnowball(Player player, AdvancedSnowballEntity snowballEntity) {
        float pitch = player.getXRot() * 0.01745329F;
        float yaw = player.getYRot() * 0.01745329F;
        Vec3 speedVec = snowballEntity.getDeltaMovement().normalize();
        Vec3 cameraVec = new Vec3(-Mth.cos(pitch) * Mth.sin(yaw), -Mth.sin(pitch), Mth.cos(pitch) * Mth.cos(yaw));
        return Mth.abs((float) (cameraVec.dot(speedVec) + 1.0F)) < 0.2F;
    }

    public static boolean isAmmoTank(ItemStack stack) {
        return stack.getItem() == ItemRegister.COMPACTED_SNOWBALL_STORAGE_TANK.get() || stack.getItem() == ItemRegister.EXPLOSIVE_SNOWBALL_STORAGE_TANK.get() ||
                stack.getItem() == ItemRegister.GLASS_SNOWBALL_STORAGE_TANK.get() || stack.getItem() == ItemRegister.GOLD_SNOWBALL_STORAGE_TANK.get() ||
                stack.getItem() == ItemRegister.ICE_SNOWBALL_STORAGE_TANK.get() || stack.getItem() == ItemRegister.IRON_SNOWBALL_STORAGE_TANK.get() ||
                stack.getItem() == ItemRegister.OBSIDIAN_SNOWBALL_STORAGE_TANK.get() || stack.getItem() == ItemRegister.STONE_SNOWBALL_STORAGE_TANK.get() ||
                stack.getItem() == ItemRegister.SPECTRAL_SNOWBALL_STORAGE_TANK.get();
    }

    public static boolean isAmmo(ItemStack stack) {
        return stack.getItem() == ItemRegister.COMPACTED_SNOWBALL.get() || stack.getItem() == ItemRegister.EXPLOSIVE_SNOWBALL.get() ||
                stack.getItem() == ItemRegister.GLASS_SNOWBALL.get() || stack.getItem() == ItemRegister.GOLD_SNOWBALL.get() ||
                stack.getItem() == ItemRegister.ICE_SNOWBALL.get() || stack.getItem() == ItemRegister.IRON_SNOWBALL.get() ||
                stack.getItem() == ItemRegister.OBSIDIAN_SNOWBALL.get() || stack.getItem() == ItemRegister.STONE_SNOWBALL.get() ||
                stack.getItem() == ItemRegister.SPECTRAL_SNOWBALL.get();
    }

    public static ItemStack findAmmo(Player player, boolean onlyTank) {
        for (int j = 0; j < player.getInventory().getContainerSize(); j++) {
            if (isAmmoTank(player.getInventory().getItem(j))) {
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
        return new ItemStack(Items.AIR, 0);
    }
}
