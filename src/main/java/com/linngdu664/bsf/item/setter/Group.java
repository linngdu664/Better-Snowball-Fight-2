package com.linngdu664.bsf.item.setter;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class Group extends CreativeModeTab {
    public Group() {
        super("bsf_group");
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(ItemRegister.EXPLOSIVE_SNOWBALL.get());
    }

}
