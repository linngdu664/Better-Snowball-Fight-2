package com.linngdu664.bsf.item.weapon;

import com.linngdu664.bsf.SoundRegister;
import com.linngdu664.bsf.entity.AdvancedSnowballEntity;
import com.linngdu664.bsf.item.setter.ItemRegister;
import com.linngdu664.bsf.util.SnowballType;
import com.linngdu664.bsf.util.BSFUtil;
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
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SnowballCannonItem extends BowItem {
    public final int coreType;

    public SnowballCannonItem(int type) {
        super(new Properties().tab(ItemRegister.GROUP).stacksTo(1).durability(256));
        this.coreType = type;
    }

    @Override
    public void releaseUsing(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull LivingEntity pEntityLiving, int pTimeLeft) {
        if (pEntityLiving instanceof Player player) {
            int i = this.getUseDuration(pStack) - pTimeLeft;
            float f = getPowerForTime(i);
            if (f >= 0.1F) {
                ItemStack itemStack = BSFUtil.findAmmo(player, false);
                if (itemStack != null) {
                    boolean k = BSFUtil.isAmmoTank(itemStack, true);
                    AdvancedSnowballEntity snowballEntity;
                    if (itemStack.getItem() == ItemRegister.COMPACTED_SNOWBALL.get() || itemStack.getItem() == ItemRegister.COMPACTED_SNOWBALL_STORAGE_TANK.get()) {
                        snowballEntity = new AdvancedSnowballEntity(pLevel, player, SnowballType.COMPACTED);
                        snowballEntity.setItem(new ItemStack(ItemRegister.COMPACTED_SNOWBALL.get()));
                    } else if (itemStack.getItem() == ItemRegister.STONE_SNOWBALL.get() || itemStack.getItem() == ItemRegister.STONE_SNOWBALL_STORAGE_TANK.get()) {
                        snowballEntity = new AdvancedSnowballEntity(pLevel, player, SnowballType.STONE, f * 3.0F, f * 4.0F);
                        snowballEntity.setItem(new ItemStack(ItemRegister.STONE_SNOWBALL.get()));
                    } else if (itemStack.getItem() == ItemRegister.GLASS_SNOWBALL.get() || itemStack.getItem() == ItemRegister.GLASS_SNOWBALL_STORAGE_TANK.get()) {
                        snowballEntity = new AdvancedSnowballEntity(pLevel, player, SnowballType.GLASS, f * 4.0F, f * 5.0F);
                        snowballEntity.setItem(new ItemStack(ItemRegister.GLASS_SNOWBALL.get()));
                    } else if (itemStack.getItem() == ItemRegister.IRON_SNOWBALL.get() || itemStack.getItem() == ItemRegister.IRON_SNOWBALL_STORAGE_TANK.get()) {
                        snowballEntity = new AdvancedSnowballEntity(pLevel, player, SnowballType.IRON, f * 5.0F, f * 7.0F);
                        snowballEntity.setItem(new ItemStack(ItemRegister.IRON_SNOWBALL.get()));
                    } else if (itemStack.getItem() == ItemRegister.ICE_SNOWBALL.get() || itemStack.getItem() == ItemRegister.ICE_SNOWBALL_STORAGE_TANK.get()) {
                        snowballEntity = new AdvancedSnowballEntity(pLevel, player, SnowballType.ICE, f * 4.0F, f * 6.0F);
                        snowballEntity.frozenTicks = 60;
                        snowballEntity.setItem(new ItemStack(ItemRegister.ICE_SNOWBALL.get()));
                    } else if (itemStack.getItem() == ItemRegister.GOLD_SNOWBALL.get() || itemStack.getItem() == ItemRegister.GOLD_SNOWBALL_STORAGE_TANK.get()) {
                        snowballEntity = new AdvancedSnowballEntity(pLevel, player, SnowballType.GOLD, f * 6.0F, f * 7.0F);
                        snowballEntity.setItem(new ItemStack(ItemRegister.GOLD_SNOWBALL.get()));
                    } else if (itemStack.getItem() == ItemRegister.OBSIDIAN_SNOWBALL.get() || itemStack.getItem() == ItemRegister.OBSIDIAN_SNOWBALL_STORAGE_TANK.get()) {
                        snowballEntity = new AdvancedSnowballEntity(pLevel, player, SnowballType.OBSIDIAN, f * 7.0F, f * 8.0F);
                        snowballEntity.setItem(new ItemStack(ItemRegister.OBSIDIAN_SNOWBALL.get()));
                    } else if (itemStack.getItem() == ItemRegister.EXPLOSIVE_SNOWBALL.get() || itemStack.getItem() == ItemRegister.EXPLOSIVE_SNOWBALL_STORAGE_TANK.get()){
                        snowballEntity = new AdvancedSnowballEntity(pLevel, player, SnowballType.EXPLOSIVE, f * 3.0F, f * 5.0F);
                        snowballEntity.setItem(new ItemStack(ItemRegister.EXPLOSIVE_SNOWBALL.get()));
                    } else if (itemStack.getItem() == ItemRegister.MONSTER_TRACKING_SNOWBALL.get() || itemStack.getItem() == ItemRegister.MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get()) {
                        snowballEntity = new AdvancedSnowballEntity(pLevel, player, SnowballType.TRACKING_MONSTER);
                        snowballEntity.setMissilesTracking(Monster.class, 20, true);
                        snowballEntity.setItem(new ItemStack(ItemRegister.MONSTER_TRACKING_SNOWBALL.get()));
                    } else if (itemStack.getItem() == ItemRegister.MONSTER_TRACKING_SNOWBALL_WITH_DAMAGE.get() || itemStack.getItem() == ItemRegister.MONSTER_TRACKING_SNOWBALL_WITH_DAMAGE_STORAGE_TANK.get()) {
                        snowballEntity = new AdvancedSnowballEntity(pLevel, player, SnowballType.TRACKING_MONSTER_DAMAGE, f * 4.0F, f * 6.0F);
                        snowballEntity.setMissilesTracking(Monster.class, 20, true);
                        snowballEntity.setItem(new ItemStack(ItemRegister.MONSTER_TRACKING_SNOWBALL_WITH_DAMAGE.get()));
                    } else if (itemStack.getItem() == ItemRegister.EXPLOSIVE_MONSTER_TRACKING_SNOWBALL.get() || itemStack.getItem() == ItemRegister.EXPLOSIVE_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get()) {
                        snowballEntity = new AdvancedSnowballEntity(pLevel, player, SnowballType.TRACKING_MONSTER_EXPLOSIVE, f * 3.0F, f * 5.0F);
                        snowballEntity.setMissilesTracking(Monster.class, 20, true);
                        snowballEntity.setItem(new ItemStack(ItemRegister.EXPLOSIVE_MONSTER_TRACKING_SNOWBALL.get()));
                    } else if (itemStack.getItem() == ItemRegister.PLAYER_TRACKING_SNOWBALL.get() || itemStack.getItem() == ItemRegister.PLAYER_TRACKING_SNOWBALL_STORAGE_TANK.get()) {
                        snowballEntity = new AdvancedSnowballEntity(pLevel, player, SnowballType.TRACKING_PLAYER);
                        snowballEntity.setMissilesTracking(Player.class, 20, true);
                        snowballEntity.setItem(new ItemStack(ItemRegister.PLAYER_TRACKING_SNOWBALL.get()));
                    } else if (itemStack.getItem() == ItemRegister.PLAYER_TRACKING_SNOWBALL_WITH_DAMAGE.get() || itemStack.getItem() == ItemRegister.PLAYER_TRACKING_SNOWBALL_WITH_DAMAGE_STORAGE_TANK.get()) {
                        snowballEntity = new AdvancedSnowballEntity(pLevel, player, SnowballType.TRACKING_PLAYER_DAMAGE, f * 4.0F, f * 6.0F);
                        snowballEntity.setMissilesTracking(Player.class, 20, true);
                        snowballEntity.setItem(new ItemStack(ItemRegister.PLAYER_TRACKING_SNOWBALL_WITH_DAMAGE.get()));
                    } else {
                        snowballEntity = new AdvancedSnowballEntity(pLevel, player, SnowballType.TRACKING_PLAYER_EXPLOSIVE, f * 3.0F, f * 5.0F);
                        snowballEntity.setMissilesTracking(Player.class, 20, true);
                        snowballEntity.setItem(new ItemStack(ItemRegister.EXPLOSIVE_PLAYER_TRACKING_SNOWBALL.get()));
                    }
                    snowballEntity.punch = f * 1.51F;
                    BSFUtil.shootFromRotation(snowballEntity, player.getXRot(), player.getYRot(), 0.0F, f * 3.0F, 1.0F);
                    if (coreType == 1) {
                        snowballEntity.frozenTicks += 140;
                        switch (snowballEntity.type) {
                            case COMPACTED, TRACKING_MONSTER, TRACKING_PLAYER -> snowballEntity.blazeDamage = f * 4.0F;
                            case STONE -> snowballEntity.blazeDamage = f * 5.0F;
                            case GLASS -> snowballEntity.blazeDamage = f * 6.0F;
                            case IRON, TRACKING_MONSTER_DAMAGE, TRACKING_PLAYER_DAMAGE -> snowballEntity.blazeDamage = f * 7.0F;
                            case GOLD -> snowballEntity.blazeDamage = f * 8.0F;
                            case OBSIDIAN -> snowballEntity.blazeDamage = f * 9.0F;
                            case EXPLOSIVE, TRACKING_PLAYER_EXPLOSIVE, TRACKING_MONSTER_EXPLOSIVE -> snowballEntity.blazeDamage = 5.0F;
                            case ICE -> {
                                snowballEntity.blazeDamage = f * 10.0F;
                                snowballEntity.frozenTicks = 200;
                            }
                        }
                    } else if (coreType == 2) {
                        snowballEntity.punch = f * 2.5;
                        BSFUtil.shootFromRotation(snowballEntity, player.getXRot(), player.getYRot(), 0.0F, f * 4.0F, 1.0F);
                        snowballEntity.weaknessTicks = 180;
                    }
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
                        serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, player.getX() + cameraVec.x, player.getEyeY() + cameraVec.y , player.getZ() + cameraVec.z, 16, 0, 0, 0, 0.16);
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
    public boolean isEnchantable(@NotNull ItemStack pStack) {
        return false;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pHand);
        pPlayer.startUsingItem(pHand);
        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack pStack, ItemStack pRepairCandidate) {
        return pRepairCandidate.is(Items.IRON_INGOT);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TranslatableComponent("snowball_cannon.tooltip").withStyle(ChatFormatting.GRAY));
    }
}