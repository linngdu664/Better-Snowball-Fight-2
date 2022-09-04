package com.linngdu664.bsf.item.snowball;

import com.linngdu664.bsf.item.setter.ItemRegister;
import com.linngdu664.bsf.util.Target;
import com.linngdu664.bsf.util.TrackingSnowballMode;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class GravitySnowballItem extends Item {
    private final Target target;
    private final TrackingSnowballMode mode;

    public GravitySnowballItem(Target target, TrackingSnowballMode mode) {
        super(new Properties().tab(ItemRegister.GROUP).stacksTo(16));
        this.target = target;
        this.mode = mode;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        if (pPlayer.getOffhandItem().getItem() == ItemRegister.EMPTY_SNOWBALL_STORAGE_TANK.get()) {
            if (target == Target.MONSTER) {
                if(mode == TrackingSnowballMode.GRAVITY){
                    pPlayer.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(ItemRegister.GRAVITY_SNOWBALL_TO_MONSTER.get()));
                }else if(mode == TrackingSnowballMode.REPULSION) {
                    pPlayer.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(ItemRegister.REPULSION_SNOWBALL_TO_MONSTER.get()));
                }
            } else if(target == Target.PROJECTILE){
                if(mode == TrackingSnowballMode.GRAVITY){
                    pPlayer.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(ItemRegister.GRAVITY_SNOWBALL_TO_PROJECTILE.get()));
                }else if(mode == TrackingSnowballMode.REPULSION) {
                    pPlayer.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(ItemRegister.REPULSION_SNOWBALL_TO_PROJECTILE.get()));
                }
            }else if(target == Target.MIX){
                pPlayer.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(ItemRegister.BLACK_HOLE_SNOWBALL.get()));
            }
            pPlayer.getOffhandItem().setDamageValue(96 - pPlayer.getMainHandItem().getCount());
            if (!pPlayer.getAbilities().instabuild) {
                itemStack.shrink(pPlayer.getMainHandItem().getCount());
            }
        } else if ((pPlayer.getOffhandItem().getItem() == ItemRegister.GRAVITY_SNOWBALL_TO_MONSTER_STORAGE_TANK.get() && target == Target.MONSTER && mode == TrackingSnowballMode.GRAVITY ||
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
        }
        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(itemStack, pLevel.isClientSide());
    }
    //todo function of force effect

}
