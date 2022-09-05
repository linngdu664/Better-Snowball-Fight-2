package com.linngdu664.bsf.item.snowball;

import com.linngdu664.bsf.entity.AdvancedSnowballEntity;
import com.linngdu664.bsf.item.setter.ItemRegister;
import com.linngdu664.bsf.util.SnowballType;
import com.linngdu664.bsf.util.Target;
import com.linngdu664.bsf.util.TrackingSnowballMode;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ForceSnowballItem extends Item {
    private final Target target;
    private final TrackingSnowballMode mode;

    public ForceSnowballItem(Target target, TrackingSnowballMode mode) {
        super(new Properties().tab(ItemRegister.GROUP).stacksTo(16));
        this.target = target;
        this.mode = mode;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);

        //Putting snowballs into empty storage tanks
        if (pPlayer.getOffhandItem().getItem() == ItemRegister.EMPTY_SNOWBALL_STORAGE_TANK.get()) {
            if (target == Target.MONSTER) {
                if (mode == TrackingSnowballMode.GRAVITY) {
                    pPlayer.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(ItemRegister.GRAVITY_SNOWBALL_TO_MONSTER.get()));
                } else if (mode == TrackingSnowballMode.REPULSION) {
                    pPlayer.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(ItemRegister.REPULSION_SNOWBALL_TO_MONSTER.get()));
                }
            } else if (target == Target.PROJECTILE) {
                if (mode == TrackingSnowballMode.GRAVITY) {
                    pPlayer.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(ItemRegister.GRAVITY_SNOWBALL_TO_PROJECTILE.get()));
                } else if (mode == TrackingSnowballMode.REPULSION) {
                    pPlayer.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(ItemRegister.REPULSION_SNOWBALL_TO_PROJECTILE.get()));
                }
            } else if (target == Target.MIX) {
                pPlayer.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(ItemRegister.BLACK_HOLE_SNOWBALL.get()));
            }
            pPlayer.getOffhandItem().setDamageValue(96 - pPlayer.getMainHandItem().getCount());
            if (!pPlayer.getAbilities().instabuild) {
                itemStack.shrink(pPlayer.getMainHandItem().getCount());
            }
        }
        //Putting snowballs into corresponding storage tanks
        else if ((pPlayer.getOffhandItem().getItem() == ItemRegister.GRAVITY_SNOWBALL_TO_MONSTER_STORAGE_TANK.get() && target == Target.MONSTER && mode == TrackingSnowballMode.GRAVITY ||
                pPlayer.getOffhandItem().getItem() == ItemRegister.REPULSION_SNOWBALL_TO_MONSTER_STORAGE_TANK.get() && target == Target.MONSTER && mode == TrackingSnowballMode.REPULSION ||
                pPlayer.getOffhandItem().getItem() == ItemRegister.GRAVITY_SNOWBALL_TO_PROJECTILE_STORAGE_TANK.get() && target == Target.PROJECTILE && mode == TrackingSnowballMode.GRAVITY ||
                pPlayer.getOffhandItem().getItem() == ItemRegister.REPULSION_SNOWBALL_TO_PROJECTILE_STORAGE_TANK.get() && target == Target.PROJECTILE && mode == TrackingSnowballMode.REPULSION ||
                pPlayer.getOffhandItem().getItem() == ItemRegister.BLACK_HOLE_SNOWBALL_STORAGE_TANK.get() && target == Target.MIX && mode == TrackingSnowballMode.BLACK_HOLE)
                && pPlayer.getOffhandItem().getDamageValue() != 0) {
            if (pPlayer.getOffhandItem().getDamageValue() >= pPlayer.getMainHandItem().getCount()) {
                pPlayer.getOffhandItem().setDamageValue(pPlayer.getOffhandItem().getDamageValue() - pPlayer.getMainHandItem().getCount());
                if (!pPlayer.getAbilities().instabuild) {
                    itemStack.shrink(pPlayer.getMainHandItem().getCount());
                }
            } else {
                if (!pPlayer.getAbilities().instabuild) {
                    itemStack.shrink(pPlayer.getOffhandItem().getDamageValue());
                }
                pPlayer.getOffhandItem().setDamageValue(0);
            }
        }else {
            //todo this facking shit unfinished
            pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
            if (!pLevel.isClientSide) {
                float i = pPlayer.hasEffect(MobEffects.WEAKNESS) ? 0.75F : 1.0F;
                float j = pPlayer.hasEffect(MobEffects.WEAKNESS) ? 0.5F : 1.0F;
                AdvancedSnowballEntity snowballEntity = new AdvancedSnowballEntity(pLevel, pPlayer, SnowballType.ICE, 3.0F * j, 6.0F * j);
                snowballEntity.setItem(itemStack);
                snowballEntity.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 1.125F * i, 1.0F);
                snowballEntity.frozenTicks = 60;



                pLevel.addFreshEntity(snowballEntity);
            }
            if (!pPlayer.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
        }


        pPlayer.awardStat(Stats.ITEM_USED.get(this));//Feedback effect
        return InteractionResultHolder.sidedSuccess(itemStack, pLevel.isClientSide());
    }

}
