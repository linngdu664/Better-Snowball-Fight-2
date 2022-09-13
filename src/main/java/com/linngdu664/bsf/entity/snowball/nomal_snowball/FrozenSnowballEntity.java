package com.linngdu664.bsf.entity.snowball.nomal_snowball;

import com.linngdu664.bsf.entity.BSFSnowballEntity;
import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.util.LaunchFunc;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import net.minecraft.world.entity.animal.SnowGolem;

import java.util.List;

import static com.linngdu664.bsf.util.BSFMthUtil.modSqr;
import static com.linngdu664.bsf.util.TargetGetter.getTargetList;

public class FrozenSnowballEntity extends BSFSnowballEntity {
    public float frozenRange=2.5F;
    public FrozenSnowballEntity(LivingEntity livingEntity, Level level, LaunchFunc launchFunc) {
        super(livingEntity, level);
        this.setFrozenTime(60).setLaunchFrom(launchFunc.getLaunchForm()).setDamage(3).setBlazeDamage(8);
        launchFunc.launchProperties(this);
        this.setItem(new ItemStack(ItemRegister.FROZEN_SNOWBALL.get()));
    }

    //This is only used for dispenser
    public FrozenSnowballEntity(Level level, double x, double y, double z) {
        super(level, x, y, z);
        this.setDamage(3).setBlazeDamage(8);
        this.setItem(new ItemStack(ItemRegister.FROZEN_SNOWBALL.get()));
    }

    @Override
    protected Item getRegisterItem() {
        return ItemRegister.FROZEN_SNOWBALL.get();
    }

    @Override
    protected void onHit(@NotNull HitResult pResult) {
        super.onHit(pResult);
        if (!isCaught) {
            BlockPos blockPos = new BlockPos(pResult.getLocation());
            BlockState ice = Blocks.ICE.defaultBlockState();
            BlockState basalt = Blocks.BASALT.defaultBlockState();
            BlockState air = Blocks.AIR.defaultBlockState();
            BlockState snow = Blocks.SNOW.defaultBlockState();
            for (int i = (int) (blockPos.getX() - frozenRange); i <= blockPos.getX() + frozenRange; i++) {
                for (int j = (int) (blockPos.getY() - frozenRange); j <= blockPos.getY() + frozenRange; j++) {
                    for (int k = (int) (blockPos.getZ() - frozenRange); k <= blockPos.getZ() + frozenRange; k++) {
                        if (modSqr(i - blockPos.getX(), j - blockPos.getY(), k - blockPos.getZ()) <= frozenRange * frozenRange) {
                            BlockPos blockPos1 = new BlockPos(i, j, k);
                            BlockState blockState = level.getBlockState(blockPos1);
                            if (blockState.getBlock() == Blocks.WATER && blockState.getValue(LiquidBlock.LEVEL) == 0) {
                                level.setBlockAndUpdate(blockPos1, ice);
                            } else if (blockState.getBlock() == Blocks.LAVA && blockState.getValue(LiquidBlock.LEVEL) == 0) {
                                level.setBlockAndUpdate(blockPos1, basalt);
                            } else if (blockState.getBlock() == Blocks.FIRE) {
                                level.setBlockAndUpdate(blockPos1, air);
                            } else if (blockState.getBlock() == Blocks.AIR && snow.canSurvive(level, blockPos1)) {
                                level.setBlockAndUpdate(blockPos1, snow);
                            }
                        }
                    }
                }
            }
            List<LivingEntity> list = getTargetList(this, LivingEntity.class, 2.5F);
            for (LivingEntity entity : list) {
                if (distanceToSqr(entity) < frozenRange * frozenRange) {
                    //todo:add entity effects here
                }
            }
        }
        if (!level.isClientSide) {
            this.discard();
        }
    }
}
