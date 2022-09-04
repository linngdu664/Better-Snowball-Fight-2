package com.linngdu664.bsf.item.weapon;

import com.linngdu664.bsf.SoundRegister;
import com.linngdu664.bsf.entity.AdvancedSnowballEntity;
import com.linngdu664.bsf.item.setter.ItemRegister;
import com.linngdu664.bsf.util.SnowballType;
import com.linngdu664.bsf.util.Util;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class SnowballShotgunItem extends Item {
    public SnowballShotgunItem() {
        super(new Properties().tab(ItemRegister.GROUP).stacksTo(1).durability(256));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (!player.isShiftKeyDown()) {
            int i;
            for (i = 0; i < 4; i++) {
                ItemStack itemStack = Util.findAmmo(player, false);
                if (itemStack != null) {
                    boolean k = Util.isAmmoTank(itemStack, true);
                    AdvancedSnowballEntity snowballEntity;
                    if (itemStack.getItem() == ItemRegister.COMPACTED_SNOWBALL.get() || itemStack.getItem() == ItemRegister.COMPACTED_SNOWBALL_STORAGE_TANK.get()) {
                        snowballEntity = new AdvancedSnowballEntity(level, player, SnowballType.COMPACTED);
                        snowballEntity.setItem(new ItemStack(ItemRegister.COMPACTED_SNOWBALL.get()));
                    } else if (itemStack.getItem() == ItemRegister.STONE_SNOWBALL.get() || itemStack.getItem() == ItemRegister.STONE_SNOWBALL_STORAGE_TANK.get()) {
                        snowballEntity = new AdvancedSnowballEntity(level, player, SnowballType.STONE, 3.0F, 4.0F);
                        snowballEntity.setItem(new ItemStack(ItemRegister.STONE_SNOWBALL.get()));
                    } else if (itemStack.getItem() == ItemRegister.GLASS_SNOWBALL.get() || itemStack.getItem() == ItemRegister.GLASS_SNOWBALL_STORAGE_TANK.get()) {
                        snowballEntity = new AdvancedSnowballEntity(level, player, SnowballType.GLASS, 4.0F, 5.0F);
                        snowballEntity.setItem(new ItemStack(ItemRegister.GLASS_SNOWBALL.get()));
                    } else if (itemStack.getItem() == ItemRegister.IRON_SNOWBALL.get() || itemStack.getItem() == ItemRegister.IRON_SNOWBALL_STORAGE_TANK.get()) {
                        snowballEntity = new AdvancedSnowballEntity(level, player, SnowballType.IRON, 5.0F, 7.0F);
                        snowballEntity.setItem(new ItemStack(ItemRegister.IRON_SNOWBALL.get()));
                    } else if (itemStack.getItem() == ItemRegister.ICE_SNOWBALL.get() || itemStack.getItem() == ItemRegister.ICE_SNOWBALL_STORAGE_TANK.get()) {
                        snowballEntity = new AdvancedSnowballEntity(level, player, SnowballType.ICE, 4.0F, 6.0F);
                        snowballEntity.frozenTicks = 60;
                        snowballEntity.setItem(new ItemStack(ItemRegister.ICE_SNOWBALL.get()));
                    } else if (itemStack.getItem() == ItemRegister.GOLD_SNOWBALL.get() || itemStack.getItem() == ItemRegister.GOLD_SNOWBALL_STORAGE_TANK.get()) {
                        snowballEntity = new AdvancedSnowballEntity(level, player, SnowballType.GOLD, 6.0F, 7.0F);
                        snowballEntity.setItem(new ItemStack(ItemRegister.GOLD_SNOWBALL.get()));
                    } else if (itemStack.getItem() == ItemRegister.OBSIDIAN_SNOWBALL.get() || itemStack.getItem() == ItemRegister.OBSIDIAN_SNOWBALL_STORAGE_TANK.get()) {
                        snowballEntity = new AdvancedSnowballEntity(level, player, SnowballType.OBSIDIAN, 7.0F, 8.0F);
                        snowballEntity.setItem(new ItemStack(ItemRegister.OBSIDIAN_SNOWBALL.get()));
                    } else if (itemStack.getItem() == ItemRegister.SPECTRAL_SNOWBALL.get() || itemStack.getItem() == ItemRegister.SPECTRAL_SNOWBALL_STORAGE_TANK.get()) {
                        snowballEntity = new AdvancedSnowballEntity(level, player, SnowballType.SPECTRAL);
                        snowballEntity.setItem(new ItemStack(ItemRegister.SPECTRAL_SNOWBALL.get()));
                    } else {
                        snowballEntity = new AdvancedSnowballEntity(level, player, SnowballType.EXPLOSIVE, 3.0F, 5.0F);
                        snowballEntity.setItem(new ItemStack(ItemRegister.EXPLOSIVE_SNOWBALL.get()));
                    }
                    snowballEntity.punch = 1.51F;

                    Util.shootFromRotation(snowballEntity,player.getXRot(), player.getYRot(), 0.0F, 2.0F, 10.0F);

                    stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(p.getUsedItemHand()));
                    level.addFreshEntity(snowballEntity);
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
                } else {
                    break;
                }
            }
            if (i > 0) {
                float pitch = player.getXRot();
                float yaw = player.getYRot();
                Vec3 cameraVec = new Vec3(-Mth.cos(pitch * Mth.DEG_TO_RAD) * Mth.sin(yaw * Mth.DEG_TO_RAD), -Mth.sin(pitch * Mth.DEG_TO_RAD), Mth.cos(pitch * Mth.DEG_TO_RAD) * Mth.cos(yaw * Mth.DEG_TO_RAD));
                if (level.isClientSide()) {
                    player.push(-0.24 * cameraVec.x, -0.24 * cameraVec.y, -0.24 * cameraVec.z);
                }
                if (!level.isClientSide()) {
                    ServerLevel serverLevel = (ServerLevel) level;
                    serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, player.getX() + cameraVec.x, player.getY() + cameraVec.y + 1.8, player.getZ() + cameraVec.z, 16, 0, 0, 0, 0.08);
                }
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundRegister.SHOTGUN_FIRE_2.get(), SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);

            }
        } else {
            double pushRank = 0.24;
            int i;
            for (i = 0; i < 4; i++) {
                ItemStack itemStack = Util.findAmmo(player, false);
                if (itemStack != null) {
                    boolean k = Util.isAmmoTank(itemStack, true);
                    if (itemStack.getItem() == ItemRegister.COMPACTED_SNOWBALL.get() || itemStack.getItem() == ItemRegister.COMPACTED_SNOWBALL_STORAGE_TANK.get()) {
                        pushRank += 0.10;
                    } else if (itemStack.getItem() == ItemRegister.STONE_SNOWBALL.get() || itemStack.getItem() == ItemRegister.STONE_SNOWBALL_STORAGE_TANK.get()) {
                        pushRank += 0.12;
                    } else if (itemStack.getItem() == ItemRegister.GLASS_SNOWBALL.get() || itemStack.getItem() == ItemRegister.GLASS_SNOWBALL_STORAGE_TANK.get()) {
                        pushRank += 0.12;
                    } else if (itemStack.getItem() == ItemRegister.IRON_SNOWBALL.get() || itemStack.getItem() == ItemRegister.IRON_SNOWBALL_STORAGE_TANK.get()) {
                        pushRank += 0.16;
                    } else if (itemStack.getItem() == ItemRegister.ICE_SNOWBALL.get() || itemStack.getItem() == ItemRegister.ICE_SNOWBALL_STORAGE_TANK.get()) {
                        pushRank += 0.12;
                    } else if (itemStack.getItem() == ItemRegister.GOLD_SNOWBALL.get() || itemStack.getItem() == ItemRegister.GOLD_SNOWBALL_STORAGE_TANK.get()) {
                        pushRank += 0.18;
                    } else if (itemStack.getItem() == ItemRegister.OBSIDIAN_SNOWBALL.get() || itemStack.getItem() == ItemRegister.OBSIDIAN_SNOWBALL_STORAGE_TANK.get()) {
                        pushRank += 0.18;
                    } else if (itemStack.getItem() == ItemRegister.SPECTRAL_SNOWBALL.get() || itemStack.getItem() == ItemRegister.SPECTRAL_SNOWBALL_STORAGE_TANK.get()) {
                        pushRank += 0.10;
                    } else {
                        pushRank += 0.42;
                    }

                    stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(p.getUsedItemHand()));
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
                } else {
                    break;
                }
            }
            if (i > 0) {
                float pitch = player.getXRot();
                float yaw = player.getYRot();
                Vec3 cameraVec = new Vec3(-Mth.cos(pitch * Mth.DEG_TO_RAD) * Mth.sin(yaw * Mth.DEG_TO_RAD), -Mth.sin(pitch * Mth.DEG_TO_RAD), Mth.cos(pitch * Mth.DEG_TO_RAD) * Mth.cos(yaw * Mth.DEG_TO_RAD));
                if (level.isClientSide()) {
                    player.push(-pushRank * cameraVec.x, -pushRank * cameraVec.y, -pushRank * cameraVec.z);
                }
                if (!level.isClientSide()) {
                    ServerLevel serverLevel = (ServerLevel) level;
                    serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, player.getX() + cameraVec.x, player.getY() + cameraVec.y + 1.8, player.getZ() + cameraVec.z, (int) (29 * pushRank + 9.04), 0, 0, 0, 0.16);
                }
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundRegister.SHOTGUN_FIRE_1.get(), SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
            }
        }
        player.getCooldowns().addCooldown(this, 20);
        player.awardStat(Stats.ITEM_USED.get(this));
        return super.use(level, player, usedHand);
    }

}
