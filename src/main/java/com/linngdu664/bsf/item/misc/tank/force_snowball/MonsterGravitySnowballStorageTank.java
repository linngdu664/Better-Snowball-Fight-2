package com.linngdu664.bsf.item.misc.tank.force_snowball;

import com.linngdu664.bsf.entity.snowball.force_snowball.MonsterGravitySnowballEntity;
import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.item.misc.SnowballStorageTankItem;
import com.linngdu664.bsf.item.snowball.BSFSnowballItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MonsterGravitySnowballStorageTank extends SnowballStorageTankItem {
    public MonsterGravitySnowballStorageTank(Item item) {
        super(item);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TranslatableComponent("lunch_yes_hand.tooltip").withStyle(ChatFormatting.DARK_GREEN));
        pTooltipComponents.add(new TranslatableComponent("lunch_no_cannon.tooltip").withStyle(ChatFormatting.DARK_RED));
        pTooltipComponents.add(new TranslatableComponent("lunch_no_machine_gun.tooltip").withStyle(ChatFormatting.DARK_RED));
        pTooltipComponents.add(new TranslatableComponent("lunch_no_shotgun.tooltip").withStyle(ChatFormatting.DARK_RED));
        pTooltipComponents.add(new TranslatableComponent("can_change.tooltip").withStyle(ChatFormatting.BLUE));
        pTooltipComponents.add(new TranslatableComponent("monster_gravity_snowball.tooltip").withStyle(ChatFormatting.DARK_PURPLE));
    }
}
