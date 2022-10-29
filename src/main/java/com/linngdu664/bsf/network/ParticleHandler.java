package com.linngdu664.bsf.network;

import com.linngdu664.bsf.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ParticleHandler {
    double x, y, z, xSpeed, ySpeed, zSpeed;

    public ParticleHandler(double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.zSpeed = zSpeed;
    }

    public ParticleHandler(FriendlyByteBuf buffer) {
        this.x = buffer.readDouble();
        this.y = buffer.readDouble();
        this.z = buffer.readDouble();
        this.xSpeed = buffer.readDouble();
        this.ySpeed = buffer.readDouble();
        this.zSpeed = buffer.readDouble();
    }

    public static void buffer(ParticleHandler message, FriendlyByteBuf buffer) {
        buffer.writeDouble(message.x);
        buffer.writeDouble(message.y);
        buffer.writeDouble(message.z);
        buffer.writeDouble(message.xSpeed);
        buffer.writeDouble(message.ySpeed);
        buffer.writeDouble(message.zSpeed);
    }

    public static void handler(ParticleHandler message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> handlePacket(message.x, message.y, message.z, message.xSpeed, message.ySpeed, message.zSpeed)));
    }

    public static boolean handlePacket(double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        Player player = Minecraft.getInstance().player;
        Level level = player.getLevel();
        level.addParticle(ParticleTypes.SNOWFLAKE, x, y, z, xSpeed, ySpeed, zSpeed);
        return true;
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        Main.addNetworkMessage(ParticleHandler.class, ParticleHandler::buffer, ParticleHandler::new, ParticleHandler::handler);
    }
}
