package com.linngdu664.bsf.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EffectRegister {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, "bsf");
    public static final RegistryObject<MobEffect> COLD_RESISTANCE_EFFECT = EFFECTS.register("cold_resistance", () -> new ColdResistanceEffect(MobEffectCategory.BENEFICIAL, 0xfefa03));
}
