package com.linngdu664.bsf.item.weapon;

import com.linngdu664.bsf.SoundRegister;
import com.linngdu664.bsf.entity.BSFSnowballEntity;
import com.linngdu664.bsf.entity.snowball.nomal_snowball.*;
import com.linngdu664.bsf.item.setter.ItemRegister;
import com.linngdu664.bsf.util.BSFUtil;
import com.linngdu664.bsf.util.LaunchFrom;
import com.linngdu664.bsf.util.LaunchFunc;
import com.linngdu664.bsf.util.ItemGroup;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SnowballMachineGunItem extends Item {
    private static int timer;
    private static float recoil;

    public SnowballMachineGunItem() {
        super(new Properties().tab(ItemGroup.MAIN).stacksTo(1).durability(512));
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
        timer = 0;
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        pPlayer.startUsingItem(pUsedHand);
        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void onUseTick(@NotNull Level pLevel, @NotNull LivingEntity pLivingEntity, @NotNull ItemStack pStack, int pRemainingUseDuration) {
        Player player = (Player) pLivingEntity;
        float pitch = player.getXRot();
        float yaw = player.getYRot();
        if (timer % 3 == 0) {
            ItemStack itemStack = BSFUtil.findAmmo(player, true, false);
            if (itemStack != null) {
                BSFSnowballEntity snowballEntity = itemToEntity(itemStack, pLevel, player);

//                if (itemStack.getItem() == ItemRegister.COMPACTED_SNOWBALL_STORAGE_TANK.get()) {
//                    snowballEntity = new AdvancedSnowballEntity(pLevel, player, TankType.COMPACTED);
//                    snowballEntity.setItem(new ItemStack(ItemRegister.COMPACTED_SNOWBALL.get()));
//                    j = 0.075F;
//                } else if (itemStack.getItem() == ItemRegister.STONE_SNOWBALL_STORAGE_TANK.get()) {
//                    snowballEntity = new AdvancedSnowballEntity(pLevel, player, TankType.STONE, 3.0F, 4.0F);
//                    snowballEntity.setItem(new ItemStack(ItemRegister.STONE_SNOWBALL.get()));
//                    j = 0.1F;
//                } else if (itemStack.getItem() == ItemRegister.GLASS_SNOWBALL_STORAGE_TANK.get()) {
//                    snowballEntity = new AdvancedSnowballEntity(pLevel, player, TankType.GLASS, 4.0F, 5.0F);
//                    snowballEntity.setItem(new ItemStack(ItemRegister.GLASS_SNOWBALL.get()));
//                    j = 0.1F;
//                } else if (itemStack.getItem() == ItemRegister.IRON_SNOWBALL_STORAGE_TANK.get()) {
//                    snowballEntity = new AdvancedSnowballEntity(pLevel, player, TankType.IRON, 5.0F, 7.0F);
//                    snowballEntity.setItem(new ItemStack(ItemRegister.IRON_SNOWBALL.get()));
//                    j = 0.12F;
//                } else if (itemStack.getItem() == ItemRegister.ICE_SNOWBALL_STORAGE_TANK.get()) {
//                    snowballEntity = new AdvancedSnowballEntity(pLevel, player, TankType.ICE, 4.0F, 6.0F);
//                    snowballEntity.frozenTicks = 60;
//                    snowballEntity.setItem(new ItemStack(ItemRegister.ICE_SNOWBALL.get()));
//                    j = 0.1F;
//                } else if (itemStack.getItem() == ItemRegister.GOLD_SNOWBALL_STORAGE_TANK.get()) {
//                    snowballEntity = new AdvancedSnowballEntity(pLevel, player, TankType.GOLD, 6.0F, 7.0F);
//                    snowballEntity.setItem(new ItemStack(ItemRegister.GOLD_SNOWBALL.get()));
//                    j = 0.14F;
//                } else if (itemStack.getItem() == ItemRegister.OBSIDIAN_SNOWBALL_STORAGE_TANK.get()) {
//                    snowballEntity = new AdvancedSnowballEntity(pLevel, player, TankType.OBSIDIAN, 7.0F, 8.0F);
//                    snowballEntity.setItem(new ItemStack(ItemRegister.OBSIDIAN_SNOWBALL.get()));
//                    j = 0.17F;
//                } else if (itemStack.getItem() == ItemRegister.EXPLOSIVE_SNOWBALL_STORAGE_TANK.get()) {
//                    snowballEntity = new AdvancedSnowballEntity(pLevel, player, TankType.EXPLOSIVE, 3.0F, 5.0F);
//                    snowballEntity.setItem(new ItemStack(ItemRegister.EXPLOSIVE_SNOWBALL.get()));
//                    j = 0.12F;
//                } else {
//                    snowballEntity = new AdvancedSnowballEntity(pLevel, player, TankType.SPECTRAL);
//                    snowballEntity.setItem(new ItemStack(ItemRegister.SPECTRAL_SNOWBALL.get()));
//                    j = 0.075F;
//                }

                BSFUtil.shootFromRotation(snowballEntity, player.getXRot(), player.getYRot(), 0.0F, 2.6F, 1.0F);
                pLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundRegister.SNOWBALL_MACHINE_GUN_SHOOT.get(), SoundSource.PLAYERS, 1.0F, 1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
                pLevel.addFreshEntity(snowballEntity);

                Vec3 cameraVec = new Vec3(-Mth.cos(pitch * Mth.DEG_TO_RAD) * Mth.sin(yaw * Mth.DEG_TO_RAD), -Mth.sin(pitch * Mth.DEG_TO_RAD), Mth.cos(pitch * Mth.DEG_TO_RAD) * Mth.cos(yaw * Mth.DEG_TO_RAD));

                //add push
                if (pLevel.isClientSide()) {
                    player.push(-0.04 * cameraVec.x, -0.04 * cameraVec.y, -0.04 * cameraVec.z);
                }

                //add particles
                if (!pLevel.isClientSide()) {
                    ServerLevel serverLevel = (ServerLevel) pLevel;
                    serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, player.getX() + cameraVec.x, player.getEyeY() + cameraVec.y, player.getZ() + cameraVec.z, 4, 0, 0, 0, 0.32);
                }

                if (!player.getAbilities().instabuild) {
                    itemStack.hurtAndBreak(1, player, (p) -> {
                        itemStack.shrink(1);
                        p.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.EMPTY_SNOWBALL_STORAGE_TANK.get()), true);
                    });
                    pStack.setDamageValue(pStack.getDamageValue() + 1);
                    if (pStack.getDamageValue() == 512) {
                        pStack.shrink(1);
                        pLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_BREAK, SoundSource.NEUTRAL, 1.0F, 1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
                    }
                }
            } else {
                recoil = 0;
            }
        }
        if (pitch > -90.0F) {
            player.setXRot(pitch - recoil);
        }
        timer++;
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
        }
        return null;
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
    public boolean isValidRepairItem(@NotNull ItemStack pStack, ItemStack pRepairCandidate) {
        return pRepairCandidate.is(Items.IRON_INGOT);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TranslatableComponent("snowball_machine_gun1.tooltip").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(new TranslatableComponent("snowball_machine_gun2.tooltip").withStyle(ChatFormatting.RED));
        pTooltipComponents.add(new TranslatableComponent("snowball_machine_gun.tooltip").withStyle(ChatFormatting.DARK_AQUA));
    }
}
