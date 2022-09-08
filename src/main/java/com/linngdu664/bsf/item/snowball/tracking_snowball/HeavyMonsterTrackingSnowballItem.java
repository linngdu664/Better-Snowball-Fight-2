package com.linngdu664.bsf.item.snowball.tracking_snowball;

import com.linngdu664.bsf.item.setter.ItemRegister;
import com.linngdu664.bsf.util.BSFUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HeavyMonsterTrackingSnowballItem extends Item {
    public HeavyMonsterTrackingSnowballItem() {
        super(new Properties().tab(ItemRegister.GROUP).stacksTo(16));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        if (pPlayer.isShiftKeyDown()) {
            ItemStack newStack = new ItemStack(ItemRegister.HEAVY_PLAYER_TRACKING_SNOWBALL.get(), itemStack.getCount());
            pPlayer.setItemInHand(pUsedHand, newStack);
        } else {
            BSFUtil.storageInTank(pPlayer, itemStack, ItemRegister.HEAVY_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get());
        }
        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(itemStack, pLevel.isClientSide());
    }
    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TranslatableComponent("lunch_no_hand.tooltip").withStyle(ChatFormatting.DARK_RED));
        pTooltipComponents.add(new TranslatableComponent("lunch_yes_cannon.tooltip").withStyle(ChatFormatting.DARK_GREEN));
        pTooltipComponents.add(new TranslatableComponent("lunch_no_machine_gun.tooltip").withStyle(ChatFormatting.DARK_RED));
        pTooltipComponents.add(new TranslatableComponent("lunch_yes_shotgun.tooltip").withStyle(ChatFormatting.DARK_GREEN));
        pTooltipComponents.add(new TranslatableComponent("can_change.tooltip").withStyle(ChatFormatting.BLUE));
        pTooltipComponents.add(new TranslatableComponent("HMT_snowball.tooltip").withStyle(ChatFormatting.GOLD));
    }
}
