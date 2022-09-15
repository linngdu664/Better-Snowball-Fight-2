package com.linngdu664.bsf.item.misc;

import com.linngdu664.bsf.util.ItemGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class UnstableCoreItem extends Item {
    public UnstableCoreItem() {
        super(new Properties().tab(ItemGroup.MAIN).rarity(Rarity.UNCOMMON));
    }
}
