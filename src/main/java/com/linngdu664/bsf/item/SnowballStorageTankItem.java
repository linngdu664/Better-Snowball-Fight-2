package com.linngdu664.bsf.item;

import com.linngdu664.bsf.entity.SnowballType;
import com.linngdu664.bsf.item.setter.ItemRegister;
import com.linngdu664.bsf.item.setter.ModGroup;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SnowballStorageTankItem extends Item {
    public SnowballType type;

    public SnowballStorageTankItem(SnowballType type) {
        super(new Properties().tab(ModGroup.group).stacksTo(1).durability(96));
        this.type = type;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        if (pPlayer.getOffhandItem().isEmpty()) {
            if (pPlayer.isShiftKeyDown() || itemStack.getDamageValue() >= 80) {
                switch (type) {
                    case COMPACTED -> pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.COMPACTED_SNOWBALL.get(), 96 - itemStack.getDamageValue()), true);
                    case STONE -> pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.STONE_SNOWBALL.get(), 96 - itemStack.getDamageValue()), true);
                    case GLASS -> pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.GLASS_SNOWBALL.get(), 96 - itemStack.getDamageValue()), true);
                    case ICE -> pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.ICE_SNOWBALL.get(), 96 - itemStack.getDamageValue()), true);
                    case IRON -> pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.IRON_SNOWBALL.get(), 96 - itemStack.getDamageValue()), true);
                    case GOLD -> pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.GOLD_SNOWBALL.get(), 96 - itemStack.getDamageValue()), true);
                    case OBSIDIAN -> pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.OBSIDIAN_SNOWBALL.get(), 96 - itemStack.getDamageValue()), true);
                    case EXPLOSIVE -> pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.EXPLOSIVE_SNOWBALL.get(), 96 - itemStack.getDamageValue()), true);
                }
                if (!pPlayer.getAbilities().instabuild) {
                    pPlayer.setItemInHand(pUsedHand, new ItemStack(ItemRegister.EMPTY_SNOWBALL_STORAGE_TANK.get()));
                }
            } else {
                switch (type) {
                    case COMPACTED -> pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.COMPACTED_SNOWBALL.get(), 16), true);
                    case STONE -> pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.STONE_SNOWBALL.get(), 16), true);
                    case GLASS -> pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.GLASS_SNOWBALL.get(), 16), true);
                    case ICE -> pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.ICE_SNOWBALL.get(), 16), true);
                    case IRON -> pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.IRON_SNOWBALL.get(), 16), true);
                    case GOLD -> pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.GOLD_SNOWBALL.get(), 16), true);
                    case OBSIDIAN -> pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.OBSIDIAN_SNOWBALL.get(), 16), true);
                    case EXPLOSIVE -> pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.EXPLOSIVE_SNOWBALL.get(), 16), true);
                }
                itemStack.hurt(16, pLevel.getRandom(), null);
            }
            pPlayer.awardStat(Stats.ITEM_USED.get(this));
        }
        return InteractionResultHolder.sidedSuccess(itemStack, pLevel.isClientSide());
    }
}
