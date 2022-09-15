package com.linngdu664.bsf.item;

import com.linngdu664.bsf.item.misc.*;
import com.linngdu664.bsf.item.snowball.force_snowball.*;
import com.linngdu664.bsf.item.snowball.normal_snowball.*;
import com.linngdu664.bsf.item.snowball.tracking_snowball.*;
import com.linngdu664.bsf.item.tank.force_snowball.*;
import com.linngdu664.bsf.item.tank.normal_snowball.*;
import com.linngdu664.bsf.item.tank.tracking_snowball.*;
import com.linngdu664.bsf.item.weapon.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegister {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "bsf");

    public static final RegistryObject<Item> SMOOTH_SNOWBALL = ITEMS.register("smooth_snowball", SmoothSnowballItem::new);
    public static final RegistryObject<Item> COMPACTED_SNOWBALL = ITEMS.register("compacted_snowball", CompactedSnowballItem::new);
    public static final RegistryObject<Item> COMPACTED_SNOWBALL_SET = ITEMS.register("compacted_snowball_set", CompactedSnowballSetItem::new);
    public static final RegistryObject<Item> STONE_SNOWBALL = ITEMS.register("stone_snowball", StoneSnowballItem::new);
    public static final RegistryObject<Item> GLASS_SNOWBALL = ITEMS.register("glass_snowball", GlassSnowballItem::new);
    public static final RegistryObject<Item> ICE_SNOWBALL = ITEMS.register("ice_snowball", IceSnowballItem::new);
    public static final RegistryObject<Item> IRON_SNOWBALL = ITEMS.register("iron_snowball", IronSnowballItem::new);
    public static final RegistryObject<Item> GOLD_SNOWBALL = ITEMS.register("gold_snowball", GoldSnowballItem::new);
    public static final RegistryObject<Item> OBSIDIAN_SNOWBALL = ITEMS.register("obsidian_snowball", ObsidianSnowballItem::new);
    public static final RegistryObject<Item> EXPLOSIVE_SNOWBALL = ITEMS.register("explosive_snowball", ExplosiveSnowballItem::new);
    public static final RegistryObject<Item> SPECTRAL_SNOWBALL = ITEMS.register("spectral_snowball", SpectralSnowballItem::new);
    public static final RegistryObject<Item> FROZEN_SNOWBALL = ITEMS.register("frozen_snowball", FrozenSnowballItem::new);
    public static final RegistryObject<Item> POWDER_SNOWBALL = ITEMS.register("powder_snowball", PowderSnowballItem::new);

    public static final RegistryObject<Item> LIGHT_MONSTER_TRACKING_SNOWBALL = ITEMS.register("monster_tracking_snowball", LightMonsterTrackingSnowballItem::new);
    public static final RegistryObject<Item> HEAVY_MONSTER_TRACKING_SNOWBALL = ITEMS.register("monster_tracking_snowball_with_damage", HeavyMonsterTrackingSnowballItem::new);
    public static final RegistryObject<Item> EXPLOSIVE_MONSTER_TRACKING_SNOWBALL = ITEMS.register("explosive_monster_tracking_snowball", ExplosiveMonsterTrackingSnowballItem::new);
    public static final RegistryObject<Item> LIGHT_PLAYER_TRACKING_SNOWBALL = ITEMS.register("player_tracking_snowball", LightPlayerTrackingSnowballItem::new);
    public static final RegistryObject<Item> HEAVY_PLAYER_TRACKING_SNOWBALL = ITEMS.register("player_tracking_snowball_with_damage", HeavyPlayerTrackingSnowballItem::new);
    public static final RegistryObject<Item> EXPLOSIVE_PLAYER_TRACKING_SNOWBALL = ITEMS.register("explosive_player_tracking_snowball", ExplosivePlayerTrackingSnowballItem::new);

    public static final RegistryObject<Item> MONSTER_GRAVITY_SNOWBALL = ITEMS.register("gravity_snowball_to_monster", MonsterGravitySnowballItem::new);
    public static final RegistryObject<Item> PROJECTILE_GRAVITY_SNOWBALL = ITEMS.register("gravity_snowball_to_projectile", ProjectileGravitySnowballItem::new);
    public static final RegistryObject<Item> MONSTER_REPULSION_SNOWBALL = ITEMS.register("repulsion_snowball_to_monster", MonsterRepulsionSnowballItem::new);
    public static final RegistryObject<Item> PROJECTILE_REPULSION_SNOWBALL = ITEMS.register("repulsion_snowball_to_projectile", ProjectileRepulsionSnowballItem::new);
    public static final RegistryObject<Item> BLACK_HOLE_SNOWBALL = ITEMS.register("black_hole_snowball", BlackHoleSnowballItem::new);

    public static final RegistryObject<Item> WOOD_SNOWBALL_CLAMP = ITEMS.register("wood_snowball_clamp", () -> new SnowballClampItem(Tiers.WOOD));
    public static final RegistryObject<Item> STONE_SNOWBALL_CLAMP = ITEMS.register("stone_snowball_clamp", () -> new SnowballClampItem(Tiers.STONE));
    public static final RegistryObject<Item> IRON_SNOWBALL_CLAMP = ITEMS.register("iron_snowball_clamp", () -> new SnowballClampItem(Tiers.IRON));
    public static final RegistryObject<Item> GOLD_SNOWBALL_CLAMP = ITEMS.register("gold_snowball_clamp", () -> new SnowballClampItem(Tiers.GOLD));
    public static final RegistryObject<Item> DIAMOND_SNOWBALL_CLAMP = ITEMS.register("diamond_snowball_clamp", () -> new SnowballClampItem(Tiers.DIAMOND));
    public static final RegistryObject<Item> NETHERITE_SNOWBALL_CLAMP = ITEMS.register("netherite_snowball_clamp", () -> new SnowballClampItem(Tiers.NETHERITE));

    public static final RegistryObject<Item> SUPER_POWER_CORE = ITEMS.register("super_power_core", SuperFrozenCoreItem::new);
    public static final RegistryObject<Item> SUPER_FROZEN_CORE = ITEMS.register("super_frozen_core", SuperPowerCoreItem::new);
    public static final RegistryObject<Item> TRACKING_CORE = ITEMS.register("tracking_core", TrackingCoreItem::new);
    public static final RegistryObject<Item> UNSTABLE_CORE = ITEMS.register("unstable_core", SuperPowerCoreItem::new);
    public static final RegistryObject<Item> REPULSION_CORE = ITEMS.register("repulsion_core", SuperPowerCoreItem::new);
    public static final RegistryObject<Item> GRAVITY_CORE = ITEMS.register("gravity_core", SuperPowerCoreItem::new);

    public static final RegistryObject<Item> GLOVE = ITEMS.register("glove", GloveItem::new);
    public static final RegistryObject<Item> ICE_SKATES_ITEM = ITEMS.register("ice_skates", IceSkatesItem::new);
    public static final RegistryObject<Item> POPSICLE = ITEMS.register("popsicle", PopsicleItem::new);
    public static final RegistryObject<Item> MILK_POPSICLE = ITEMS.register("milk_popsicle", MilkPopsicleItem::new);
    public static final RegistryObject<Item> EMPTY_BASIN = ITEMS.register("empty_basin", EmptyBasin::new);
    public static final RegistryObject<Item> BASIN_OF_SNOW = ITEMS.register("basin_of_snow", BasinOfSnow::new);
    public static final RegistryObject<Item> BASIN_OF_POWDER_SNOW = ITEMS.register("basin_of_powder_snow", BasinOfPowderSnow::new);

    public static final RegistryObject<Item> SNOWBALL_CANNON = ITEMS.register("snowball_cannon", SnowballCannonItem::new);
    public static final RegistryObject<Item> POWERFUL_SNOWBALL_CANNON = ITEMS.register("powerful_snowball_cannon", PowerfulSnowballCannonItem::new);
    public static final RegistryObject<Item> FREEZING_SNOWBALL_CANNON = ITEMS.register("freezing_snowball_cannon", FreezingSnowballCannonItem::new);
    public static final RegistryObject<Item> SNOWBALL_MACHINE_GUN = ITEMS.register("snowball_machine_gun", SnowballMachineGunItem::new);
    public static final RegistryObject<Item> SNOWBALL_SHOTGUN = ITEMS.register("snowball_shotgun", SnowballShotgunItem::new);

    public static final RegistryObject<Item> EMPTY_SNOWBALL_STORAGE_TANK = ITEMS.register("empty_snowball_storage_tank", EmptySnowballStorageTankItem::new);
    public static final RegistryObject<Item> COMPACTED_SNOWBALL_STORAGE_TANK = ITEMS.register("compacted_snowball_storage_tank", () -> new CompactedSnowballStorageTank(COMPACTED_SNOWBALL.get()));
    public static final RegistryObject<Item> STONE_SNOWBALL_STORAGE_TANK = ITEMS.register("stone_snowball_storage_tank", () -> new StoneSnowballStorageTank(STONE_SNOWBALL.get()));
    public static final RegistryObject<Item> GLASS_SNOWBALL_STORAGE_TANK = ITEMS.register("glass_snowball_storage_tank", () -> new GlassSnowballStorageTank(GLASS_SNOWBALL.get()));
    public static final RegistryObject<Item> ICE_SNOWBALL_STORAGE_TANK = ITEMS.register("ice_snowball_storage_tank", () -> new IceSnowballStorageTank(ICE_SNOWBALL.get()));
    public static final RegistryObject<Item> IRON_SNOWBALL_STORAGE_TANK = ITEMS.register("iron_snowball_storage_tank", () -> new IronSnowballStorageTank(IRON_SNOWBALL.get()));
    public static final RegistryObject<Item> GOLD_SNOWBALL_STORAGE_TANK = ITEMS.register("gold_snowball_storage_tank", () -> new GoldSnowballStorageTank(GOLD_SNOWBALL.get()));
    public static final RegistryObject<Item> OBSIDIAN_SNOWBALL_STORAGE_TANK = ITEMS.register("obsidian_snowball_storage_tank", () -> new ObsidianSnowballStorageTank(OBSIDIAN_SNOWBALL.get()));
    public static final RegistryObject<Item> EXPLOSIVE_SNOWBALL_STORAGE_TANK = ITEMS.register("explosive_snowball_storage_tank", () -> new ExplosiveSnowballStorageTank(EXPLOSIVE_SNOWBALL.get()));
    public static final RegistryObject<Item> SPECTRAL_SNOWBALL_STORAGE_TANK = ITEMS.register("spectral_snowball_storage_tank", () -> new SpectralSnowballStorageTank(SPECTRAL_SNOWBALL.get()));
    public static final RegistryObject<Item> FROZEN_SNOWBALL_STORAGE_TANK = ITEMS.register("frozen_snowball_storage_tank",  () -> new FrozenSnowballStorageTank(FROZEN_SNOWBALL.get()));
    public static final RegistryObject<Item> POWDER_SNOWBALL_STORAGE_TANK = ITEMS.register("powder_snowball_storage_tank",  () -> new PowderSnowballStorageTank(POWDER_SNOWBALL.get()));

    public static final RegistryObject<Item> LIGHT_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK = ITEMS.register("monster_tracking_snowball_storage_tank", () -> new LightMonsterTrackingSnowballStorageTank(LIGHT_MONSTER_TRACKING_SNOWBALL.get()));
    public static final RegistryObject<Item> HEAVY_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK = ITEMS.register("monster_tracking_snowball_with_damage_storage_tank", () -> new HeavyMonsterTrackingSnowballStorageTank(HEAVY_MONSTER_TRACKING_SNOWBALL.get()));
    public static final RegistryObject<Item> EXPLOSIVE_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK = ITEMS.register("explosive_monster_tracking_snowball_storage_tank", () -> new ExplosiveMonsterTrackingSnowballStorageTank(EXPLOSIVE_MONSTER_TRACKING_SNOWBALL.get()));
    public static final RegistryObject<Item> LIGHT_PLAYER_TRACKING_SNOWBALL_STORAGE_TANK = ITEMS.register("player_tracking_snowball_storage_tank", () -> new LightPlayerTrackingSnowballStorageTank(LIGHT_PLAYER_TRACKING_SNOWBALL.get()));
    public static final RegistryObject<Item> HEAVY_PLAYER_TRACKING_SNOWBALL_STORAGE_TANK = ITEMS.register("player_tracking_snowball_with_damage_storage_tank", () -> new HeavyPlayerTrackingSnowballStorageTank(HEAVY_PLAYER_TRACKING_SNOWBALL.get()));
    public static final RegistryObject<Item> EXPLOSIVE_PLAYER_TRACKING_SNOWBALL_STORAGE_TANK = ITEMS.register("explosive_player_tracking_snowball_storage_tank", () -> new ExplosivePlayerTrackingSnowballStorageTank(EXPLOSIVE_PLAYER_TRACKING_SNOWBALL.get()));

    public static final RegistryObject<Item> MONSTER_GRAVITY_SNOWBALL_STORAGE_TANK = ITEMS.register("gravity_snowball_to_monster_storage_tank", () -> new MonsterGravitySnowballStorageTank(MONSTER_GRAVITY_SNOWBALL.get()));
    public static final RegistryObject<Item> PROJECTILE_GRAVITY_SNOWBALL_STORAGE_TANK = ITEMS.register("gravity_snowball_to_projectile_storage_tank", () -> new ProjectileGravitySnowballStorageTank(PROJECTILE_GRAVITY_SNOWBALL.get()));
    public static final RegistryObject<Item> MONSTER_REPULSION_SNOWBALL_STORAGE_TANK = ITEMS.register("repulsion_snowball_to_monster_storage_tank", () -> new MonsterRepulsionSnowballStorageTank(MONSTER_REPULSION_SNOWBALL.get()));
    public static final RegistryObject<Item> PROJECTILE_REPULSION_SNOWBALL_STORAGE_TANK = ITEMS.register("repulsion_snowball_to_projectile_storage_tank", () -> new ProjectileRepulsionSnowballStorageTank(PROJECTILE_REPULSION_SNOWBALL.get()));
    public static final RegistryObject<Item> BLACK_HOLE_SNOWBALL_STORAGE_TANK = ITEMS.register("black_hole_snowball_storage_tank", () -> new BlackHoleSnowballStorageTank(BLACK_HOLE_SNOWBALL.get()));

}
