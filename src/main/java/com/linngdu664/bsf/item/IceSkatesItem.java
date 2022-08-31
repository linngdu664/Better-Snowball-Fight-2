package com.linngdu664.bsf.item;

import com.linngdu664.bsf.client.model.IceSkatesModel;
import com.linngdu664.bsf.item.setter.ItemRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.client.IItemRenderProperties;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

public class IceSkatesItem extends ArmorItem {
    public IceSkatesItem() {
        super(new ArmorMaterial() {
            @Override
            public int getDurabilityForSlot(EquipmentSlot pSlot) {
                return 256;
            }

            @Override
            public int getDefenseForSlot(EquipmentSlot pSlot) {
                return 1;
            }

            @Override
            public int getEnchantmentValue() {
                return 0;
            }

            @Override
            public SoundEvent getEquipSound() {
                return SoundEvents.ARMOR_EQUIP_LEATHER;
            }

            @Override
            public Ingredient getRepairIngredient() {
                return null;
            }

            @Override
            public String getName() {
                return "skates";
            }

            @Override
            public float getToughness() {
                return 0;
            }

            @Override
            public float getKnockbackResistance() {
                return 0;
            }

        }, EquipmentSlot.FEET, new Properties().tab(ItemRegister.GROUP).stacksTo(1));

    }
    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return "bsf:textures/models/armor/ice_skates_layer.png";
    }
    public void initializeClient(Consumer<IItemRenderProperties> consumer){
        consumer.accept(new IItemRenderProperties() {
            @Nullable
            @Override
            public HumanoidModel<?> getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> _default) {
                HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(), Map.of(
                        "left_leg",new IceSkatesModel(Minecraft.getInstance().getEntityModels().bakeLayer(IceSkatesModel.LAYER_LOCATION)).bone,
                        "right_leg",new IceSkatesModel(Minecraft.getInstance().getEntityModels().bakeLayer(IceSkatesModel.LAYER_LOCATION)).bone,
                        "head", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                        "hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                        "body", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                        "right_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                        "left_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
                armorModel.crouching = entityLiving.isShiftKeyDown();
                armorModel.riding = _default.riding;
                armorModel.young = entityLiving.isBaby();
                return armorModel;
            }
        });
    }
}
