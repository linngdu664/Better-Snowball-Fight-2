package com.linngdu664.bsf.event;

import com.linngdu664.bsf.Main;
import com.linngdu664.bsf.client.model.BSFSnowGolemModel;
import com.linngdu664.bsf.client.model.IceSkatesModel;
import com.linngdu664.bsf.client.model.SnowFallBootsModel;
import com.linngdu664.bsf.client.renderer.entity.BSFSnowGolemRenderer;
import com.linngdu664.bsf.entity.EntityRegister;
import com.linngdu664.bsf.item.ItemRegister;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(ItemRegister.SNOWBALL_CANNON.get(),
                    new ResourceLocation("pull"), (itemStack, world, livingEntity, num) -> {
                        if (livingEntity == null) {
                            return 0.0F;
                        } else {
                            return livingEntity.getUseItem() != itemStack ? 0.0F : (float) (itemStack.getUseDuration() - livingEntity.getUseItemRemainingTicks()) / 20.0F;
                        }
                    });
            ItemProperties.register(ItemRegister.SNOWBALL_CANNON.get(), new ResourceLocation("pulling"), (itemStack, world, livingEntity, num)
                    -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F);
            ItemProperties.register(ItemRegister.FREEZING_SNOWBALL_CANNON.get(),
                    new ResourceLocation("pull"), (itemStack, world, livingEntity, num) -> {
                        if (livingEntity == null) {
                            return 0.0F;
                        } else {
                            return livingEntity.getUseItem() != itemStack ? 0.0F : (float) (itemStack.getUseDuration() - livingEntity.getUseItemRemainingTicks()) / 20.0F;
                        }
                    });
            ItemProperties.register(ItemRegister.FREEZING_SNOWBALL_CANNON.get(), new ResourceLocation("pulling"), (itemStack, world, livingEntity, num)
                    -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F);
            ItemProperties.register(ItemRegister.POWERFUL_SNOWBALL_CANNON.get(),
                    new ResourceLocation("pull"), (itemStack, world, livingEntity, num) -> {
                        if (livingEntity == null) {
                            return 0.0F;
                        } else {
                            return livingEntity.getUseItem() != itemStack ? 0.0F : (float) (itemStack.getUseDuration() - livingEntity.getUseItemRemainingTicks()) / 20.0F;
                        }
                    });
            ItemProperties.register(ItemRegister.POWERFUL_SNOWBALL_CANNON.get(), new ResourceLocation("pulling"), (itemStack, world, livingEntity, num)
                    -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F);
            ItemProperties.register(ItemRegister.GLOVE.get(), new ResourceLocation("using"), (itemStack, world, livingEntity, num)
                    -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F);
        });
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegister.BSF_SNOW_GOLEM.get(), BSFSnowGolemRenderer::new);
        event.registerEntityRenderer(EntityRegister.SMOOTH_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.COMPACTED_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.STONE_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.ICE_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.GLASS_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.IRON_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.GOLD_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.OBSIDIAN_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.EXPLOSIVE_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.SPECTRAL_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.BLACK_HOLE_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.MONSTER_GRAVITY_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.MONSTER_REPULSION_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.PROJECTILE_GRAVITY_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.PROJECTILE_REPULSION_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.ENDER_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.FROZEN_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.POWDER_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.SUBSPACE_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.EXPLOSIVE_MONSTER_TRACKING_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.EXPLOSIVE_PLAYER_TRACKING_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.HEAVY_MONSTER_TRACKING_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.HEAVY_PLAYER_TRACKING_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.LIGHT_MONSTER_TRACKING_SNOWBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityRegister.LIGHT_PLAYER_TRACKING_SNOWBALL.get(), ThrownItemRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(IceSkatesModel.LAYER_LOCATION, IceSkatesModel::createBodyLayer);
        event.registerLayerDefinition(SnowFallBootsModel.LAYER_LOCATION, SnowFallBootsModel::createBodyLayer);
        event.registerLayerDefinition(BSFSnowGolemModel.LAYER_LOCATION, BSFSnowGolemModel::createBodyLayer);
    }
}
