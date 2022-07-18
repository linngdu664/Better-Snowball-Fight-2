package com.linngdu664.bsf;

import com.linngdu664.bsf.item.setter.ItemRegister;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("bsf")
public class Register {
    public Register() {
        ItemRegister.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
