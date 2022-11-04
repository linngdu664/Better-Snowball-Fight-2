package com.linngdu664.bsf.item.snowball.special;

import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.item.snowball.AbstractBSFSnowballItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class PropulsionSnowball extends AbstractBSFSnowballItem {
    public PropulsionSnowball() {
        super(Rarity.COMMON);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        storageInTank(pPlayer, itemStack, ItemRegister.PROPULSION_SNOWBALL_TANK.get());
        return InteractionResultHolder.sidedSuccess(itemStack, pLevel.isClientSide());
    }
}
