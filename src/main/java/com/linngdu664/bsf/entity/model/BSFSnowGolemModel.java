package com.linngdu664.bsf.entity.model;// Made with Blockbench 4.4.1
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.linngdu664.bsf.entity.BSFSnowGolemEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class BSFSnowGolemModel<T extends BSFSnowGolemEntity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("bsf", "bsf_snow_golem"), "main");
	private final ModelPart middle;
	private final ModelPart up;
	private final ModelPart bb_main;
	private final ModelPart left_arm_r1;
	private final ModelPart right_arm_r1;

	public BSFSnowGolemModel(ModelPart root) {
		this.middle = root.getChild("middle");
		this.left_arm_r1 = root.getChild("middle").getChild("left_arm_r1");
		this.right_arm_r1 = root.getChild("middle").getChild("right_arm_r1");
		this.up = root.getChild("up");
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition middle = partdefinition.addOrReplaceChild("middle", CubeListBuilder.create().texOffs(0, 16).addBox(-5.0F, -22.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition left_arm_r1 = middle.addOrReplaceChild("left_arm_r1", CubeListBuilder.create().texOffs(32, 0).addBox(1.0F, 0.0F, 0.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(4.0F, -21.0F, -1.0F, 0.0F, 0.0F, 1.0472F));

		PartDefinition right_arm_r1 = middle.addOrReplaceChild("right_arm_r1", CubeListBuilder.create().texOffs(32, 0).addBox(-13.0F, 0.0F, 0.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F)), PartPose.offsetAndRotation(-4.0F, -21.0F, -1.0F, 0.0F, 0.0F, -1.0472F));

		PartDefinition up = partdefinition.addOrReplaceChild("up", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.5F)), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 36).addBox(-6.0F, -12.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float degToRad=((float)Math.PI / 180F);
		this.up.yRot = netHeadYaw * degToRad;
		this.up.xRot = headPitch * degToRad;
		this.middle.yRot = netHeadYaw * degToRad * 0.25F;
		if(entity.haveGun()){
			this.left_arm_r1.xRot = degToRad * (-103);
			this.left_arm_r1.yRot = degToRad * (-67);
			this.left_arm_r1.zRot = degToRad * 162;
			this.right_arm_r1.xRot = degToRad * (-124);
			this.right_arm_r1.yRot = degToRad * 81;
			this.right_arm_r1.zRot = degToRad * 176;
		}else {
			this.left_arm_r1.xRot = 0;
			this.left_arm_r1.yRot = 0;
			this.left_arm_r1.zRot = degToRad * 60;
			this.right_arm_r1.xRot = 0;
			this.right_arm_r1.yRot = 0;
			this.right_arm_r1.zRot = degToRad * -60;
		}
		System.out.println(entity);

//		System.out.println(entity.toString()+" "+limbSwing+" "+limbSwingAmount+" "+ageInTicks);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		middle.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		up.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}