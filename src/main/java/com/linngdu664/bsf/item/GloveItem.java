package com.linngdu664.bsf.item;

import com.linngdu664.bsf.item.setter.ModGroup;
import net.minecraft.world.item.ShieldItem;

public class GloveItem extends ShieldItem {
    public GloveItem() {
        super(new Properties().tab(ModGroup.group));
    }
}
