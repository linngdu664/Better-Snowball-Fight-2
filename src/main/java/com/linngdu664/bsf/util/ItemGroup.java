package com.linngdu664.bsf.util;

import com.linngdu664.bsf.item.setter.ItemRegister;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemGroup {
    public static CreativeModeTab MAIN = new CreativeModeTab("bsf_group") {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(ItemRegister.EXPLOSIVE_SNOWBALL.get());
        }
    };
}
