package com.linngdu664.bsf.entity.renderer;

import com.linngdu664.bsf.entity.BSFSnowGolemEntity;
import com.linngdu664.bsf.entity.model.BSFSnowGolemModel;
import net.minecraft.client.model.SnowGolemModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

//@OnlyIn(Dist.CLIENT)
public class BSFSnowGolemRenderer extends MobRenderer<BSFSnowGolemEntity, BSFSnowGolemModel<BSFSnowGolemEntity>> {
    public BSFSnowGolemRenderer(EntityRendererProvider.Context context) {
        // TODO: create model and texture for snow golem
        super(context,new BSFSnowGolemModel(context.bakeLayer(BSFSnowGolemModel.LAYER_LOCATION)),0.7f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull BSFSnowGolemEntity pEntity) {
        return new ResourceLocation("bsf:textures/models/bsf_snow_golem.png");
    }
}
