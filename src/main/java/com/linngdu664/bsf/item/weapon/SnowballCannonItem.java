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
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.linngdu664.bsf.util.BSFMthUtil.SphericalToCartesian;

public class SnowballCannonItem extends BSFWeaponItem {
    public SnowballCannonItem() {
        super(256, Rarity.RARE);
    }

    public LaunchFunc getLaunchFunc(double damageDropRate) {
        return new LaunchFunc() {
            @Override
            public LaunchFrom getLaunchForm() {
                return LaunchFrom.CANNON;
            }

            @Override
            public void launchProperties(BSFSnowballEntity bsfSnowballEntity) {
                bsfSnowballEntity.punch = damageDropRate * 1.51F;
            }
        };
    }

    public static float getPowerForTime(int pCharge) {
        float f = (float)pCharge / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }

    @Override
    public void releaseUsing(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull LivingEntity pEntityLiving, int pTimeLeft) {
        if (pEntityLiving instanceof Player player) {
            int i = this.getUseDuration(pStack) - pTimeLeft;
            float f = getPowerForTime(i);
            if (f >= 0.1F) {
                ItemStack itemStack = findAmmo(player, false, true);
                if (itemStack != null) {
                    BSFSnowballEntity snowballEntity = itemToEntity(itemStack, pLevel, player, f);

                    BSFShootFromRotation(snowballEntity, player.getXRot(), player.getYRot(), f * 3.0F, 1.0F);
//                    if (coreType == 1) {
//                        snowballEntity.frozenTime+=140;
//
//                        snowballEntity.frozenTicks += 140;
//                        switch (snowballEntity.type) {
//                            case COMPACTED, TRACKING_MONSTER, TRACKING_PLAYER -> snowballEntity.blazeDamage = f * 4.0F;
//                            case STONE -> snowballEntity.blazeDamage = f * 5.0F;
//                            case GLASS -> snowballEntity.blazeDamage = f * 6.0F;
//                            case IRON, TRACKING_MONSTER_DAMAGE, TRACKING_PLAYER_DAMAGE -> snowballEntity.blazeDamage = f * 7.0F;
//                            case GOLD -> snowballEntity.blazeDamage = f * 8.0F;
//                            case OBSIDIAN -> snowballEntity.blazeDamage = f * 9.0F;
//                            case EXPLOSIVE, TRACKING_PLAYER_EXPLOSIVE, TRACKING_MONSTER_EXPLOSIVE -> snowballEntity.blazeDamage = 5.0F;
//                            case ICE -> {
//                                snowballEntity.blazeDamage = f * 10.0F;
//                                snowballEntity.frozenTicks = 200;
//                            }
//                        }
//                    } else if (coreType == 2) {
//                        snowballEntity.punch = f * 2.5;
//                        BSFMthUtil.shootFromRotation(snowballEntity, player.getXRot(), player.getYRot(), 0.0F, f * 4.0F, 1.0F);
//                        snowballEntity.weaknessTicks = 180;
//                    }
                    pStack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(p.getUsedItemHand()));

                    Vec3 cameraVec = SphericalToCartesian(player.getXRot() * Mth.DEG_TO_RAD, player.getYRot() * Mth.DEG_TO_RAD);
                    //add push
                    if (pLevel.isClientSide()) {
                        player.push(-0.2 * cameraVec.x * f, -0.2 * cameraVec.y * f, -0.2 * cameraVec.z * f);
                    }

                    //add particles
                    if (!pLevel.isClientSide()) {
                        ServerLevel serverLevel = (ServerLevel) pLevel;
                        serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, player.getX() + cameraVec.x, player.getEyeY() + cameraVec.y, player.getZ() + cameraVec.z, 16, 0, 0, 0, 0.32);
                    }

                    pLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundRegister.SNOWBALL_CANNON_SHOOT.get(), SoundSource.PLAYERS, 1.0F, 1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                    pLevel.addFreshEntity(snowballEntity);
                    consumeAmmo(itemStack, player);
                    /*
                    if (isAmmoTank(itemStack.getItem(), true)) {
                        itemStack.hurtAndBreak(1, player, (p) -> p.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.EMPTY_SNOWBALL_STORAGE_TANK.get()), true));
                    } else if (!player.getAbilities().instabuild) {
                        itemStack.shrink(1);
                        if (itemStack.isEmpty()) {
                            player.getInventory().removeItem(itemStack);
                        }
                    }

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
                    }*/
                }
                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    /**
     * This horrible method is used to return the specific snowball entity according to the item.
     * @param itemStack The selected itemstack.
     * @param level Player's level
     * @param player Just player
     * @param damageDropRate This param(0~1) is associate with the charge time of the snowball cannon. The damage value will multiply this param.
     * @return The specific snowball entity with proper characters.
     */
    public BSFSnowballEntity itemToEntity(ItemStack itemStack, Level level, Player player, double damageDropRate) {
        Item item = itemStack.getItem();
        if (item == ItemRegister.COMPACTED_SNOWBALL.get() || item == ItemRegister.COMPACTED_SNOWBALL_STORAGE_TANK.get()) {
            return new CompactedSnowballEntity(player, level, getLaunchFunc(damageDropRate));
        } else if (item == ItemRegister.STONE_SNOWBALL.get() || item == ItemRegister.STONE_SNOWBALL_STORAGE_TANK.get()) {
            return new StoneSnowballEntity(player, level, getLaunchFunc(damageDropRate));
        } else if (item == ItemRegister.GLASS_SNOWBALL.get() || item == ItemRegister.GLASS_SNOWBALL_STORAGE_TANK.get()) {
            return new GlassSnowballEntity(player, level, getLaunchFunc(damageDropRate));
        } else if (item == ItemRegister.IRON_SNOWBALL.get() || item == ItemRegister.IRON_SNOWBALL_STORAGE_TANK.get()) {
            return new IronSnowballEntity(player, level, getLaunchFunc(damageDropRate));
        } else if (item == ItemRegister.ICE_SNOWBALL.get() || item == ItemRegister.ICE_SNOWBALL_STORAGE_TANK.get()) {
            return new IceSnowballEntity(player, level, getLaunchFunc(damageDropRate));
        } else if (item == ItemRegister.GOLD_SNOWBALL.get() || item == ItemRegister.GOLD_SNOWBALL_STORAGE_TANK.get()) {
            return new GoldSnowballEntity(player, level, getLaunchFunc(damageDropRate));
        } else if (item == ItemRegister.OBSIDIAN_SNOWBALL.get() || item == ItemRegister.OBSIDIAN_SNOWBALL_STORAGE_TANK.get()) {
            return new ObsidianSnowballEntity(player, level, getLaunchFunc(damageDropRate));
        } else if (item == ItemRegister.EXPLOSIVE_SNOWBALL.get() || item == ItemRegister.EXPLOSIVE_SNOWBALL_STORAGE_TANK.get()) {
            return new ExplosiveSnowballEntity(player, level, getLaunchFunc(damageDropRate));
        } else if (item == ItemRegister.SPECTRAL_SNOWBALL.get() || item == ItemRegister.SPECTRAL_SNOWBALL_STORAGE_TANK.get()) {
            return new SpectralSnowballEntity(player, level, getLaunchFunc(damageDropRate));
        } else if (item == ItemRegister.FROZEN_SNOWBALL.get() || item == ItemRegister.FROZEN_SNOWBALL_STORAGE_TANK.get()) {
            return new FrozenSnowballEntity(player, level, getLaunchFunc(damageDropRate));
        } else if (item == ItemRegister.POWDER_SNOWBALL.get() || item == ItemRegister.POWDER_SNOWBALL_STORAGE_TANK.get()) {
            return new FrozenSnowballEntity(player, level, getLaunchFunc(damageDropRate));
        } else if (item == ItemRegister.LIGHT_MONSTER_TRACKING_SNOWBALL.get() || item == ItemRegister.LIGHT_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get()) {
            return new LightMonsterTrackingSnowballEntity(player, level, getLaunchFunc(damageDropRate));
        } else if (item == ItemRegister.HEAVY_MONSTER_TRACKING_SNOWBALL.get() || item == ItemRegister.HEAVY_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get()) {
            return new HeavyMonsterTrackingSnowballEntity(player, level, getLaunchFunc(damageDropRate));
        } else if (item == ItemRegister.EXPLOSIVE_MONSTER_TRACKING_SNOWBALL.get() || item == ItemRegister.EXPLOSIVE_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get()) {
            return new ExplosiveMonsterTrackingSnowballEntity(player, level, getLaunchFunc(damageDropRate));
        } else if (item == ItemRegister.LIGHT_PLAYER_TRACKING_SNOWBALL.get() || item == ItemRegister.LIGHT_PLAYER_TRACKING_SNOWBALL_STORAGE_TANK.get()) {
            return new LightPlayerTrackingSnowballEntity(player, level, getLaunchFunc(damageDropRate));
        } else if (item == ItemRegister.HEAVY_PLAYER_TRACKING_SNOWBALL.get() || item == ItemRegister.HEAVY_PLAYER_TRACKING_SNOWBALL_STORAGE_TANK.get()) {
            return new HeavyPlayerTrackingSnowballEntity(player, level, getLaunchFunc(damageDropRate));
        } else if (item == ItemRegister.EXPLOSIVE_PLAYER_TRACKING_SNOWBALL.get() || item == ItemRegister.EXPLOSIVE_PLAYER_TRACKING_SNOWBALL_STORAGE_TANK.get()) {
            return new ExplosivePlayerTrackingSnowballEntity(player, level, getLaunchFunc(damageDropRate));
        }
        return null;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pHand);
        pPlayer.startUsingItem(pHand);
        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack pStack) {
        return 72000;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TranslatableComponent("snowball_cannon1.tooltip").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(new TranslatableComponent("snowball_cannon2.tooltip").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(new TranslatableComponent("snowball_cannon3.tooltip").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(new TranslatableComponent("snowball_cannon.tooltip").withStyle(ChatFormatting.DARK_AQUA));
    }
}
