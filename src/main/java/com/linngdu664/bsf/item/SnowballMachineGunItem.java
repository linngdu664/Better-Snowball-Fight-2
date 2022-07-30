package com.linngdu664.bsf.item;

import com.linngdu664.bsf.SoundRegister;
import com.linngdu664.bsf.entity.AdvancedSnowballEntity;
import com.linngdu664.bsf.entity.SnowballType;
import com.linngdu664.bsf.item.setter.ItemRegister;
import com.linngdu664.bsf.item.setter.ModGroup;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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
    private static float j;

    public SnowballMachineGunItem() {
        super(new Properties().tab(ModGroup.group).stacksTo(1).durability(512));
    }

    protected boolean isAmmoTank(ItemStack stack) {
        return stack.getItem() == ItemRegister.COMPACTED_SNOWBALL_STORAGE_TANK.get() || stack.getItem() == ItemRegister.EXPLOSIVE_SNOWBALL_STORAGE_TANK.get() ||
                stack.getItem() == ItemRegister.GLASS_SNOWBALL_STORAGE_TANK.get() || stack.getItem() == ItemRegister.GOLD_SNOWBALL_STORAGE_TANK.get() ||
                stack.getItem() == ItemRegister.ICE_SNOWBALL_STORAGE_TANK.get() || stack.getItem() == ItemRegister.IRON_SNOWBALL_STORAGE_TANK.get() ||
                stack.getItem() == ItemRegister.OBSIDIAN_SNOWBALL_STORAGE_TANK.get() || stack.getItem() == ItemRegister.STONE_SNOWBALL_STORAGE_TANK.get();
    }

    protected ItemStack findAmmo(Player player) {
        for (int j = 0; j < player.getInventory().getContainerSize(); j++) {
            if (isAmmoTank(player.getInventory().getItem(j))) {
                return player.getInventory().getItem(j);
            }
        }
        return new ItemStack(Items.AIR);
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
            ItemStack itemStack = findAmmo(player);
            if (itemStack.getItem() != Items.AIR) {
                AdvancedSnowballEntity snowballEntity;
                if (itemStack.getItem() == ItemRegister.COMPACTED_SNOWBALL_STORAGE_TANK.get()) {
                    snowballEntity = new AdvancedSnowballEntity(pLevel, player, SnowballType.COMPACTED);
                    snowballEntity.setItem(new ItemStack(ItemRegister.COMPACTED_SNOWBALL.get()));
                    j = 0.08F;
                } else if (itemStack.getItem() == ItemRegister.STONE_SNOWBALL_STORAGE_TANK.get()) {
                    snowballEntity = new AdvancedSnowballEntity(pLevel, player, SnowballType.STONE, 3.0F, 4.0F);
                    snowballEntity.setItem(new ItemStack(ItemRegister.STONE_SNOWBALL.get()));
                    j = 0.1F;
                } else if (itemStack.getItem() == ItemRegister.GLASS_SNOWBALL_STORAGE_TANK.get()) {
                    snowballEntity = new AdvancedSnowballEntity(pLevel, player, SnowballType.GLASS, 4.0F, 5.0F);
                    snowballEntity.setItem(new ItemStack(ItemRegister.GLASS_SNOWBALL.get()));
                    j = 0.1F;
                } else if (itemStack.getItem() == ItemRegister.IRON_SNOWBALL_STORAGE_TANK.get()) {
                    snowballEntity = new AdvancedSnowballEntity(pLevel, player, SnowballType.IRON, 5.0F, 7.0F);
                    snowballEntity.setItem(new ItemStack(ItemRegister.IRON_SNOWBALL.get()));
                    j = 0.12F;
                } else if (itemStack.getItem() == ItemRegister.ICE_SNOWBALL_STORAGE_TANK.get()) {
                    snowballEntity = new AdvancedSnowballEntity(pLevel, player, SnowballType.ICE, 4.0F, 6.0F);
                    snowballEntity.frozenTicks = 60;
                    snowballEntity.setItem(new ItemStack(ItemRegister.ICE_SNOWBALL.get()));
                    j = 0.1F;
                } else if (itemStack.getItem() == ItemRegister.GOLD_SNOWBALL_STORAGE_TANK.get()) {
                    snowballEntity = new AdvancedSnowballEntity(pLevel, player, SnowballType.GOLD, 6.0F, 7.0F);
                    snowballEntity.setItem(new ItemStack(ItemRegister.GOLD_SNOWBALL.get()));
                    j = 0.14F;
                } else if (itemStack.getItem() == ItemRegister.OBSIDIAN_SNOWBALL_STORAGE_TANK.get()) {
                    snowballEntity = new AdvancedSnowballEntity(pLevel, player, SnowballType.OBSIDIAN, 7.0F, 8.0F);
                    snowballEntity.setItem(new ItemStack(ItemRegister.OBSIDIAN_SNOWBALL.get()));
                    j = 0.17F;
                } else {
                    snowballEntity = new AdvancedSnowballEntity(pLevel, player, SnowballType.EXPLOSIVE, 3.0F, 5.0F);
                    snowballEntity.explode = true;
                    snowballEntity.setItem(new ItemStack(ItemRegister.EXPLOSIVE_SNOWBALL.get()));
                    j = 0.12F;
                }
                snowballEntity.punch = 1.2;
                snowballEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.6F, 1.0F);
                pLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundRegister.SNOWBALL_MACHINE_GUN_SHOOT.get(), SoundSource.PLAYERS, 1.0F, 1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);pLevel.addFreshEntity(snowballEntity);
                Vec3 cameraVec = new Vec3(-Mth.cos(pitch * Mth.DEG_TO_RAD) * Mth.sin(yaw * Mth.DEG_TO_RAD), -Mth.sin(pitch * Mth.DEG_TO_RAD), Mth.cos(pitch * Mth.DEG_TO_RAD) * Mth.cos(yaw * Mth.DEG_TO_RAD));
                player.push(-0.24 * j * cameraVec.x, -0.24 * j * cameraVec.y, -0.24 * j * cameraVec.z);
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
                j = 0;
            }
        }
        if (pitch > -90.0F) {
            player.setXRot(pitch - j);
        }
        timer++;
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
        pTooltipComponents.add(new TranslatableComponent("snowball_machine_gun.tooltip").withStyle(ChatFormatting.GRAY));
    }
}
