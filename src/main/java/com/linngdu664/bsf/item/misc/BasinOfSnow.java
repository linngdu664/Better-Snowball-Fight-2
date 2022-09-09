package com.linngdu664.bsf.item.misc;

import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.util.ItemGroup;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.linngdu664.bsf.util.BSFUtil.*;
import static com.linngdu664.bsf.util.TargetGetter.getTargetList;

public class BasinOfSnow extends Item {
    public BasinOfSnow() {
        super(new Properties().tab(ItemGroup.MAIN).stacksTo(16));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        List<LivingEntity> list = getTargetList(pPlayer, LivingEntity.class, 8);
        Vec3 cameraVec = SphericalToCartesian(pPlayer.getXRot() * Mth.DEG_TO_RAD, pPlayer.getYRot() * Mth.DEG_TO_RAD);
        double newX2 = modSqr(cameraVec.x, cameraVec.z);
        if (!pLevel.isClientSide) {
            for (LivingEntity livingEntity : list) {
                Vec3 rVec1 = new Vec3(livingEntity.getX() - pPlayer.getX(), livingEntity.getEyeY() - pPlayer.getEyeY(), livingEntity.getZ() - pPlayer.getZ());
                Vec3 rVec2 = new Vec3(rVec1.x, livingEntity.getY() - pPlayer.getEyeY(), rVec1.z);
                double newX1 = modSqr(rVec1.x, rVec1.z);
                if (vec2AngleCos(rVec1.x, rVec1.z, cameraVec.x, cameraVec.z) > 0.5 && vec2AngleCos(newX2, cameraVec.y, newX1, rVec1.y) > 0.5 && isNotBlocked(rVec1, rVec2, pPlayer, pLevel)) {
                    System.out.println("ready freeze");
                    double r2 = modSqr(rVec1);
                    livingEntity.setTicksFrozen((int) (180 / r2));
                }
            }
        }
        if (!pPlayer.getAbilities().instabuild) {
            pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.EMPTY_BASIN.get()), true);
            itemStack.shrink(1);
        }
        return InteractionResultHolder.success(itemStack);
    }
}
