package com.linngdu664.bsf.entity.renderer;

import com.linngdu664.bsf.entity.BSFSnowGolemEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class BSFSnowGolemRenderer extends MobRenderer<BSFSnowGolemEntity, HumanoidModel<BSFSnowGolemEntity>> {
    public BSFSnowGolemRenderer(EntityRendererProvider.Context context) {
        // TODO: create model and texture for snow golem
        super(context, new HumanoidModel<>(context.bakeLayer(null)), 0.5f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull BSFSnowGolemEntity pEntity) {
        return new ResourceLocation("bsf", "bsf_snow_golem");
    }
}
