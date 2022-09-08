package com.linngdu664.bsf.item.misc;

import com.linngdu664.bsf.item.setter.ItemRegister;
import com.linngdu664.bsf.util.ItemGroup;
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

public class SnowballStorageTankItem extends Item {
    public Item item;

    public SnowballStorageTankItem(Item item) {
        super(new Properties().tab(ItemGroup.MAIN).stacksTo(1).durability(96));
        this.item = item;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        if (pPlayer.getOffhandItem().isEmpty()) {
            if (pPlayer.isShiftKeyDown() || itemStack.getDamageValue() >= 80) {
                int k = 96 - itemStack.getDamageValue();
                for (int i = 0; i < k / 16; i++) {
                    pPlayer.getInventory().placeItemBackInInventory(new ItemStack(item, 16), true);
                }
                pPlayer.getInventory().placeItemBackInInventory(new ItemStack(item, k % 16), true);
                if (!pPlayer.getAbilities().instabuild) {
                    pPlayer.setItemInHand(pUsedHand, new ItemStack(ItemRegister.EMPTY_SNOWBALL_STORAGE_TANK.get()));
                }
            } else {
                pPlayer.getInventory().placeItemBackInInventory(new ItemStack(item, 16), true);
                itemStack.hurt(16, pLevel.getRandom(), null);
            }
            pPlayer.awardStat(Stats.ITEM_USED.get(this));
        }
        return InteractionResultHolder.sidedSuccess(itemStack, pLevel.isClientSide());
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TranslatableComponent("snowball_storage_tank.tooltip").withStyle(ChatFormatting.GRAY));
    }
}
