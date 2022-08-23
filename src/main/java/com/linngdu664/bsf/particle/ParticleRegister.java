package com.linngdu664.bsf.particle;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IRegistryDelegate;
import net.minecraftforge.registries.RegistryObject;

public class ParticleRegister {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, "bsf");
    public static final RegistryObject<SimpleParticleType> SNOW_PARTICLE = PARTICLE_TYPES.register("snow_particle", new IRegistryDelegate<>() {
        @Override
        public SimpleParticleType get() {
            return new SimpleParticleType(false);
        }

        @Override
        public ResourceLocation name() {
            return new ResourceLocation("bsf", "snow_particle");
        }

        @Override
        public Class<SimpleParticleType> type() {
            return null;
        }
    });
}
