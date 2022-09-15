package com.linngdu664.bsf.item.weapon;

import com.linngdu664.bsf.SoundRegister;
import com.linngdu664.bsf.entity.BSFSnowballEntity;
import com.linngdu664.bsf.entity.snowball.nomal_snowball.*;
import com.linngdu664.bsf.entity.snowball.tracking_snowball.*;
import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.util.LaunchFrom;
import com.linngdu664.bsf.util.LaunchFunc;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.linngdu664.bsf.util.BSFMthUtil.SphericalToCartesian;

public class SnowballMachineGunItem extends BSFWeaponItem {
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
            public void launchProperties(BSFSnowballEntity bsfSnowballEntity) {
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
                if (itemStack.is(ItemRegister.EXPLOSIVE_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get()) ||
                        itemStack.is(ItemRegister.EXPLOSIVE_PLAYER_TRACKING_SNOWBALL_STORAGE_TANK.get()) ||
                        itemStack.is(ItemRegister.EXPLOSIVE_SNOWBALL_STORAGE_TANK.get())) {
                    flag = true;
                }
            } else {
                recoil = 0;
            }
            if (timer % 3 == 0 && itemStack != null && (!flag || timer % 6 == 0)) {
                BSFSnowballEntity snowballEntity = itemToEntity(itemStack, pLevel, player);
                BSFShootFromRotation(snowballEntity, player.getXRot(), player.getYRot(), 2.6F, 1.0F);
                pLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundRegister.SNOWBALL_MACHINE_GUN_SHOOT.get(), SoundSource.PLAYERS, 1.0F, 1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
                pLevel.addFreshEntity(snowballEntity);

                Vec3 cameraVec = SphericalToCartesian(pitch * Mth.DEG_TO_RAD, yaw * Mth.DEG_TO_RAD);
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
            // set pitch according to recoil.
            if (pitch > -90.0F && pLevel.isClientSide() && (!flag || timer % 6 < 3)) {
                player.setXRot(pitch - recoil);
            }

            // add and check cd time.
            if (!pLevel.isClientSide && recoil != 0) {
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

    public BSFSnowballEntity itemToEntity(ItemStack itemStack, Level level, Player player) {
        Item item = itemStack.getItem();
        if (item == ItemRegister.COMPACTED_SNOWBALL_STORAGE_TANK.get()) {
            recoil = 0.075F;
            return new CompactedSnowballEntity(player, level, getLaunchFunc());
        } else if (item == ItemRegister.STONE_SNOWBALL_STORAGE_TANK.get()) {
            recoil = 0.1F;
            return new StoneSnowballEntity(player, level, getLaunchFunc());
        } else if (item == ItemRegister.GLASS_SNOWBALL_STORAGE_TANK.get()) {
            recoil = 0.1F;
            return new GlassSnowballEntity(player, level, getLaunchFunc());
        } else if (item == ItemRegister.IRON_SNOWBALL_STORAGE_TANK.get()) {
            recoil = 0.12F;
            return new IronSnowballEntity(player, level, getLaunchFunc());
        } else if (item == ItemRegister.ICE_SNOWBALL_STORAGE_TANK.get()) {
            recoil = 0.1F;
            return new IceSnowballEntity(player, level, getLaunchFunc());
        } else if (item == ItemRegister.GOLD_SNOWBALL_STORAGE_TANK.get()) {
            recoil = 0.14F;
            return new GoldSnowballEntity(player, level, getLaunchFunc());
        } else if (item == ItemRegister.OBSIDIAN_SNOWBALL_STORAGE_TANK.get()) {
            recoil = 0.17F;
            return new ObsidianSnowballEntity(player, level, getLaunchFunc());
        } else if (item == ItemRegister.EXPLOSIVE_SNOWBALL_STORAGE_TANK.get()) {
            recoil = 0.12F;
            return new ExplosiveSnowballEntity(player, level, getLaunchFunc());
        } else if (item == ItemRegister.SPECTRAL_SNOWBALL_STORAGE_TANK.get()) {
            recoil = 0.075F;
            return new SpectralSnowballEntity(player, level, getLaunchFunc());
        } else if (item == ItemRegister.LIGHT_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get()) {
            recoil = 0.075F;
            return new LightMonsterTrackingSnowballEntity(player, level, getLaunchFunc());
        } else if (item == ItemRegister.HEAVY_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get()) {
            recoil = 0.12F;
            return new HeavyMonsterTrackingSnowballEntity(player, level, getLaunchFunc());
        } else if (item == ItemRegister.EXPLOSIVE_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get()) {
            recoil = 0.12F;
            return new ExplosiveMonsterTrackingSnowballEntity(player, level, getLaunchFunc());
        } else if (item == ItemRegister.LIGHT_PLAYER_TRACKING_SNOWBALL_STORAGE_TANK.get()) {
            recoil = 0.075F;
            return new LightPlayerTrackingSnowballEntity(player, level, getLaunchFunc());
        } else if (item == ItemRegister.HEAVY_PLAYER_TRACKING_SNOWBALL_STORAGE_TANK.get()) {
            recoil = 0.12F;
            return new HeavyPlayerTrackingSnowballEntity(player, level, getLaunchFunc());
        } else if (item == ItemRegister.EXPLOSIVE_PLAYER_TRACKING_SNOWBALL_STORAGE_TANK.get()) {
            recoil = 0.12F;
            return new ExplosivePlayerTrackingSnowballEntity(player, level, getLaunchFunc());
        }
        return null;
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
        pTooltipComponents.add(new TranslatableComponent("snowball_machine_gun1.tooltip").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(new TranslatableComponent("snowball_machine_gun2.tooltip").withStyle(ChatFormatting.RED));
        pTooltipComponents.add(new TranslatableComponent("snowball_machine_gun.tooltip").withStyle(ChatFormatting.DARK_AQUA));
    }
}
