package com.linngdu664.bsf.item.misc;

import com.linngdu664.bsf.item.setter.ItemRegister;
import com.linngdu664.bsf.util.SnowballType;
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
    public SnowballType type;

    public SnowballStorageTankItem(SnowballType type) {
        super(new Properties().tab(ItemRegister.GROUP).stacksTo(1).durability(96));
        this.type = type;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
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
                    case SPECTRAL -> pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.SPECTRAL_SNOWBALL.get(), 96 - itemStack.getDamageValue()), true);
                    case TRACKING_MONSTER -> pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.MONSTER_TRACKING_SNOWBALL.get(), 96 - itemStack.getDamageValue()), true);
                    case TRACKING_MONSTER_DAMAGE -> pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.MONSTER_TRACKING_SNOWBALL_WITH_DAMAGE.get(), 96 - itemStack.getDamageValue()), true);
                    case TRACKING_MONSTER_EXPLOSIVE -> pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.EXPLOSIVE_MONSTER_TRACKING_SNOWBALL.get(), 96 - itemStack.getDamageValue()), true);
                    case TRACKING_PLAYER -> pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.PLAYER_TRACKING_SNOWBALL.get(), 96 - itemStack.getDamageValue()), true);
                    case TRACKING_PLAYER_DAMAGE -> pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.PLAYER_TRACKING_SNOWBALL_WITH_DAMAGE.get(), 96 - itemStack.getDamageValue()), true);
                    case TRACKING_PLAYER_EXPLOSIVE -> pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.EXPLOSIVE_PLAYER_TRACKING_SNOWBALL.get(), 96 - itemStack.getDamageValue()), true);
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
                    case SPECTRAL -> pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.SPECTRAL_SNOWBALL.get(), 16), true);
                    case TRACKING_MONSTER -> pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.MONSTER_TRACKING_SNOWBALL.get(), 16), true);
                    case TRACKING_MONSTER_DAMAGE -> pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.MONSTER_TRACKING_SNOWBALL_WITH_DAMAGE.get(), 16), true);
                    case TRACKING_MONSTER_EXPLOSIVE -> pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.EXPLOSIVE_MONSTER_TRACKING_SNOWBALL.get(), 16), true);
                    case TRACKING_PLAYER -> pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.PLAYER_TRACKING_SNOWBALL.get(), 16), true);
                    case TRACKING_PLAYER_DAMAGE -> pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.PLAYER_TRACKING_SNOWBALL_WITH_DAMAGE.get(), 16), true);
                    case TRACKING_PLAYER_EXPLOSIVE -> pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.EXPLOSIVE_PLAYER_TRACKING_SNOWBALL.get(), 16), true);
                }
                //todo: What the fuck? Check creative mode?
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
