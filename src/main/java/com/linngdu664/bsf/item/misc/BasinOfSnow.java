package com.linngdu664.bsf.item.misc;

import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.util.BSFMthUtil;
import com.linngdu664.bsf.util.ItemGroup;
import com.linngdu664.bsf.util.TargetGetter;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BasinOfSnow extends Item {
    public BasinOfSnow() {
        super(new Properties().tab(ItemGroup.MAIN).stacksTo(1));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        List<LivingEntity> list = TargetGetter.getTargetList(pPlayer, LivingEntity.class, 8);
        Vec3 cameraVec = Vec3.directionFromRotation(pPlayer.getXRot(), pPlayer.getYRot());
        if (pLevel.isClientSide) {
            Vec3 vecA = cameraVec.cross(new Vec3(0, 1, 0)).normalize();
            if (vecA == Vec3.ZERO) {
                vecA = cameraVec.cross(new Vec3(1, 0, 0).normalize());
            }
            Vec3 vecB = cameraVec.cross(vecA).normalize();
            for (float r = 0.5F; r <= 4.5F; r += 0.5F) {
                float rand = pLevel.getRandom().nextFloat() * Mth.PI * 0.16666667F;
                for (float theta = rand; theta < Mth.TWO_PI + rand; theta += Mth.PI * 0.16666667F) {
                    double x = 8.0F * cameraVec.x + r * (Mth.cos(theta) * vecA.x + Mth.sin(theta) * vecB.x);
                    double y = 8.0F * cameraVec.y + r * (Mth.cos(theta) * vecA.y + Mth.sin(theta) * vecB.y);
                    double z = 8.0F * cameraVec.z + r * (Mth.cos(theta) * vecA.z + Mth.sin(theta) * vecB.z);
                    double inverseL = Mth.fastInvSqrt(BSFMthUtil.modSqr(x, y, z));
                    double rand1 = Math.sqrt(pLevel.getRandom().nextDouble() * 0.9 + 0.1);
                    pLevel.addParticle(ParticleTypes.SNOWFLAKE, pPlayer.getX(), pPlayer.getEyeY() - 0.2, pPlayer.getZ(), x * inverseL * rand1, y * inverseL * rand1, z * inverseL * rand1);
                }
            }
        }
        if (!pLevel.isClientSide) {
            for (LivingEntity livingEntity : list) {
                Vec3 rVec1 = new Vec3(livingEntity.getX() - pPlayer.getX(), livingEntity.getEyeY() - pPlayer.getEyeY() + 0.2, livingEntity.getZ() - pPlayer.getZ());
                Vec3 rVec2 = new Vec3(rVec1.x, livingEntity.getY() - pPlayer.getEyeY(), rVec1.z);
                if (BSFMthUtil.vec3AngleCos(rVec1, cameraVec) > 0.9363291776 && isNotBlocked(rVec1, rVec2, pPlayer, pLevel)) {
                    float r = (float) rVec1.length();
                    int t = 0;
                    if (r < 5.0F) {
                        t = 180;
                        livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, (int) (livingEntity.getTicksFrozen() * 0.5), 2));
                    } else if (r < 8.0F) {
                        t = (int) (180.0F - 6.6666667F * (r - 5.0F) * (r - 5.0F) * (r - 5.0F));
                        livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, (int) (livingEntity.getTicksFrozen() * 0.5), 1));
                    }
                    if (livingEntity.getTicksFrozen() < t) {
                        livingEntity.setTicksFrozen(t);
                    }
                    livingEntity.hurt(DamageSource.playerAttack(pPlayer), Float.MIN_VALUE);
                }
            }
        }
        if (!pPlayer.getAbilities().instabuild) {
            ItemStack newStack = new ItemStack(ItemRegister.EMPTY_BASIN.get(), itemStack.getCount());
            pPlayer.setItemInHand(pUsedHand, newStack);
        }
        return InteractionResultHolder.success(itemStack);
    }

    /**
     * Check whether there are solid blocks on head-head and head-feet line segments (See param). Specially designed for
     * basin of snow/powder snow.
     *
     * @param rVec   The vector from attacker's head to target's head.
     * @param rVec1  The vector from attacker's head to target's feet.
     * @param player The attacker.
     * @param level  The attacker's level.
     * @return Both rVec and rVec1 are blocked by solid block: false. Otherwise: true.
     */
    public boolean isNotBlocked(Vec3 rVec, Vec3 rVec1, Player player, Level level) {
        double offsetX = 0.25 * rVec.z * Mth.fastInvSqrt(BSFMthUtil.modSqr(rVec.x, rVec.z));
        double offsetZ = 0.25 * rVec.x * Mth.fastInvSqrt(BSFMthUtil.modSqr(rVec.x, rVec.z));
        double x = player.getX();
        double y = player.getEyeY();
        double z = player.getZ();
        Vec3 n = rVec.normalize().scale(0.25);
        int l = (int) (4 * rVec.length());
        boolean flag = true;
        for (int i = 0; i < l; i++) {
            int k = 0;
            for (int j = -1; j <= 1; j++) {
                BlockPos blockPos = new BlockPos(x - offsetX * j, y, z + offsetZ * j);
                BlockState blockState = level.getBlockState(blockPos);
                if (blockState.getMaterial().blocksMotion()) {
                    k++;
                }
            }
            System.out.println(k);
            if (k > 1) {
                flag = false;
                break;
            }
            x += n.x;
            y += n.y;
            z += n.z;
        }
        x = player.getX();
        y = player.getEyeY();
        z = player.getZ();
        n = rVec1.normalize().scale(0.25);
        l = (int) (4 * rVec1.length());
        for (int i = 0; i < l; i++) {
            int k = 0;
            for (int j = -1; j <= 1; j++) {
                BlockPos blockPos = new BlockPos(x - offsetX * j, y, z + offsetZ * j);
                BlockState blockState = level.getBlockState(blockPos);
                if (blockState.getMaterial().blocksMotion()) {
                    k++;
                }
            }
            System.out.println(k);
            if (k > 1) {
                return flag;
            }
            x += n.x;
            y += n.y;
            z += n.z;
        }
        return true;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TranslatableComponent("basin_of_snow.tooltip1").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(new TranslatableComponent("basin_of_snow.tooltip").withStyle(ChatFormatting.DARK_AQUA));
    }
}
