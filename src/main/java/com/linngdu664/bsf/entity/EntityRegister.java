package com.linngdu664.bsf.entity;

import com.linngdu664.bsf.Main;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegister {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Main.MODID);

    public static final RegistryObject<EntityType<BSFSnowGolemEntity>> BSF_SNOW_GOLEM =
            ENTITY_TYPES.register("bsf_snow_golem", () -> EntityType.Builder.of(BSFSnowGolemEntity::new, MobCategory.MISC)
                    .sized(0.7F, 1.9F).clientTrackingRange(8).immuneTo(Blocks.POWDER_SNOW)
                    .build(new ResourceLocation(Main.MODID, "bsf_snow_golem").toString()));
}
