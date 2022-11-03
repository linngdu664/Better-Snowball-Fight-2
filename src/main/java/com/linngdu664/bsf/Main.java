package com.linngdu664.bsf;

import com.linngdu664.bsf.block.BlockRegister;
import com.linngdu664.bsf.effect.EffectRegister;
import com.linngdu664.bsf.enchantment.BSFEnchantments;
import com.linngdu664.bsf.entity.EntityRegister;
import com.linngdu664.bsf.event.AttackEntityEvent;
import com.linngdu664.bsf.event.LivingFallEvent;
import com.linngdu664.bsf.event.OnPlayerTickEvent;
import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.particle.ParticleRegister;
import com.linngdu664.bsf.util.SoundRegister;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;


@Mod("bsf")
public class Main {
    //    public static final Logger LOGGER = LogManager.getLogger(Main.class);
    public static final String MODID = "bsf";
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, "main"), () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    private static int messageID = 0;

    public Main() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        BlockRegister.BLOCKS.register(bus);
        ItemRegister.ITEMS.register(bus);
        SoundRegister.SOUNDS.register(bus);
        ParticleRegister.PARTICLES.register(bus);
        EffectRegister.EFFECTS.register(bus);
        EntityRegister.ENTITY_TYPES.register(bus);
        MinecraftForge.EVENT_BUS.register(new AttackEntityEvent());
        MinecraftForge.EVENT_BUS.register(new OnPlayerTickEvent());
        MinecraftForge.EVENT_BUS.register(new LivingFallEvent());
//        BSFEnchantments.REGISTRY.register(bus);
    }

    public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder,
                                             BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        PACKET_HANDLER.registerMessage(messageID++, messageType, encoder, decoder, messageConsumer);
    }
}
