package com.linngdu664.bsf.item;

import com.linngdu664.bsf.entity.AdvancedSnowballEntity;
import com.linngdu664.bsf.entity.SnowballType;
import com.linngdu664.bsf.item.setter.ItemRegister;
import com.linngdu664.bsf.item.setter.ModGroup;
import com.linngdu664.bsf.sound.SoundRegister;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SnowballMachineGunItem extends Item {
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
        return new ItemStack(Items.AIR, 0);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        pPlayer.getCooldowns().addCooldown(this, 3);
        ItemStack itemStack = findAmmo(pPlayer);
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (itemStack.getItem() != Items.AIR) {
            float j;
            AdvancedSnowballEntity snowballEntity;
            if (itemStack.getItem() == ItemRegister.COMPACTED_SNOWBALL.get() || itemStack.getItem() == ItemRegister.COMPACTED_SNOWBALL_STORAGE_TANK.get()) {
                snowballEntity = new AdvancedSnowballEntity(pLevel, pPlayer, SnowballType.COMPACTED);
                snowballEntity.setItem(new ItemStack(ItemRegister.COMPACTED_SNOWBALL.get()));
                j = 0.7F;
            } else if (itemStack.getItem() == ItemRegister.STONE_SNOWBALL.get() || itemStack.getItem() == ItemRegister.STONE_SNOWBALL_STORAGE_TANK.get()) {
                snowballEntity = new AdvancedSnowballEntity(pLevel, pPlayer, SnowballType.STONE, 3.0F, 4.0F);
                snowballEntity.setItem(new ItemStack(ItemRegister.STONE_SNOWBALL.get()));
                j = 0.94F;
            } else if (itemStack.getItem() == ItemRegister.GLASS_SNOWBALL.get() || itemStack.getItem() == ItemRegister.GLASS_SNOWBALL_STORAGE_TANK.get()) {
                snowballEntity = new AdvancedSnowballEntity(pLevel, pPlayer, SnowballType.GLASS, 4.0F, 5.0F);
                snowballEntity.setItem(new ItemStack(ItemRegister.GLASS_SNOWBALL.get()));
                j = 0.94F;
            } else if (itemStack.getItem() == ItemRegister.IRON_SNOWBALL.get() || itemStack.getItem() == ItemRegister.IRON_SNOWBALL_STORAGE_TANK.get()) {
                snowballEntity = new AdvancedSnowballEntity(pLevel, pPlayer, SnowballType.IRON, 5.0F, 7.0F);
                snowballEntity.setItem(new ItemStack(ItemRegister.IRON_SNOWBALL.get()));
                j = 1.05F;
            } else if (itemStack.getItem() == ItemRegister.ICE_SNOWBALL.get() || itemStack.getItem() == ItemRegister.ICE_SNOWBALL_STORAGE_TANK.get()) {
                snowballEntity = new AdvancedSnowballEntity(pLevel, pPlayer, SnowballType.ICE, 4.0F, 6.0F);
                snowballEntity.frozenTicks = 60;
                snowballEntity.setItem(new ItemStack(ItemRegister.ICE_SNOWBALL.get()));
                j = 0.94F;
            } else if (itemStack.getItem() == ItemRegister.GOLD_SNOWBALL.get() || itemStack.getItem() == ItemRegister.GOLD_SNOWBALL_STORAGE_TANK.get()) {
                snowballEntity = new AdvancedSnowballEntity(pLevel, pPlayer, SnowballType.GOLD, 6.0F, 7.0F);
                snowballEntity.setItem(new ItemStack(ItemRegister.GOLD_SNOWBALL.get()));
                j = 1.24F;
            } else if (itemStack.getItem() == ItemRegister.OBSIDIAN_SNOWBALL.get() || itemStack.getItem() == ItemRegister.OBSIDIAN_SNOWBALL_STORAGE_TANK.get()) {
                snowballEntity = new AdvancedSnowballEntity(pLevel, pPlayer, SnowballType.OBSIDIAN, 7.0F, 8.0F);
                snowballEntity.setItem(new ItemStack(ItemRegister.OBSIDIAN_SNOWBALL.get()));
                j = 1.5F;
            } else {
                snowballEntity = new AdvancedSnowballEntity(pLevel, pPlayer, SnowballType.EXPLOSIVE, 3.0F, 5.0F);
                snowballEntity.explode = true;
                snowballEntity.setItem(new ItemStack(ItemRegister.EXPLOSIVE_SNOWBALL.get()));
                j = 1.16F;
            }
            snowballEntity.punch = 1.2;
            snowballEntity.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 2.6F, 1.0F);
            pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundRegister.SNOWBALL_MACHINE_GUN_SHOOT.get(), SoundSource.PLAYERS, 1.0F, 1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
            pLevel.addFreshEntity(snowballEntity);
            stack.hurtAndBreak(1, pPlayer, (p) -> p.broadcastBreakEvent(p.getUsedItemHand()));
            if (!pPlayer.getAbilities().instabuild) {
                itemStack.setDamageValue(itemStack.getDamageValue() + 1);
                if (itemStack.getDamageValue() == 96) {
                    itemStack.shrink(1);
                    pPlayer.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.EMPTY_SNOWBALL_STORAGE_TANK.get(), 1), true);
                }
            }
            float pitch = pPlayer.getXRot(), yaw = pPlayer.getYRot();
            if (pitch > -90.0F) {
                pPlayer.setXRot(pitch - j);
            }
            pitch *= 0.01745329F;
            yaw *= 0.01745329F;
            Vec3 cameraVec = new Vec3(-Mth.cos(pitch) * Mth.sin(yaw), -Mth.sin(pitch), Mth.cos(pitch) * Mth.cos(yaw));
            pPlayer.push(-0.05 * j * cameraVec.x, -0.05 * j * cameraVec.y, -0.05 * j * cameraVec.z);
            pPlayer.awardStat(Stats.ITEM_USED.get(this));
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public boolean isValidRepairItem(ItemStack pStack, ItemStack pRepairCandidate) {
        return pRepairCandidate.is(Items.IRON_INGOT);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TranslatableComponent("snowball_machine_gun.tooltip").withStyle(ChatFormatting.GRAY));
    }
}
