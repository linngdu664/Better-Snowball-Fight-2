package com.linngdu664.bsf;

import com.linngdu664.bsf.effect.EffectRegister;
import com.linngdu664.bsf.entity.EntityRegister;
import com.linngdu664.bsf.event.AttackEntityEvent;
import com.linngdu664.bsf.event.OnPlayerTickEvent;
import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.particle.ParticleRegister;
import com.linngdu664.bsf.util.SoundRegister;
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
        ParticleRegister.PARTICLES.register(bus);
        EffectRegister.EFFECTS.register(bus);
        EntityRegister.ENTITY_TYPES.register(bus);
        MinecraftForge.EVENT_BUS.register(new AttackEntityEvent());
        MinecraftForge.EVENT_BUS.register(new OnPlayerTickEvent());
    }
}
