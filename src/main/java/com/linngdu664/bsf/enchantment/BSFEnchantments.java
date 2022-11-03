package com.linngdu664.bsf.enchantment;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BSFEnchantments {
    public static final DeferredRegister<Enchantment> REGISTRY = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, "bsf");
    public static final RegistryObject<Enchantment> KINETIC_ENERGY_STORAGE = REGISTRY.register("kinetic_energy_storage", KineticEnergyStorageEnchantment::new);
}
