package com.linngdu664.bsf.item.tool;

import com.linngdu664.bsf.entity.snowball.force_snowball.BlackHoleSnowballEntity;
import com.linngdu664.bsf.particle.ParticleRegister;
import com.linngdu664.bsf.util.BSFMthUtil;
import com.linngdu664.bsf.util.TargetGetter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RepulsiveFieldGeneratorItem extends BSFEnhanceableToolItem {
    public RepulsiveFieldGeneratorItem() {
        super(Rarity.RARE, 256);
    }

    @Override
    public void releaseUsing(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull LivingEntity pLivingEntity, int pTimeCharged) {
        if (pLivingEntity instanceof Player player) {
            if (!pLevel.isClientSide) {
                Vec3 vec3 = Vec3.directionFromRotation(player.getXRot(), player.getYRot());
                List<Projectile> list = TargetGetter.getTargetList(player, Projectile.class, 3.5);
                for (Projectile projectile : list) {
                    Vec3 rVec = new Vec3(projectile.getX() - player.getX(), projectile.getY() - player.getEyeY(), projectile.getZ() - player.getZ());
                    if (BSFMthUtil.vec3AngleCos(rVec, vec3) > 0.70710678F) {
                        if (!(projectile instanceof BlackHoleSnowballEntity)) {
                            Vec3 dvVec = Vec3.directionFromRotation(player.getXRot(), player.getYRot()).scale(2);
                            projectile.push(dvVec.x, dvVec.y, dvVec.z);
                            ((ServerLevel) pLevel).sendParticles(ParticleRegister.SHORT_TIME_SNOWFLAKE.get(), projectile.getX(), projectile.getY(), projectile.getZ(), 10, 0, 0, 0, 0.04);
                        }
                    }
                }
            }
            if (!player.getAbilities().instabuild) {
                pStack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(p.getUsedItemHand()));
            }
            player.awardStat(Stats.ITEM_USED.get(this));
            player.getCooldowns().addCooldown(this, getUseDuration(pStack) - pTimeCharged + 20);
        }
    }

    /**
     * Triggered every tick to make the snowball stop
     *
     * @param pLevel                level
     * @param pLivingEntity         player
     * @param pStack                itemstack
     * @param pRemainingUseDuration remaining use duration
     */
    @Override
    @SuppressWarnings("deprecation")
    public void onUseTick(@NotNull Level pLevel, @NotNull LivingEntity pLivingEntity, @NotNull ItemStack pStack, int pRemainingUseDuration) {
        if (pRemainingUseDuration == 1) {
            this.releaseUsing(pStack, pLevel, pLivingEntity, pRemainingUseDuration);
        } else {
            if (pLivingEntity instanceof Player player) {
                if (!pLevel.isClientSide) {
                    Vec3 vec3 = Vec3.directionFromRotation(player.getXRot(), player.getYRot());
                    List<Projectile> list = TargetGetter.getTargetList(player, Projectile.class, 3);
                    for (Projectile projectile : list) {
                        Vec3 rVec = new Vec3(projectile.getX() - player.getX(), projectile.getY() - player.getEyeY(), projectile.getZ() - player.getZ());
                        if (BSFMthUtil.vec3AngleCos(rVec, vec3) > 0.86602540F) {
                            if (!(projectile instanceof BlackHoleSnowballEntity)) {
                                Vec3 dvVec = projectile.getDeltaMovement().scale(-0.9);
                                projectile.push(dvVec.x, dvVec.y, dvVec.z);
                                ((ServerLevel) pLevel).sendParticles(ParticleTypes.ELECTRIC_SPARK, projectile.getX(), projectile.getY(), projectile.getZ(), 3, 0, 0, 0, 0.04);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        pPlayer.startUsingItem(pUsedHand);
        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack pStack) {
        return 60;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
