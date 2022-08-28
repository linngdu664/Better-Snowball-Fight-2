package com.linngdu664.bsf.item;

import com.linngdu664.bsf.entity.AdvancedSnowballEntity;
import com.linngdu664.bsf.util.SnowballType;
import com.linngdu664.bsf.item.setter.ItemRegister;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CompactedSnowballSetItem extends Item {
    public CompactedSnowballSetItem() {
        super(new Properties().tab(ItemRegister.group).stacksTo(16));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!pLevel.isClientSide) {
            float i = pPlayer.hasEffect(MobEffects.WEAKNESS) ? 0.75F : 1.0F;
            AdvancedSnowballEntity snowballEntity1 = new AdvancedSnowballEntity(pLevel, pPlayer, SnowballType.COMPACTED);
            AdvancedSnowballEntity snowballEntity2 = new AdvancedSnowballEntity(pLevel, pPlayer, SnowballType.COMPACTED);
            AdvancedSnowballEntity snowballEntity3 = new AdvancedSnowballEntity(pLevel, pPlayer, SnowballType.COMPACTED);
            snowballEntity1.setItem(new ItemStack(ItemRegister.COMPACTED_SNOWBALL.get()));
            snowballEntity2.setItem(new ItemStack(ItemRegister.COMPACTED_SNOWBALL.get()));
            snowballEntity3.setItem(new ItemStack(ItemRegister.COMPACTED_SNOWBALL.get()));
            snowballEntity1.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, i, 10.0F);
            snowballEntity2.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, i, 10.0F);
            snowballEntity3.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, i, 10.0F);
            pLevel.addFreshEntity(snowballEntity1);
            pLevel.addFreshEntity(snowballEntity2);
            pLevel.addFreshEntity(snowballEntity3);
        }
        if (!pPlayer.getAbilities().instabuild) {
            itemStack.shrink(1);
        }
        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(itemStack, pLevel.isClientSide());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TranslatableComponent("compacted_snowball_set.tooltip").withStyle(ChatFormatting.GRAY));
    }
}
