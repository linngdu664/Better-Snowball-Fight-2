package com.linngdu664.bsf;

import com.linngdu664.bsf.item.setter.ItemRegister;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("bsf")
public class Main {
    public Main() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemRegister.ITEMS.register(bus);
        SoundRegister.SOUNDS.register(bus);
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }
}
