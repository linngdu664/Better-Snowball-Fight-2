package com.linngdu664.bsf.entity;

import com.linngdu664.bsf.Main;
import com.linngdu664.bsf.entity.snowball.force.*;
import com.linngdu664.bsf.entity.snowball.nomal.*;
import com.linngdu664.bsf.entity.snowball.special.*;
import com.linngdu664.bsf.entity.snowball.tracking.*;
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
    public static final RegistryObject<EntityType<SmoothSnowballEntity>> SMOOTH_SNOWBALL =
            ENTITY_TYPES.register("smooth_snowball", () -> EntityType.Builder.<SmoothSnowballEntity>of(SmoothSnowballEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10)
                    .build(new ResourceLocation(Main.MODID, "smooth_snowball").toString()));
    public static final RegistryObject<EntityType<CompactedSnowballEntity>> COMPACTED_SNOWBALL =
            ENTITY_TYPES.register("compacted_snowball", () -> EntityType.Builder.<CompactedSnowballEntity>of(CompactedSnowballEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10)
                    .build(new ResourceLocation(Main.MODID, "compacted_snowball").toString()));
    public static final RegistryObject<EntityType<StoneSnowballEntity>> STONE_SNOWBALL =
            ENTITY_TYPES.register("stone_snowball", () -> EntityType.Builder.<StoneSnowballEntity>of(StoneSnowballEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10)
                    .build(new ResourceLocation(Main.MODID, "stone_snowball").toString()));
    public static final RegistryObject<EntityType<GlassSnowballEntity>> GLASS_SNOWBALL =
            ENTITY_TYPES.register("glass_snowball", () -> EntityType.Builder.<GlassSnowballEntity>of(GlassSnowballEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10)
                    .build(new ResourceLocation(Main.MODID, "glass_snowball").toString()));
    public static final RegistryObject<EntityType<IceSnowballEntity>> ICE_SNOWBALL =
            ENTITY_TYPES.register("ice_snowball", () -> EntityType.Builder.<IceSnowballEntity>of(IceSnowballEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10)
                    .build(new ResourceLocation(Main.MODID, "ice_snowball").toString()));
    public static final RegistryObject<EntityType<IronSnowballEntity>> IRON_SNOWBALL =
            ENTITY_TYPES.register("iron_snowball", () -> EntityType.Builder.<IronSnowballEntity>of(IronSnowballEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10)
                    .build(new ResourceLocation(Main.MODID, "iron_snowball").toString()));
    public static final RegistryObject<EntityType<GoldSnowballEntity>> GOLD_SNOWBALL =
            ENTITY_TYPES.register("gold_snowball", () -> EntityType.Builder.<GoldSnowballEntity>of(GoldSnowballEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10)
                    .build(new ResourceLocation(Main.MODID, "gold_snowball").toString()));
    public static final RegistryObject<EntityType<ObsidianSnowballEntity>> OBSIDIAN_SNOWBALL =
            ENTITY_TYPES.register("obsidian_snowball", () -> EntityType.Builder.<ObsidianSnowballEntity>of(ObsidianSnowballEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10)
                    .build(new ResourceLocation(Main.MODID, "obsidian_snowball").toString()));
    public static final RegistryObject<EntityType<SpectralSnowballEntity>> SPECTRAL_SNOWBALL =
            ENTITY_TYPES.register("spectral_snowball", () -> EntityType.Builder.<SpectralSnowballEntity>of(SpectralSnowballEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10)
                    .build(new ResourceLocation(Main.MODID, "spectral_snowball").toString()));
    public static final RegistryObject<EntityType<ExplosiveSnowballEntity>> EXPLOSIVE_SNOWBALL =
            ENTITY_TYPES.register("explosive_snowball", () -> EntityType.Builder.<ExplosiveSnowballEntity>of(ExplosiveSnowballEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10)
                    .build(new ResourceLocation(Main.MODID, "explosive_snowball").toString()));
    public static final RegistryObject<EntityType<EnderSnowballEntity>> ENDER_SNOWBALL =
            ENTITY_TYPES.register("ender_snowball", () -> EntityType.Builder.<EnderSnowballEntity>of(EnderSnowballEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10)
                    .build(new ResourceLocation(Main.MODID, "ender_snowball").toString()));
    public static final RegistryObject<EntityType<FrozenSnowballEntity>> FROZEN_SNOWBALL =
            ENTITY_TYPES.register("frozen_snowball", () -> EntityType.Builder.<FrozenSnowballEntity>of(FrozenSnowballEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10)
                    .build(new ResourceLocation(Main.MODID, "frozen_snowball").toString()));
    public static final RegistryObject<EntityType<PowderSnowballEntity>> POWDER_SNOWBALL =
            ENTITY_TYPES.register("powder_snowball", () -> EntityType.Builder.<PowderSnowballEntity>of(PowderSnowballEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10)
                    .build(new ResourceLocation(Main.MODID, "powder_snowball").toString()));
    public static final RegistryObject<EntityType<SubspaceSnowballEntity>> SUBSPACE_SNOWBALL =
            ENTITY_TYPES.register("subspace_snowball", () -> EntityType.Builder.<SubspaceSnowballEntity>of(SubspaceSnowballEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10)
                    .build(new ResourceLocation(Main.MODID, "subspace_snowball").toString()));
    public static final RegistryObject<EntityType<ExplosiveMonsterTrackingSnowballEntity>> EXPLOSIVE_MONSTER_TRACKING_SNOWBALL =
            ENTITY_TYPES.register("explosive_monster_tracking_snowball", () -> EntityType.Builder.<ExplosiveMonsterTrackingSnowballEntity>of(ExplosiveMonsterTrackingSnowballEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10)
                    .build(new ResourceLocation(Main.MODID, "explosive_monster_tracking_snowball").toString()));
    public static final RegistryObject<EntityType<ExplosivePlayerTrackingSnowballEntity>> EXPLOSIVE_PLAYER_TRACKING_SNOWBALL =
            ENTITY_TYPES.register("explosive_player_tracking_snowball", () -> EntityType.Builder.<ExplosivePlayerTrackingSnowballEntity>of(ExplosivePlayerTrackingSnowballEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10)
                    .build(new ResourceLocation(Main.MODID, "explosive_player_tracking_snowball").toString()));
    public static final RegistryObject<EntityType<HeavyMonsterTrackingSnowballEntity>> HEAVY_MONSTER_TRACKING_SNOWBALL =
            ENTITY_TYPES.register("heavy_monster_tracking_snowball", () -> EntityType.Builder.<HeavyMonsterTrackingSnowballEntity>of(HeavyMonsterTrackingSnowballEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10)
                    .build(new ResourceLocation(Main.MODID, "heavy_monster_tracking_snowball").toString()));
    public static final RegistryObject<EntityType<HeavyPlayerTrackingSnowballEntity>> HEAVY_PLAYER_TRACKING_SNOWBALL =
            ENTITY_TYPES.register("heavy_player_tracking_snowball", () -> EntityType.Builder.<HeavyPlayerTrackingSnowballEntity>of(HeavyPlayerTrackingSnowballEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10)
                    .build(new ResourceLocation(Main.MODID, "heavy_player_tracking_snowball").toString()));
    public static final RegistryObject<EntityType<LightMonsterTrackingSnowballEntity>> LIGHT_MONSTER_TRACKING_SNOWBALL =
            ENTITY_TYPES.register("light_monster_tracking_snowball", () -> EntityType.Builder.<LightMonsterTrackingSnowballEntity>of(LightMonsterTrackingSnowballEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10)
                    .build(new ResourceLocation(Main.MODID, "light_monster_tracking_snowball").toString()));
    public static final RegistryObject<EntityType<LightPlayerTrackingSnowballEntity>> LIGHT_PLAYER_TRACKING_SNOWBALL =
            ENTITY_TYPES.register("light_player_tracking_snowball", () -> EntityType.Builder.<LightPlayerTrackingSnowballEntity>of(LightPlayerTrackingSnowballEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10)
                    .build(new ResourceLocation(Main.MODID, "light_player_tracking_snowball").toString()));
    public static final RegistryObject<EntityType<BlackHoleSnowballEntity>> BLACK_HOLE_SNOWBALL =
            ENTITY_TYPES.register("black_hole_snowball", () -> EntityType.Builder.<BlackHoleSnowballEntity>of(BlackHoleSnowballEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10)
                    .build(new ResourceLocation(Main.MODID, "black_hole_snowball").toString()));
    public static final RegistryObject<EntityType<MonsterGravitySnowballEntity>> MONSTER_GRAVITY_SNOWBALL =
            ENTITY_TYPES.register("monster_gravity_snowball", () -> EntityType.Builder.<MonsterGravitySnowballEntity>of(MonsterGravitySnowballEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10)
                    .build(new ResourceLocation(Main.MODID, "monster_gravity_snowball").toString()));
    public static final RegistryObject<EntityType<MonsterRepulsionSnowballEntity>> MONSTER_REPULSION_SNOWBALL =
            ENTITY_TYPES.register("monster_repulsion_snowball", () -> EntityType.Builder.<MonsterRepulsionSnowballEntity>of(MonsterRepulsionSnowballEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10)
                    .build(new ResourceLocation(Main.MODID, "monster_repulsion_snowball").toString()));
    public static final RegistryObject<EntityType<ProjectileGravitySnowballEntity>> PROJECTILE_GRAVITY_SNOWBALL =
            ENTITY_TYPES.register("projectile_gravity_snowball", () -> EntityType.Builder.<ProjectileGravitySnowballEntity>of(ProjectileGravitySnowballEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10)
                    .build(new ResourceLocation(Main.MODID, "projectile_gravity_snowball").toString()));
    public static final RegistryObject<EntityType<ProjectileRepulsionSnowballEntity>> PROJECTILE_REPULSION_SNOWBALL =
            ENTITY_TYPES.register("projectile_repulsion_snowball", () -> EntityType.Builder.<ProjectileRepulsionSnowballEntity>of(ProjectileRepulsionSnowballEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10)
                    .build(new ResourceLocation(Main.MODID, "projectile_repulsion_snowball").toString()));
}
