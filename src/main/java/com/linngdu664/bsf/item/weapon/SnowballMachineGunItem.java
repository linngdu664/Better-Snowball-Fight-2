package com.linngdu664.bsf.item.weapon;

import com.linngdu664.bsf.entity.AbstractBSFSnowballEntity;
import com.linngdu664.bsf.item.tank.AbstractSnowballTankItem;
import com.linngdu664.bsf.item.tank.normal.ExplosiveSnowballTank;
import com.linngdu664.bsf.item.tank.tracking.ExplosiveMonsterTrackingSnowballTank;
import com.linngdu664.bsf.item.tank.tracking.ExplosivePlayerTrackingSnowballTank;
import com.linngdu664.bsf.util.LaunchFrom;
import com.linngdu664.bsf.util.LaunchFunc;
import com.linngdu664.bsf.util.SoundRegister;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SnowballMachineGunItem extends AbstractBSFWeaponItem {
    private int timer = 0;
    private float recoil = 0;
    private float damageChance;
    private boolean isOnCoolDown = false;

    public SnowballMachineGunItem() {
        super(512, Rarity.EPIC);
    }

    public LaunchFunc getLaunchFunc() {
        return new LaunchFunc() {
            @Override
            public LaunchFrom getLaunchForm() {
                return LaunchFrom.MACHINE_GUN;
            }

            @Override
            public void launchProperties(AbstractBSFSnowballEntity bsfSnowballEntity) {
                bsfSnowballEntity.punch = 1.2;
            }
        };
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        damageChance = 1.0F / (1.0F + EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, stack));
        pPlayer.startUsingItem(pUsedHand);
        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.consume(stack);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onUseTick(@NotNull Level pLevel, @NotNull LivingEntity pLivingEntity, @NotNull ItemStack pStack, int pRemainingUseDuration) {
        Player player = (Player) pLivingEntity;
        float pitch = player.getXRot();
        float yaw = player.getYRot();
        boolean flag = false;
        if (!isOnCoolDown) {
            ItemStack itemStack = findAmmo(player, true, false);
            if (itemStack != null) {
                if (itemStack.getItem() instanceof ExplosiveSnowballTank || itemStack.getItem() instanceof ExplosivePlayerTrackingSnowballTank || itemStack.getItem() instanceof ExplosiveMonsterTrackingSnowballTank) {
                    flag = true;
                }
                if (timer % 3 == 0 && (!flag || timer % 6 == 0)) {
                    AbstractBSFSnowballEntity snowballEntity = ItemToEntity(itemStack.getItem(), player, pLevel, getLaunchFunc());
                    recoil = ((AbstractSnowballTankItem) itemStack.getItem()).getSnowball().getRecoil();
                    BSFShootFromRotation(snowballEntity, pitch, yaw, 2.6F, 1.0F);
                    pLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundRegister.SNOWBALL_MACHINE_GUN_SHOOT.get(), SoundSource.PLAYERS, 1.0F, 1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
                    pLevel.addFreshEntity(snowballEntity);

                    Vec3 cameraVec = Vec3.directionFromRotation(pitch, yaw);
                    // add push
                    if (pLevel.isClientSide()) {
                        player.push(-cameraVec.x * recoil * 0.25, -cameraVec.y * recoil * 0.25, -cameraVec.z * recoil * 0.25);
                    }

                    // add particles
                    if (!pLevel.isClientSide()) {
                        ((ServerLevel) pLevel).sendParticles(ParticleTypes.SNOWFLAKE, player.getX() + cameraVec.x, player.getEyeY() + cameraVec.y, player.getZ() + cameraVec.z, 4, 0, 0, 0, 0.32);
                    }

                    // handle ammo consume and damage weapon.
                    consumeAmmo(itemStack, player);
                    if (pLevel.getRandom().nextFloat() <= damageChance && !player.getAbilities().instabuild) {
                        pStack.setDamageValue(pStack.getDamageValue() + 1);
                        if (pStack.getDamageValue() == 512) {
                            player.awardStat(Stats.ITEM_BROKEN.get(pStack.getItem()));
                            pStack.shrink(1);
                            pLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_BREAK, SoundSource.NEUTRAL, 1.0F, 1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
                        }
                    }
                }
            } else {
                recoil = 0;
            }

            // set pitch according to recoil.
            if (pitch > -90.0F && pLevel.isClientSide() && (!flag || timer % 6 < 3)) {
                player.setXRot(pitch - recoil);
            }

            // add and check cd time.
            if (!pLevel.isClientSide && itemStack != null) {
                timer += 3;
                if (flag && timer >= 60) {
                    player.getCooldowns().addCooldown(this, 60);
                    isOnCoolDown = true;
                    timer = 120;
                } else if (timer >= 120) {
                    player.getCooldowns().addCooldown(this, 60);
                    isOnCoolDown = true;
                }
            }
        }
    }
    @Override
    public void inventoryTick(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (!pLevel.isClientSide) {
            if (timer > 0) {
                timer -= 2;
            } else {
                isOnCoolDown = false;
            }
        }
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack pStack) {
        return Integer.MAX_VALUE;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TranslatableComponent("snowball_machine_gun1.tooltip").withStyle(ChatFormatting.BLUE));
        pTooltipComponents.add(new TranslatableComponent("snowball_machine_gun2.tooltip").withStyle(ChatFormatting.RED));
        pTooltipComponents.add(new TranslatableComponent("snowball_machine_gun.tooltip").withStyle(ChatFormatting.DARK_AQUA));
    }
}
