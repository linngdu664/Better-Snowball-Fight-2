package com.linngdu664.bsf;

import com.linngdu664.bsf.item.setter.ItemRegister;
import com.linngdu664.bsf.sound.SoundRegister;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("bsf")
public class Register {
    public Register() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemRegister.ITEMS.register(bus);
        SoundRegister.SOUNDS.register(bus);
    }
}
