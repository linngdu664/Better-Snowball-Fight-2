package com.linngdu664.bsf.item.setter;

import com.linngdu664.bsf.util.SnowballType;
import com.linngdu664.bsf.item.*;
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
    public static final RegistryObject<Item> COMPACTED_SNOWBALL_SET = ITEMS.register("compacted_snowball_set", CompactedSnowballSetItem::new);
    public static final RegistryObject<Item> WOOD_SNOWBALL_CLAMP = ITEMS.register("wood_snowball_clamp", () -> new SnowballClampItem(Tiers.WOOD));
    public static final RegistryObject<Item> STONE_SNOWBALL_CLAMP = ITEMS.register("stone_snowball_clamp", () -> new SnowballClampItem(Tiers.STONE));
    public static final RegistryObject<Item> IRON_SNOWBALL_CLAMP = ITEMS.register("iron_snowball_clamp", () -> new SnowballClampItem(Tiers.IRON));
    public static final RegistryObject<Item> GOLD_SNOWBALL_CLAMP = ITEMS.register("gold_snowball_clamp", () -> new SnowballClampItem(Tiers.GOLD));
    public static final RegistryObject<Item> DIAMOND_SNOWBALL_CLAMP = ITEMS.register("diamond_snowball_clamp", () -> new SnowballClampItem(Tiers.DIAMOND));
    public static final RegistryObject<Item> NETHERITE_SNOWBALL_CLAMP = ITEMS.register("netherite_snowball_clamp", () -> new SnowballClampItem(Tiers.NETHERITE));
    public static final RegistryObject<Item> SNOWBALL_CANNON = ITEMS.register("snowball_cannon", () -> new SnowballCannonItem(0));
    public static final RegistryObject<Item> POWERFUL_SNOWBALL_CANNON = ITEMS.register("powerful_snowball_cannon", () -> new SnowballCannonItem(2));
    public static final RegistryObject<Item> FREEZING_SNOWBALL_CANNON = ITEMS.register("freezing_snowball_cannon", () -> new SnowballCannonItem(1));
    public static final RegistryObject<Item> SUPER_POWER_CORE = ITEMS.register("super_power_core", SuperFrozenCoreItem::new);
    public static final RegistryObject<Item> SUPER_FROZEN_CORE = ITEMS.register("super_frozen_core", SuperPowerCoreItem::new);
    public static final RegistryObject<Item> GLOVE = ITEMS.register("glove", GloveItem::new);
    public static final RegistryObject<Item> POPSICLE = ITEMS.register("popsicle", PopsicleItem::new);
    public static final RegistryObject<Item> MILK_POPSICLE = ITEMS.register("milk_popsicle", MilkPopsicleItem::new);
    public static final RegistryObject<Item> SNOWBALL_MACHINE_GUN = ITEMS.register("snowball_machine_gun", SnowballMachineGunItem::new);
    public static final RegistryObject<Item> EMPTY_SNOWBALL_STORAGE_TANK = ITEMS.register("empty_snowball_storage_tank", EmptySnowballStorageTankItem::new);
    public static final RegistryObject<Item> COMPACTED_SNOWBALL_STORAGE_TANK = ITEMS.register("compacted_snowball_storage_tank", () -> new SnowballStorageTankItem(SnowballType.COMPACTED));
    public static final RegistryObject<Item> STONE_SNOWBALL_STORAGE_TANK = ITEMS.register("stone_snowball_storage_tank", () -> new SnowballStorageTankItem(SnowballType.STONE));
    public static final RegistryObject<Item> GLASS_SNOWBALL_STORAGE_TANK = ITEMS.register("glass_snowball_storage_tank", () -> new SnowballStorageTankItem(SnowballType.GLASS));
    public static final RegistryObject<Item> ICE_SNOWBALL_STORAGE_TANK = ITEMS.register("ice_snowball_storage_tank", () -> new SnowballStorageTankItem(SnowballType.ICE));
    public static final RegistryObject<Item> IRON_SNOWBALL_STORAGE_TANK = ITEMS.register("iron_snowball_storage_tank", () -> new SnowballStorageTankItem(SnowballType.IRON));
    public static final RegistryObject<Item> GOLD_SNOWBALL_STORAGE_TANK = ITEMS.register("gold_snowball_storage_tank", () -> new SnowballStorageTankItem(SnowballType.GOLD));
    public static final RegistryObject<Item> OBSIDIAN_SNOWBALL_STORAGE_TANK = ITEMS.register("obsidian_snowball_storage_tank", () -> new SnowballStorageTankItem(SnowballType.OBSIDIAN));
    public static final RegistryObject<Item> EXPLOSIVE_SNOWBALL_STORAGE_TANK = ITEMS.register("explosive_snowball_storage_tank", () -> new SnowballStorageTankItem(SnowballType.EXPLOSIVE));
    public static final Group group = new Group();
}
