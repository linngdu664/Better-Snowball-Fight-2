package com.linngdu664.bsf.item.setter;

import com.linngdu664.bsf.item.misc.*;
import com.linngdu664.bsf.item.snowball.snowballs.*;
import com.linngdu664.bsf.item.weapon.SnowballCannonItem;
import com.linngdu664.bsf.item.weapon.SnowballMachineGunItem;
import com.linngdu664.bsf.item.weapon.SnowballShotgunItem;
import com.linngdu664.bsf.util.SnowballType;
import com.linngdu664.bsf.util.Target;
import com.linngdu664.bsf.util.TrackingSnowballMode;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegister {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "bsf");

    public static final RegistryObject<Item> SMOOTH_SNOWBALL = ITEMS.register("smooth_snowball", SmoothSnowballItem::new);
    public static final RegistryObject<Item> COMPACTED_SNOWBALL = ITEMS.register("compacted_snowball", CompactedSnowballItem::new);
    public static final RegistryObject<Item> STONE_SNOWBALL = ITEMS.register("stone_snowball", StoneSnowballItem::new);
    public static final RegistryObject<Item> GLASS_SNOWBALL = ITEMS.register("glass_snowball", GlassSnowballItem::new);
    public static final RegistryObject<Item> ICE_SNOWBALL = ITEMS.register("ice_snowball", IceSnowballItem::new);
    public static final RegistryObject<Item> IRON_SNOWBALL = ITEMS.register("iron_snowball", IronSnowballItem::new);
    public static final RegistryObject<Item> GOLD_SNOWBALL = ITEMS.register("gold_snowball", GoldSnowballItem::new);
    public static final RegistryObject<Item> OBSIDIAN_SNOWBALL = ITEMS.register("obsidian_snowball", ObsidianSnowballItem::new);
    public static final RegistryObject<Item> EXPLOSIVE_SNOWBALL = ITEMS.register("explosive_snowball", ExplosiveSnowballItem::new);
    public static final RegistryObject<Item> SPECTRAL_SNOWBALL = ITEMS.register("spectral_snowball", SpectralSnowballItem::new);

    public static final RegistryObject<Item> LIGHT_MONSTER_TRACKING_SNOWBALL = ITEMS.register("monster_tracking_snowball", () -> new TrackingSnowballItem(Target.MONSTER, TrackingSnowballMode.NO_DAMAGE));
    public static final RegistryObject<Item> HEAVY_MONSTER_TRACKING_SNOWBALL = ITEMS.register("monster_tracking_snowball_with_damage", () -> new TrackingSnowballItem(Target.MONSTER, TrackingSnowballMode.HAVE_DAMAGE));
    public static final RegistryObject<Item> EXPLOSIVE_MONSTER_TRACKING_SNOWBALL = ITEMS.register("explosive_monster_tracking_snowball", () -> new TrackingSnowballItem(Target.MONSTER, TrackingSnowballMode.EXPLOSIVE));
    public static final RegistryObject<Item> LIGHT_PLAYER_TRACKING_SNOWBALL = ITEMS.register("player_tracking_snowball", () -> new TrackingSnowballItem(Target.PLAYER, TrackingSnowballMode.NO_DAMAGE));
    public static final RegistryObject<Item> HEAVY_PLAYER_TRACKING_SNOWBALL = ITEMS.register("player_tracking_snowball_with_damage", () -> new TrackingSnowballItem(Target.PLAYER, TrackingSnowballMode.HAVE_DAMAGE));
    public static final RegistryObject<Item> EXPLOSIVE_PLAYER_TRACKING_SNOWBALL = ITEMS.register("explosive_player_tracking_snowball", () -> new TrackingSnowballItem(Target.PLAYER, TrackingSnowballMode.EXPLOSIVE));

    public static final RegistryObject<Item> MONSTER_GRAVITY_SNOWBALL = ITEMS.register("gravity_snowball_to_monster", () -> new ForceSnowballItem(Target.MONSTER, TrackingSnowballMode.GRAVITY));
    public static final RegistryObject<Item> PROJECTILE_GRAVITY_SNOWBALL = ITEMS.register("gravity_snowball_to_projectile", () -> new ForceSnowballItem(Target.PROJECTILE, TrackingSnowballMode.GRAVITY));
    public static final RegistryObject<Item> MONSTER_REPULSION_SNOWBALL = ITEMS.register("repulsion_snowball_to_monster", () -> new ForceSnowballItem(Target.MONSTER, TrackingSnowballMode.REPULSION));
    public static final RegistryObject<Item> PROJECTILE_REPULSION_SNOWBALL = ITEMS.register("repulsion_snowball_to_projectile", () -> new ForceSnowballItem(Target.PROJECTILE, TrackingSnowballMode.REPULSION));
    public static final RegistryObject<Item> BLACK_HOLE_SNOWBALL = ITEMS.register("black_hole_snowball", () -> new ForceSnowballItem(Target.MIX, TrackingSnowballMode.BLACK_HOLE));

    public static final RegistryObject<Item> COMPACTED_SNOWBALL_SET = ITEMS.register("compacted_snowball_set", CompactedSnowballSetItem::new);
    public static final RegistryObject<Item> WOOD_SNOWBALL_CLAMP = ITEMS.register("wood_snowball_clamp", () -> new SnowballClampItem(Tiers.WOOD));
    public static final RegistryObject<Item> STONE_SNOWBALL_CLAMP = ITEMS.register("stone_snowball_clamp", () -> new SnowballClampItem(Tiers.STONE));
    public static final RegistryObject<Item> IRON_SNOWBALL_CLAMP = ITEMS.register("iron_snowball_clamp", () -> new SnowballClampItem(Tiers.IRON));
    public static final RegistryObject<Item> GOLD_SNOWBALL_CLAMP = ITEMS.register("gold_snowball_clamp", () -> new SnowballClampItem(Tiers.GOLD));
    public static final RegistryObject<Item> DIAMOND_SNOWBALL_CLAMP = ITEMS.register("diamond_snowball_clamp", () -> new SnowballClampItem(Tiers.DIAMOND));
    public static final RegistryObject<Item> NETHERITE_SNOWBALL_CLAMP = ITEMS.register("netherite_snowball_clamp", () -> new SnowballClampItem(Tiers.NETHERITE));

    public static final RegistryObject<Item> SUPER_POWER_CORE = ITEMS.register("super_power_core", SuperFrozenCoreItem::new);
    public static final RegistryObject<Item> SUPER_FROZEN_CORE = ITEMS.register("super_frozen_core", SuperPowerCoreItem::new);

    public static final RegistryObject<Item> GLOVE = ITEMS.register("glove", GloveItem::new);
    public static final RegistryObject<Item> ICE_SKATES_ITEM = ITEMS.register("ice_skates", IceSkatesItem::new);
    public static final RegistryObject<Item> POPSICLE = ITEMS.register("popsicle", PopsicleItem::new);
    public static final RegistryObject<Item> MILK_POPSICLE = ITEMS.register("milk_popsicle", MilkPopsicleItem::new);

    public static final RegistryObject<Item> SNOWBALL_CANNON = ITEMS.register("snowball_cannon", SnowballCannonItem::new);
    public static final RegistryObject<Item> POWERFUL_SNOWBALL_CANNON = ITEMS.register("powerful_snowball_cannon", SnowballCannonItem::new);
    public static final RegistryObject<Item> FREEZING_SNOWBALL_CANNON = ITEMS.register("freezing_snowball_cannon", SnowballCannonItem::new);
    public static final RegistryObject<Item> SNOWBALL_MACHINE_GUN = ITEMS.register("snowball_machine_gun", SnowballMachineGunItem::new);
    public static final RegistryObject<Item> SNOWBALL_SHOTGUN = ITEMS.register("snowball_shotgun", SnowballShotgunItem::new);

    public static final RegistryObject<Item> EMPTY_SNOWBALL_STORAGE_TANK = ITEMS.register("empty_snowball_storage_tank", EmptySnowballStorageTankItem::new);
    public static final RegistryObject<Item> COMPACTED_SNOWBALL_STORAGE_TANK = ITEMS.register("compacted_snowball_storage_tank", () -> new SnowballStorageTankItem(SnowballType.COMPACTED));
    public static final RegistryObject<Item> STONE_SNOWBALL_STORAGE_TANK = ITEMS.register("stone_snowball_storage_tank", () -> new SnowballStorageTankItem(SnowballType.STONE));
    public static final RegistryObject<Item> GLASS_SNOWBALL_STORAGE_TANK = ITEMS.register("glass_snowball_storage_tank", () -> new SnowballStorageTankItem(SnowballType.GLASS));
    public static final RegistryObject<Item> ICE_SNOWBALL_STORAGE_TANK = ITEMS.register("ice_snowball_storage_tank", () -> new SnowballStorageTankItem(SnowballType.ICE));
    public static final RegistryObject<Item> IRON_SNOWBALL_STORAGE_TANK = ITEMS.register("iron_snowball_storage_tank", () -> new SnowballStorageTankItem(SnowballType.IRON));
    public static final RegistryObject<Item> GOLD_SNOWBALL_STORAGE_TANK = ITEMS.register("gold_snowball_storage_tank", () -> new SnowballStorageTankItem(SnowballType.GOLD));
    public static final RegistryObject<Item> OBSIDIAN_SNOWBALL_STORAGE_TANK = ITEMS.register("obsidian_snowball_storage_tank", () -> new SnowballStorageTankItem(SnowballType.OBSIDIAN));
    public static final RegistryObject<Item> EXPLOSIVE_SNOWBALL_STORAGE_TANK = ITEMS.register("explosive_snowball_storage_tank", () -> new SnowballStorageTankItem(SnowballType.EXPLOSIVE));
    public static final RegistryObject<Item> SPECTRAL_SNOWBALL_STORAGE_TANK = ITEMS.register("spectral_snowball_storage_tank", () -> new SnowballStorageTankItem(SnowballType.SPECTRAL));

    public static final RegistryObject<Item> LIGHT_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK = ITEMS.register("monster_tracking_snowball_storage_tank", () -> new SnowballStorageTankItem(SnowballType.TRACKING_MONSTER));
    public static final RegistryObject<Item> HEAVY_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK = ITEMS.register("monster_tracking_snowball_with_damage_storage_tank", () -> new SnowballStorageTankItem(SnowballType.TRACKING_MONSTER_DAMAGE));
    public static final RegistryObject<Item> EXPLOSIVE_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK = ITEMS.register("explosive_monster_tracking_snowball_storage_tank", () -> new SnowballStorageTankItem(SnowballType.TRACKING_MONSTER_EXPLOSIVE));
    public static final RegistryObject<Item> LIGHT_PLAYER_TRACKING_SNOWBALL_STORAGE_TANK = ITEMS.register("player_tracking_snowball_storage_tank", () -> new SnowballStorageTankItem(SnowballType.TRACKING_PLAYER));
    public static final RegistryObject<Item> HEAVY_PLAYER_TRACKING_SNOWBALL_STORAGE_TANK = ITEMS.register("player_tracking_snowball_with_damage_storage_tank", () -> new SnowballStorageTankItem(SnowballType.TRACKING_PLAYER_DAMAGE));
    public static final RegistryObject<Item> EXPLOSIVE_PLAYER_TRACKING_SNOWBALL_STORAGE_TANK = ITEMS.register("explosive_player_tracking_snowball_storage_tank", () -> new SnowballStorageTankItem(SnowballType.TRACKING_PLAYER_EXPLOSIVE));

    public static final RegistryObject<Item> MONSTER_GRAVITY_SNOWBALL_STORAGE_TANK = ITEMS.register("gravity_snowball_to_monster_storage_tank", () -> new SnowballStorageTankItem(SnowballType.GRAVITY_MONSTER));
    public static final RegistryObject<Item> PROJECTILE_GRAVITY_SNOWBALL_STORAGE_TANK = ITEMS.register("gravity_snowball_to_projectile_storage_tank", () -> new SnowballStorageTankItem(SnowballType.GRAVITY_PROJECTILE));
    public static final RegistryObject<Item> MONSTER_REPULSION_SNOWBALL_STORAGE_TANK = ITEMS.register("repulsion_snowball_to_monster_storage_tank", () -> new SnowballStorageTankItem(SnowballType.REPULSION_MONSTER));
    public static final RegistryObject<Item> PROJECTILE_REPULSION_SNOWBALL_STORAGE_TANK = ITEMS.register("repulsion_snowball_to_projectile_storage_tank", () -> new SnowballStorageTankItem(SnowballType.REPULSION_PROJECTILE));
    public static final RegistryObject<Item> BLACK_HOLE_SNOWBALL_STORAGE_TANK = ITEMS.register("black_hole_snowball_storage_tank", () -> new SnowballStorageTankItem(SnowballType.BLACK_HOLE));



    public static final Group GROUP = new Group();
}
