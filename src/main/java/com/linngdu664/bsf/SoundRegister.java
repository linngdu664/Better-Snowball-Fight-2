package com.linngdu664.bsf;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundRegister {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, "bsf");
    public static final RegistryObject<SoundEvent> SNOWBALL_CANNON_SHOOT = build("snowball_cannon_shoot");
    public static final RegistryObject<SoundEvent> SNOWBALL_MACHINE_GUN_SHOOT = build("snowball_machine_gun_shoot");
    public static final RegistryObject<SoundEvent> SHOTGUN_FIRE_1 = build("shotgun_fire1");
    public static final RegistryObject<SoundEvent> SHOTGUN_FIRE_2 = build("shotgun_fire2");
    public static final RegistryObject<SoundEvent> BLACK_HOLE_START = build("black_hole_start");

    private static RegistryObject<SoundEvent> build (String id) {
        return SOUNDS.register(id, () -> new SoundEvent(new ResourceLocation("bsf", id)));
    }
}
