package com.linngdu664.bsf.item.misc;

import com.linngdu664.bsf.util.ItemGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class EmptySnowballStorageTankItem extends Item {
    public EmptySnowballStorageTankItem() {
        super(new Properties().tab(ItemGroup.MAIN).stacksTo(1).rarity(Rarity.UNCOMMON));
    }
}
