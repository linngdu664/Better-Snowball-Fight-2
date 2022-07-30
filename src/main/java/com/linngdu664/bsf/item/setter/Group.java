package com.linngdu664.bsf.item.setter;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Group extends CreativeModeTab {
    public Group() {
        super("bsf_group");
    }

    @Override
    public @NotNull ItemStack makeIcon() {
        return new ItemStack(ItemRegister.EXPLOSIVE_SNOWBALL.get());
    }

    @Override
    public @NotNull Component getDisplayName() {
        return new TranslatableComponent("itemGroup.bsf.bsf_group");
    }
}
