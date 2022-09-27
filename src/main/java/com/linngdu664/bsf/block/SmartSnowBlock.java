package com.linngdu664.bsf.block;

import com.linngdu664.bsf.entity.BSFSnowGolemEntity;
import com.linngdu664.bsf.entity.EntityRegister;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class SmartSnowBlock extends Block {
    private static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);
    private static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
    private static final Predicate<BlockState> PUMPKINS_PREDICATE = (p_51396_) -> p_51396_ != null && p_51396_.is(BlockRegister.SMART_SNOW_BLOCK.get());
    private BlockPattern snowGolemFull;

    public SmartSnowBlock() {
        super(Properties.of(Material.WOOD).strength(1.0F).sound(SoundType.WOOD));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return SHAPE;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getOcclusionShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos) {
        return Shapes.empty();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(ROTATION, Mth.floor((double) (pContext.getRotation() * 16.0F / 360.0F) + 0.5D) & 15);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(ROTATION, pRotation.rotate(pState.getValue(ROTATION), 16));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.setValue(ROTATION, pMirror.mirror(pState.getValue(ROTATION), 16));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(ROTATION);
    }

    @Override
    public void setPlacedBy(@NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, @Nullable LivingEntity pPlacer, @NotNull ItemStack pStack) {
        if (pPlacer instanceof Player player) {
            checkSpawn(pLevel, pPos, player);
        }
    }

    private void checkSpawn(Level level, BlockPos pos, Player player) {
        BlockPattern.BlockPatternMatch blockPatternMatch = getOrCreateSnowGolemFull().find(level, pos);
        if (blockPatternMatch != null) {
            for (int i = 0; i < getOrCreateSnowGolemFull().getHeight(); ++i) {
                BlockInWorld blockInWorld = blockPatternMatch.getBlock(0, i, 0);
                level.setBlock(blockInWorld.getPos(), Blocks.AIR.defaultBlockState(), 2);
                level.levelEvent(2001, blockInWorld.getPos(), Block.getId(blockInWorld.getState()));
            }
            BSFSnowGolemEntity snowGolem = EntityRegister.BSF_SNOW_GOLEM.get().create(level);
            snowGolem.setTame(true);
            snowGolem.setOwnerUUID(player.getUUID());
            snowGolem.setOrderedToSit(true);
            BlockPos blockPos = blockPatternMatch.getBlock(0, 2, 0).getPos();
            snowGolem.moveTo(blockPos.getX() + 0.5D, blockPos.getY() + 0.05D, blockPos.getZ() + 0.5D, 0.0F, 0.0F);
            level.addFreshEntity(snowGolem);
            for (ServerPlayer serverplayer : level.getEntitiesOfClass(ServerPlayer.class, snowGolem.getBoundingBox().inflate(5.0D))) {
                CriteriaTriggers.SUMMONED_ENTITY.trigger(serverplayer, snowGolem);
            }
            for (int l = 0; l < this.getOrCreateSnowGolemFull().getHeight(); ++l) {
                BlockInWorld blockInWorld = blockPatternMatch.getBlock(0, l, 0);
                level.blockUpdated(blockInWorld.getPos(), Blocks.AIR);
            }
        }
    }

    private BlockPattern getOrCreateSnowGolemFull() {
        if (this.snowGolemFull == null) {
            this.snowGolemFull = BlockPatternBuilder.start().aisle("^", "#", "#").where('^', BlockInWorld.hasState(PUMPKINS_PREDICATE)).where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.SNOW_BLOCK))).build();
        }
        return this.snowGolemFull;
    }
}