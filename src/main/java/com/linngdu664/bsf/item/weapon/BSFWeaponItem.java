package com.linngdu664.bsf.item.weapon;

import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.util.ItemGroup;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

public abstract class BSFWeaponItem extends Item {
    public BSFWeaponItem(int durability, Rarity rarity) {
        super(new Properties().tab(ItemGroup.MAIN).stacksTo(1).durability(durability).rarity(rarity));
    }

    protected boolean isAmmoTank(Item item, boolean allowPowder) {
        return item == ItemRegister.COMPACTED_SNOWBALL_STORAGE_TANK.get() || item == ItemRegister.EXPLOSIVE_SNOWBALL_STORAGE_TANK.get() ||
                item == ItemRegister.GLASS_SNOWBALL_STORAGE_TANK.get() || item == ItemRegister.GOLD_SNOWBALL_STORAGE_TANK.get() ||
                item == ItemRegister.ICE_SNOWBALL_STORAGE_TANK.get() || item == ItemRegister.IRON_SNOWBALL_STORAGE_TANK.get() ||
                item == ItemRegister.OBSIDIAN_SNOWBALL_STORAGE_TANK.get() || item == ItemRegister.STONE_SNOWBALL_STORAGE_TANK.get() ||
                item == ItemRegister.SPECTRAL_SNOWBALL_STORAGE_TANK.get() || item == ItemRegister.FROZEN_SNOWBALL_STORAGE_TANK.get() ||
                item == ItemRegister.POWDER_SNOWBALL_STORAGE_TANK.get() && allowPowder ||
                item == ItemRegister.LIGHT_PLAYER_TRACKING_SNOWBALL_STORAGE_TANK.get() || item == ItemRegister.HEAVY_PLAYER_TRACKING_SNOWBALL_STORAGE_TANK.get() ||
                item == ItemRegister.EXPLOSIVE_PLAYER_TRACKING_SNOWBALL_STORAGE_TANK.get() || item == ItemRegister.LIGHT_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get() ||
                item == ItemRegister.HEAVY_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get() || item == ItemRegister.EXPLOSIVE_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get();
    }

    protected boolean isAmmo(Item item) {
        return item == ItemRegister.COMPACTED_SNOWBALL.get() || item == ItemRegister.EXPLOSIVE_SNOWBALL.get() ||
                item == ItemRegister.GLASS_SNOWBALL.get() || item == ItemRegister.GOLD_SNOWBALL.get() ||
                item == ItemRegister.ICE_SNOWBALL.get() || item == ItemRegister.IRON_SNOWBALL.get() ||
                item == ItemRegister.OBSIDIAN_SNOWBALL.get() || item == ItemRegister.STONE_SNOWBALL.get() ||
                item == ItemRegister.SPECTRAL_SNOWBALL.get() || item == ItemRegister.FROZEN_SNOWBALL.get() ||
                item == ItemRegister.POWDER_SNOWBALL.get() ||
                item == ItemRegister.LIGHT_PLAYER_TRACKING_SNOWBALL.get() || item == ItemRegister.HEAVY_PLAYER_TRACKING_SNOWBALL.get() ||
                item == ItemRegister.EXPLOSIVE_PLAYER_TRACKING_SNOWBALL.get() || item == ItemRegister.LIGHT_MONSTER_TRACKING_SNOWBALL.get() ||
                item == ItemRegister.HEAVY_MONSTER_TRACKING_SNOWBALL.get() || item == ItemRegister.EXPLOSIVE_MONSTER_TRACKING_SNOWBALL.get();
    }

    /**
     * Find the ammo of the weapon in player's inventory. It will search tanks first, and then it will search bulk
     * snowballs if "onlyTank" is false.
     * @param player The user of the weapon.
     * @param onlyTank Whether the weapon can only use the snowball in tanks.
     * @param allowPowder Whether the weapon can shoot powder snow snowball.
     * @return The first valid ammo itemstack. If the method can't find a proper itemstack, it will return null.
     */
    protected ItemStack findAmmo(Player player, boolean onlyTank, boolean allowPowder) {
        for (int j = 0; j < player.getInventory().getContainerSize(); j++) {
            if (isAmmoTank(player.getInventory().getItem(j).getItem(), allowPowder)) {
                return player.getInventory().getItem(j);
            }
        }
        if (!onlyTank) {
            for (int j = 0; j < player.getInventory().getContainerSize(); j++) {
                if (isAmmo(player.getInventory().getItem(j).getItem())) {
                    return player.getInventory().getItem(j);
                }
            }
        }
        return null;
    }

    //Rewrite vanilla "shootFromRotation" method to remove the influence of player's velocity.
    protected void BSFShootFromRotation(Projectile projectile, float pX, float pY, float pVelocity, float pInaccuracy) {
        float f = -Mth.sin(pY * Mth.DEG_TO_RAD) * Mth.cos(pX * Mth.DEG_TO_RAD);
        float f1 = -Mth.sin(pX * Mth.DEG_TO_RAD);
        float f2 = Mth.cos(pY * Mth.DEG_TO_RAD) * Mth.cos(pX * Mth.DEG_TO_RAD);
        projectile.shoot(f, f1, f2, pVelocity, pInaccuracy);
    }

    protected void consumeAmmo(ItemStack itemStack, Player player) {
        if (isAmmoTank(itemStack.getItem(), true)) {
            itemStack.hurtAndBreak(1, player, (p) -> p.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.EMPTY_SNOWBALL_STORAGE_TANK.get()), true));
        } else if (!player.getAbilities().instabuild) {
            itemStack.shrink(1);
            if (itemStack.isEmpty()) {
                player.getInventory().removeItem(itemStack);
            }
        }
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack pStack, ItemStack pRepairCandidate) {
        return pRepairCandidate.is(Items.IRON_INGOT);
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        return 1;
    }
}
