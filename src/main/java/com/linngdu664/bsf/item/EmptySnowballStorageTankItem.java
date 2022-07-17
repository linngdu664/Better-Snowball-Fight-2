package com.linngdu664.bsf.item;

import com.linngdu664.bsf.item.setter.ModGroup;
import net.minecraft.world.item.Item;

public class EmptySnowballStorageTankItem extends Item {
    public EmptySnowballStorageTankItem() {
        super(new Properties().tab(ModGroup.group).stacksTo(1));
    }
}
