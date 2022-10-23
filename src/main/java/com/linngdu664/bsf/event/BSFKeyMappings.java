package com.linngdu664.bsf.event;

import com.linngdu664.bsf.Main;
import com.linngdu664.bsf.network.CycleMoveAmmoRight;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class BSFKeyMappings {
    public static final KeyMapping CYCLE_MOVE_AMMO_RIGHT = new KeyMapping("key.testmod.cycle_move_ammo_right", GLFW.GLFW_KEY_G, "key.categories.misc");


    @SubscribeEvent
    public static void registerKeyBindings(FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(CYCLE_MOVE_AMMO_RIGHT);
    }

    @Mod.EventBusSubscriber({Dist.CLIENT})
    public static class KeyEventListener {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.KeyInputEvent event) {
            if (Minecraft.getInstance().screen == null) {
                if (event.getKey() == CYCLE_MOVE_AMMO_RIGHT.getKey().getValue()) {
                    if (event.getAction() == GLFW.GLFW_PRESS) {
                        Main.PACKET_HANDLER.sendToServer(new CycleMoveAmmoRight(0, 0));
                        CycleMoveAmmoRight.pressAction(Minecraft.getInstance().player, 0, 0);
                    }
                }
            }
        }
    }

}
