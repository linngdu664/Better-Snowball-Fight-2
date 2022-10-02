package com.linngdu664.bsf.client.renderer.entity;

import com.linngdu664.bsf.client.model.BSFSnowGolemHoldItemLayer;
import com.linngdu664.bsf.client.model.BSFSnowGolemModel;
import com.linngdu664.bsf.entity.BSFSnowGolemEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

//@OnlyIn(Dist.CLIENT)
public class BSFSnowGolemRenderer extends MobRenderer<BSFSnowGolemEntity, BSFSnowGolemModel<BSFSnowGolemEntity>> {
    public BSFSnowGolemRenderer(EntityRendererProvider.Context context) {
        // TODO: create model and texture for snow golem
        super(context, new BSFSnowGolemModel(context.bakeLayer(BSFSnowGolemModel.LAYER_LOCATION)), 0.7f);
        this.addLayer(new BSFSnowGolemHoldItemLayer(this));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull BSFSnowGolemEntity pEntity) {
        return new ResourceLocation("bsf:textures/models/bsf_snow_golem.png");
    }
}
