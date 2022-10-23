package com.linngdu664.bsf.item.tool;

import com.linngdu664.bsf.util.ItemGroup;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CreativeSnowGolemToolItem extends Item {
    public CreativeSnowGolemToolItem() {
        super(new Properties().tab(ItemGroup.MAIN).rarity(Rarity.EPIC).stacksTo(1));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TranslatableComponent("creative_snow_golem_tool.tooltip").withStyle(ChatFormatting.BLUE));
    }
}
