package com.linngdu664.bsf.item.snowball.tracking_snowball;

import com.linngdu664.bsf.item.setter.ItemRegister;
import com.linngdu664.bsf.util.BSFUtil;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class LightPlayerTrackingSnowballItem extends Item {
    public LightPlayerTrackingSnowballItem() {
        super(new Item.Properties().tab(ItemRegister.GROUP).stacksTo(16));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        if (pPlayer.isShiftKeyDown()) {
            ItemStack newStack = new ItemStack(ItemRegister.LIGHT_MONSTER_TRACKING_SNOWBALL.get(), itemStack.getCount());
            pPlayer.setItemInHand(pUsedHand, newStack);
        } else {
            BSFUtil.storageInTank(pPlayer, itemStack, ItemRegister.LIGHT_PLAYER_TRACKING_SNOWBALL_STORAGE_TANK.get());
        }
        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(itemStack, pLevel.isClientSide());
    }
}
