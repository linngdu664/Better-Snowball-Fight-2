package com.linngdu664.bsf.item;

import com.linngdu664.bsf.item.setter.ModGroup;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class PopsicleItem extends Item {
    private static final FoodProperties food=new FoodProperties.Builder().build();
    public PopsicleItem() {
        super(new Properties().food(food).tab(ModGroup.group));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack p_41452_) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack p_41454_) {
        return 64;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        if(!level.isClientSide){
            user.setSecondsOnFire(0);
            user.setTicksFrozen(40);
            user.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,40,1));
        }
        return stack;
    }
}
