package com.linngdu664.bsf.item.tool;

import com.linngdu664.bsf.util.BSFMthUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SnowBlockBlenderItem extends AbstractBSFEnhanceableToolItem {
    public SnowBlockBlenderItem() {
        super(Rarity.COMMON, 256);
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
        if (pLevel.getBlockState(blockPos).getBlock() == Blocks.SNOW_BLOCK && !pLevel.isClientSide) {
            ServerLevel serverLevel = (ServerLevel) pLevel;
            if (pRemainingUseDuration == 1) {
                pLevel.setBlockAndUpdate(blockPos, Blocks.POWDER_SNOW.defaultBlockState());
                for (int i = 0; i < 5; i++) {
                    serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, blockPos.getX(), blockPos.getY() + BSFMthUtil.randDouble(0, 1), blockPos.getZ() + BSFMthUtil.randDouble(0, 1), 5, 0, 0, 0, 0.1);
                    serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, blockPos.getX() + 1, blockPos.getY() + BSFMthUtil.randDouble(0, 1), blockPos.getZ() + BSFMthUtil.randDouble(0, 1), 5, 0, 0, 0, 0.1);
                    serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, blockPos.getX() + BSFMthUtil.randDouble(0, 1), blockPos.getY(), blockPos.getZ() + BSFMthUtil.randDouble(0, 1), 5, 0, 0, 0, 0.1);
                    serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, blockPos.getX() + BSFMthUtil.randDouble(0, 1), blockPos.getY() + 1, blockPos.getZ() + BSFMthUtil.randDouble(0, 1), 5, 0, 0, 0, 0.1);
                    serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, blockPos.getX() + BSFMthUtil.randDouble(0, 1), blockPos.getY() + BSFMthUtil.randDouble(0, 1), blockPos.getZ(), 5, 0, 0, 0, 0.1);
                    serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, blockPos.getX() + BSFMthUtil.randDouble(0, 1), blockPos.getY() + BSFMthUtil.randDouble(0, 1), blockPos.getZ() + 1, 5, 0, 0, 0, 0.1);
                }
                pLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOW_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);

                if (!player.getAbilities().instabuild) {
                    pStack.hurtAndBreak(1, player, e -> e.broadcastBreakEvent(player.getUsedItemHand()));
                }
                player.awardStat(Stats.ITEM_USED.get(this));
            } else {
                for (int i = 0; i < 5; i++) {
                    switch (BSFMthUtil.randInt(1, 6)) {
                        case 1 ->
                                serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, blockPos.getX(), blockPos.getY() + BSFMthUtil.randDouble(0, 1), blockPos.getZ() + BSFMthUtil.randDouble(0, 1), 3, 0, 0, 0, 0.04);
                        case 2 ->
                                serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, blockPos.getX() + 1, blockPos.getY() + BSFMthUtil.randDouble(0, 1), blockPos.getZ() + BSFMthUtil.randDouble(0, 1), 3, 0, 0, 0, 0.04);
                        case 3 ->
                                serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, blockPos.getX() + BSFMthUtil.randDouble(0, 1), blockPos.getY(), blockPos.getZ() + BSFMthUtil.randDouble(0, 1), 3, 0, 0, 0, 0.04);
                        case 4 ->
                                serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, blockPos.getX() + BSFMthUtil.randDouble(0, 1), blockPos.getY() + 1, blockPos.getZ() + BSFMthUtil.randDouble(0, 1), 3, 0, 0, 0, 0.04);
                        case 5 ->
                                serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, blockPos.getX() + BSFMthUtil.randDouble(0, 1), blockPos.getY() + BSFMthUtil.randDouble(0, 1), blockPos.getZ(), 3, 0, 0, 0, 0.04);
                        case 6 ->
                                serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, blockPos.getX() + BSFMthUtil.randDouble(0, 1), blockPos.getY() + BSFMthUtil.randDouble(0, 1), blockPos.getZ() + 1, 3, 0, 0, 0, 0.04);
                    }
                }
                if (pRemainingUseDuration % 2 == 1) {
                    pLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOW_PLACE, SoundSource.PLAYERS, 1.0F, 1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
                }
            }
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

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack pStack, ItemStack pRepairCandidate) {
        return pRepairCandidate.is(Items.IRON_INGOT);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TranslatableComponent("snow_block_blender1.tooltip").withStyle(ChatFormatting.BLUE));
        pTooltipComponents.add(new TranslatableComponent("snow_block_blender.tooltip").withStyle(ChatFormatting.DARK_AQUA));
    }
}
