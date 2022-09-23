package com.linngdu664.bsf.entity.model;

import com.linngdu664.bsf.entity.BSFSnowGolemEntity;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;

public class BSFSnowGolemModel <T extends BSFSnowGolemEntity> extends QuadrupedModel<T> {
    public BSFSnowGolemModel(ModelPart modelPart) {
        super(modelPart, false, 10.0F, 4.0F, 2.0F, 2.0F, 24);
    }
}
