package com.linngdu664.bsf.item.misc;

import com.linngdu664.bsf.item.setter.ItemRegister;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GloveItem extends ShieldItem {
    public GloveItem() {
        super(new Properties().tab(ItemRegister.GROUP).durability(128));
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack pStack) {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack pToRepair, ItemStack pRepair) {
        return pRepair.is(ItemTags.WOOL);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TranslatableComponent("glove.tooltip").withStyle(ChatFormatting.DARK_GREEN));
    }
}
