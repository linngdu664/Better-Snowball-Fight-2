package com.linngdu664.bsf.enchantment;

import com.linngdu664.bsf.item.ItemRegister;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class KineticEnergyStorageEnchantment extends Enchantment {
    public KineticEnergyStorageEnchantment(EquipmentSlot... slots) {
        super(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.ARMOR_FEET, slots);
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        Item item = stack.getItem();
        return item == ItemRegister.SNOW_FALL_BOOTS.get();
    }
}
