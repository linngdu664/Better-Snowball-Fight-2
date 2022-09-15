package com.linngdu664.bsf.item.misc;

import com.linngdu664.bsf.util.ItemGroup;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class SnowBlockBlender extends Item {
    public SnowBlockBlender() {
        super(new Properties().stacksTo(1).durability(100).tab(ItemGroup.MAIN));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        pPlayer.startUsingItem(pUsedHand);
        return InteractionResultHolder.consume(itemStack);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onUseTick(@NotNull Level pLevel, @NotNull LivingEntity pLivingEntity, @NotNull ItemStack pStack, int pRemainingUseDuration) {
        Player player = (Player) pLivingEntity;
        BlockHitResult blockHitResult = getPlayerPOVHitResult(pLevel, player, ClipContext.Fluid.NONE);
        BlockPos blockPos = blockHitResult.getBlockPos();
        if (pLevel.getBlockState(blockPos).getBlock() == Blocks.SNOW_BLOCK && !pLevel.isClientSide && pRemainingUseDuration == 1) {
            pLevel.setBlockAndUpdate(blockPos, Blocks.POWDER_SNOW.defaultBlockState());
            if (!player.getAbilities().instabuild) {
                pStack.hurtAndBreak(1, player, (e) -> e.broadcastBreakEvent(player.getUsedItemHand()));
            }
            player.awardStat(Stats.ITEM_USED.get(this));
        }
    }

    @Override
    public int getUseDuration(@NotNull ItemStack pStack) {
        return 60;
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack pStack) {
        return UseAnim.BOW;
    }
}
