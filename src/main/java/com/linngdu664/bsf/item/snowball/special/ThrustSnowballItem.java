package com.linngdu664.bsf.item.snowball.special;

import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.item.snowball.AbstractBSFSnowballItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ThrustSnowballItem extends AbstractBSFSnowballItem {
    public ThrustSnowballItem() {
        super(Rarity.COMMON);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        storageInTank(pPlayer, itemStack, ItemRegister.THRUST_SNOWBALL_TANK.get());
        return InteractionResultHolder.sidedSuccess(itemStack, pLevel.isClientSide());
    }

    @Override
    public boolean canBeLaunchedByMachineGun() {
        return false;
    }

    @Override
    public boolean canBeLaunchedByNormalWeapon() {
        return false;
    }

    @Override
    public double getPushRank() {
        return 0.38;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TranslatableComponent("lunch_no_hand.tooltip").withStyle(ChatFormatting.DARK_RED));
        pTooltipComponents.add(new TranslatableComponent("lunch_no_cannon.tooltip").withStyle(ChatFormatting.DARK_RED));
        pTooltipComponents.add(new TranslatableComponent("lunch_no_machine_gun.tooltip").withStyle(ChatFormatting.DARK_RED));
        pTooltipComponents.add(new TranslatableComponent("lunch_no_shotgun.tooltip").withStyle(ChatFormatting.DARK_RED));
        pTooltipComponents.add(new TranslatableComponent("thrust_snowball1.tooltip").withStyle(ChatFormatting.BLUE));
        pTooltipComponents.add(new TranslatableComponent("thrust_snowball2.tooltip").withStyle(ChatFormatting.BLUE));
        pTooltipComponents.add(new TranslatableComponent("thrust_snowball.tooltip").withStyle(ChatFormatting.DARK_AQUA));
    }
}
