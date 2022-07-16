package com.linngdu664.bsf;

import com.linngdu664.bsf.item.setter.ItemRegister;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("bsf")
public class Register {
    public Register(){
        //获取mod总线
        ItemRegister.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
