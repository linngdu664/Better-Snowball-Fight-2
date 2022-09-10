package com.linngdu664.bsf.item.misc;

import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.util.ItemGroup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.linngdu664.bsf.util.BSFUtil.*;
import static com.linngdu664.bsf.util.TargetGetter.getTargetList;

public class BasinOfSnow extends Item {
    public BasinOfSnow() {
        super(new Properties().tab(ItemGroup.MAIN).stacksTo(1));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        List<LivingEntity> list = getTargetList(pPlayer, LivingEntity.class, 8);
        Vec3 cameraVec = SphericalToCartesian(pPlayer.getXRot() * Mth.DEG_TO_RAD, pPlayer.getYRot() * Mth.DEG_TO_RAD);
        if (pLevel.isClientSide) {
            Vec3 vecA = cameraVec.cross(new Vec3(0, 1, 0)).normalize();
            if (vecA == Vec3.ZERO) {
                vecA = cameraVec.cross(new Vec3(1, 0, 0).normalize());
            }
            Vec3 vecB = cameraVec.cross(vecA).normalize();
            for (float r = 0.1F; r <= 4.1F; r += 0.5F) {
                float rand = pLevel.getRandom().nextFloat();
                for (float theta = rand; theta < Mth.TWO_PI + rand; theta += Mth.PI / 6) {
                    double x = 8.0F * cameraVec.x + r * (Mth.cos(theta) * vecA.x + Mth.sin(theta) * vecB.x);
                    double y = 8.0F * cameraVec.y + r * (Mth.cos(theta) * vecA.y + Mth.sin(theta) * vecB.y);
                    double z = 8.0F * cameraVec.z + r * (Mth.cos(theta) * vecA.z + Mth.sin(theta) * vecB.z);
                    Vec3 vVec = new Vec3(x, y, z).normalize();
                    pLevel.addParticle(ParticleTypes.SNOWFLAKE, pPlayer.getX(), pPlayer.getEyeY(), pPlayer.getZ(), vVec.x, vVec.y, vVec.z);
                }
            }
        }
        if (!pLevel.isClientSide) {
            for (LivingEntity livingEntity : list) {
                Vec3 rVec1 = new Vec3(livingEntity.getX() - pPlayer.getX(), livingEntity.getEyeY() - pPlayer.getEyeY(), livingEntity.getZ() - pPlayer.getZ());
                Vec3 rVec2 = new Vec3(rVec1.x, livingEntity.getY() - pPlayer.getEyeY(), rVec1.z);
                if (vec3AngleCos(rVec1, cameraVec) > 0.9363291776 && isNotBlocked(rVec1, rVec2, pPlayer, pLevel)) {
                    System.out.println("ready freeze");
                    double r2 = modSqr(rVec1);
                    livingEntity.setTicksFrozen(livingEntity.getTicksFrozen() + (int) (320 / r2));
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