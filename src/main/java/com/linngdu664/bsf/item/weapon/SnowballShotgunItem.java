package com.linngdu664.bsf.item.weapon;

import com.linngdu664.bsf.entity.BSFSnowballEntity;
import com.linngdu664.bsf.entity.snowball.nomal_snowball.*;
import com.linngdu664.bsf.entity.snowball.tracking_snowball.*;
import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.item.tank.SnowballStorageTankItem;
import com.linngdu664.bsf.util.LaunchFrom;
import com.linngdu664.bsf.util.LaunchFunc;
import com.linngdu664.bsf.util.SoundRegister;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SnowballShotgunItem extends BSFWeaponItem {
    private double pushRank;

    public SnowballShotgunItem() {
        super(256, Rarity.EPIC);
    }

    public static LaunchFunc getLaunchFunc() {
        return new LaunchFunc() {
            @Override
            public LaunchFrom getLaunchForm() {
                return LaunchFrom.SHOTGUN;
            }

            @Override
            public void launchProperties(BSFSnowballEntity bsfSnowballEntity) {
                bsfSnowballEntity.punch = 1.51F;
            }
        };
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        pushRank = 0.24;
        int i;
        for (i = 0; i < 4; i++) {
            ItemStack itemStack = findAmmo(player, false, true);
            if (itemStack != null) {
                BSFSnowballEntity snowballEntity = itemToEntity(itemStack, level, player);
                assert snowballEntity != null;
                if (!player.isShiftKeyDown()) {
                    BSFShootFromRotation(snowballEntity, player.getXRot(), player.getYRot(), 2.0F, 10.0F);
                    level.addFreshEntity(snowballEntity);
                }
                consumeAmmo(itemStack, player);
            } else {
                break;
            }
        }
        if (i > 0) {
            Vec3 cameraVec = Vec3.directionFromRotation(player.getXRot(), player.getYRot());
            if (!player.isShiftKeyDown()) {
                if (level.isClientSide()) {
                    player.push(-0.24 * cameraVec.x, -0.24 * cameraVec.y, -0.24 * cameraVec.z);
                } else {
                    //add particles
                    ServerLevel serverLevel = (ServerLevel) level;
                    serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, player.getX() + cameraVec.x, player.getEyeY() + cameraVec.y, player.getZ() + cameraVec.z, 16, 0, 0, 0, 0.16);
                }
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundRegister.SHOTGUN_FIRE_2.get(), SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
            } else {
                if (level.isClientSide()) {
                    player.push(-pushRank * cameraVec.x, -pushRank * cameraVec.y, -pushRank * cameraVec.z);
                } else {
                    //add particles
                    ServerLevel serverLevel = (ServerLevel) level;
                    serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, player.getX() + cameraVec.x, player.getEyeY() + cameraVec.y, player.getZ() + cameraVec.z, (int) (29 * pushRank + 9.04), 0, 0, 0, 0.32);
                }
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundRegister.SHOTGUN_FIRE_1.get(), SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
            }
            stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(p.getUsedItemHand()));
            player.getCooldowns().addCooldown(this, 20);
            player.awardStat(Stats.ITEM_USED.get(this));
        }
        return InteractionResultHolder.pass(stack);
    }

    private BSFSnowballEntity itemToEntity(ItemStack itemStack, Level level, Player player) {
        Item item = itemStack.getItem();
        if (item instanceof SnowballStorageTankItem tank) {
            item = tank.getSnowball();
        }
        if (item == ItemRegister.COMPACTED_SNOWBALL.get()) {
            pushRank += 0.10;
            return new CompactedSnowballEntity(player, level, getLaunchFunc());
        } else if (item == ItemRegister.STONE_SNOWBALL.get()) {
            pushRank += 0.12;
            return new StoneSnowballEntity(player, level, getLaunchFunc());
        } else if (item == ItemRegister.GLASS_SNOWBALL.get()) {
            pushRank += 0.12;
            return new GlassSnowballEntity(player, level, getLaunchFunc());
        } else if (item == ItemRegister.IRON_SNOWBALL.get()) {
            pushRank += 0.16;
            return new IronSnowballEntity(player, level, getLaunchFunc());
        } else if (item == ItemRegister.ICE_SNOWBALL.get()) {
            pushRank += 0.12;
            return new IceSnowballEntity(player, level, getLaunchFunc());
        } else if (item == ItemRegister.GOLD_SNOWBALL.get()) {
            pushRank += 0.18;
            return new GoldSnowballEntity(player, level, getLaunchFunc());
        } else if (item == ItemRegister.OBSIDIAN_SNOWBALL.get()) {
            pushRank += 0.18;
            return new ObsidianSnowballEntity(player, level, getLaunchFunc());
        } else if (item == ItemRegister.EXPLOSIVE_SNOWBALL.get()) {
            pushRank += 0.42;
            return new ExplosiveSnowballEntity(player, level, getLaunchFunc());
        } else if (item == ItemRegister.SPECTRAL_SNOWBALL.get()) {
            pushRank += 0.10;
            return new SpectralSnowballEntity(player, level, getLaunchFunc());
        } else if (item == ItemRegister.FROZEN_SNOWBALL.get()) {
            pushRank += 0.12;
            return new FrozenSnowballEntity(player, level, getLaunchFunc());
        } else if (item == ItemRegister.POWDER_SNOWBALL.get()) {
            pushRank += 0.12;
            return new PowderSnowballEntity(player, level, getLaunchFunc());
        } else if (item == ItemRegister.LIGHT_MONSTER_TRACKING_SNOWBALL.get()) {
            pushRank += 0.10;
            return new LightMonsterTrackingSnowballEntity(player, level, getLaunchFunc());
        } else if (item == ItemRegister.HEAVY_MONSTER_TRACKING_SNOWBALL.get()) {
            pushRank += 0.18;
            return new HeavyMonsterTrackingSnowballEntity(player, level, getLaunchFunc());
        } else if (item == ItemRegister.EXPLOSIVE_MONSTER_TRACKING_SNOWBALL.get()) {
            pushRank += 0.42;
            return new ExplosiveMonsterTrackingSnowballEntity(player, level, getLaunchFunc());
        } else if (item == ItemRegister.LIGHT_PLAYER_TRACKING_SNOWBALL.get()) {
            pushRank += 0.10;
            return new LightPlayerTrackingSnowballEntity(player, level, getLaunchFunc());
        } else if (item == ItemRegister.HEAVY_PLAYER_TRACKING_SNOWBALL.get()) {
            pushRank += 0.18;
            return new HeavyPlayerTrackingSnowballEntity(player, level, getLaunchFunc());
        } else if (item == ItemRegister.EXPLOSIVE_PLAYER_TRACKING_SNOWBALL.get()) {
            pushRank += 0.42;
            return new ExplosivePlayerTrackingSnowballEntity(player, level, getLaunchFunc());
        }
        return null;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TranslatableComponent("snowball_shotgun1.tooltip").withStyle(ChatFormatting.DARK_PURPLE));
        pTooltipComponents.add(new TranslatableComponent("snowball_shotgun2.tooltip").withStyle(ChatFormatting.DARK_PURPLE));
        pTooltipComponents.add(new TranslatableComponent("snowball_shotgun3.tooltip").withStyle(ChatFormatting.DARK_PURPLE));
        pTooltipComponents.add(new TranslatableComponent("snowball_shotgun.tooltip").withStyle(ChatFormatting.DARK_AQUA));
    }
}
