package com.linngdu664.bsf.item;

import com.linngdu664.bsf.item.setter.ItemRegister;
import net.minecraft.world.item.Item;

public class EmptySnowballStorageTankItem extends Item {
    public EmptySnowballStorageTankItem() {
        super(new Properties().tab(ItemRegister.GROUP).stacksTo(1));
    }
}
