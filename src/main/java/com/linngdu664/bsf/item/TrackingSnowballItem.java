package com.linngdu664.bsf.item;

import com.linngdu664.bsf.item.setter.ItemRegister;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class TrackingSnowballItem extends Item {
    private final Class<? extends Entity> targetClass;
    private final int damageMode;

    public TrackingSnowballItem(Class <? extends Entity> targetClass, int damageMode) {
        super(new Properties().tab(ItemRegister.GROUP).stacksTo(16));
        this.targetClass = targetClass;
        this.damageMode = damageMode;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        if (pPlayer.getOffhandItem().getItem() == ItemRegister.EMPTY_SNOWBALL_STORAGE_TANK.get()) {
            if (targetClass == Player.class) {
                switch (damageMode) {
                    case 0 -> pPlayer.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(ItemRegister.PLAYER_TRACKING_SNOWBALL.get()));
                    case 1 -> pPlayer.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(ItemRegister.PLAYER_TRACKING_SNOWBALL_WITH_DAMAGE.get()));
                    case 2 -> pPlayer.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(ItemRegister.EXPLOSIVE_PLAYER_TRACKING_SNOWBALL.get()));
                }
            } else {
                switch (damageMode) {
                    case 0 -> pPlayer.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(ItemRegister.MONSTER_TRACKING_SNOWBALL.get()));
                    case 1 -> pPlayer.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(ItemRegister.MONSTER_TRACKING_SNOWBALL_WITH_DAMAGE.get()));
                    case 2 -> pPlayer.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(ItemRegister.EXPLOSIVE_MONSTER_TRACKING_SNOWBALL.get()));
                }
            }
            pPlayer.getOffhandItem().setDamageValue(96 - pPlayer.getMainHandItem().getCount());
            if (!pPlayer.getAbilities().instabuild) {
                itemStack.shrink(pPlayer.getMainHandItem().getCount());
            }
        } else if ((pPlayer.getOffhandItem().getItem() == ItemRegister.PLAYER_TRACKING_SNOWBALL_STORAGE_TANK.get() && targetClass == Player.class && damageMode == 0 ||
                pPlayer.getOffhandItem().getItem() == ItemRegister.PLAYER_TRACKING_SNOWBALL_WITH_DAMAGE_STORAGE_TANK.get() && targetClass == Player.class && damageMode == 1 ||
                pPlayer.getOffhandItem().getItem() == ItemRegister.EXPLOSIVE_PLAYER_TRACKING_SNOWBALL_STORAGE_TANK.get() && targetClass == Player.class && damageMode == 2 ||
                pPlayer.getOffhandItem().getItem() == ItemRegister.MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get() && targetClass == Monster.class && damageMode == 0 ||
                pPlayer.getOffhandItem().getItem() == ItemRegister.MONSTER_TRACKING_SNOWBALL_WITH_DAMAGE_STORAGE_TANK.get() && targetClass == Monster.class && damageMode == 1 ||
                pPlayer.getOffhandItem().getItem() == ItemRegister.EXPLOSIVE_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get() && targetClass == Monster.class && damageMode == 2)
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
}
