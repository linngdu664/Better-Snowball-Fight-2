package com.linngdu664.bsf.item.weapon;

import com.linngdu664.bsf.SoundRegister;
import com.linngdu664.bsf.entity.BSFSnowballEntity;
import com.linngdu664.bsf.item.setter.ItemRegister;
import com.linngdu664.bsf.util.BSFUtil;
import com.linngdu664.bsf.util.LaunchFrom;
import com.linngdu664.bsf.util.LaunchFunc;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PowerfulSnowballCannonItem extends SnowballCannonItem{
    @Override
    public LaunchFunc getLaunchFunc(double damageDropRate) {
        return new LaunchFunc() {
            @Override
            public LaunchFrom getLaunchForm() {
                return LaunchFrom.CANNON;
            }

            @Override
            public void launchProperties(BSFSnowballEntity bsfSnowballEntity) {
                bsfSnowballEntity.punch = damageDropRate * 2.5F;
            }
        };
    }
    @Override
    public void releaseUsing(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull LivingEntity pEntityLiving, int pTimeLeft) {
        if (pEntityLiving instanceof Player player) {
            int i = this.getUseDuration(pStack) - pTimeLeft;
            float f = getPowerForTime(i);
            if (f >= 0.1F) {
                ItemStack itemStack = BSFUtil.findAmmo(player, false, true);
                if (itemStack != null) {
                    boolean k = BSFUtil.isAmmoTank(itemStack.getItem(), true);

                    BSFSnowballEntity snowballEntity = itemToEntity(itemStack, pLevel, player, f);

                    //v is change
                    BSFUtil.shootFromRotation(snowballEntity, player.getXRot(), player.getYRot(), 0.0F, f * 4.0F, 1.0F);

                    pStack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(p.getUsedItemHand()));


                    float pitch = player.getXRot();
                    float yaw = player.getYRot();
                    Vec3 cameraVec = new Vec3(-Mth.cos(pitch * Mth.DEG_TO_RAD) * Mth.sin(yaw * Mth.DEG_TO_RAD), -Mth.sin(pitch * Mth.DEG_TO_RAD), Mth.cos(pitch * Mth.DEG_TO_RAD) * Mth.cos(yaw * Mth.DEG_TO_RAD));
                    //add push
                    if (pLevel.isClientSide()) {
                        player.push(-0.12 * cameraVec.x, -0.12 * cameraVec.y, -0.12 * cameraVec.z);
                    }

                    //add particles
                    if (!pLevel.isClientSide()) {
                        ServerLevel serverLevel = (ServerLevel) pLevel;
                        serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, player.getX() + cameraVec.x, player.getEyeY() + cameraVec.y, player.getZ() + cameraVec.z, 16, 0, 0, 0, 0.32);
                    }

                    pLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundRegister.SNOWBALL_CANNON_SHOOT.get(), SoundSource.PLAYERS, 1.0F, 1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                    pLevel.addFreshEntity(snowballEntity);
                    if (!player.getAbilities().instabuild) {
                        if (k) {
                            itemStack.setDamageValue(itemStack.getDamageValue() + 1);
                            if (itemStack.getDamageValue() == 96) {
                                itemStack.shrink(1);
                                player.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.EMPTY_SNOWBALL_STORAGE_TANK.get(), 1), true);
                            }
                        } else {
                            itemStack.shrink(1);
                            if (itemStack.isEmpty()) {
                                player.getInventory().removeItem(itemStack);
                            }
                        }
                    }
                }
                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }
    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TranslatableComponent("snowball_cannon5.tooltip").withStyle(ChatFormatting.RED));
        pTooltipComponents.add(new TranslatableComponent("snowball_cannon2.tooltip").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(new TranslatableComponent("snowball_cannon3.tooltip").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(new TranslatableComponent("snowball_cannon.tooltip").withStyle(ChatFormatting.DARK_AQUA));
    }
}
