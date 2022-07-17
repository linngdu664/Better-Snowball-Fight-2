package com.linngdu664.bsf.item;

import com.linngdu664.bsf.item.setter.ModGroup;
import net.minecraft.world.item.Item;

public class SnowballStorageTankItem extends Item {
    public int type;
    public SnowballStorageTankItem(int type) {
        super(new Properties().tab(ModGroup.group).stacksTo(1).durability(96));
        this.type = type;
    }
}
