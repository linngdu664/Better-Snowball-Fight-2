package com.linngdu664.bsf.item;

import com.linngdu664.bsf.SoundRegister;
import com.linngdu664.bsf.entity.AdvancedSnowballEntity;
import com.linngdu664.bsf.item.setter.ItemRegister;
import com.linngdu664.bsf.util.SnowballType;
import com.linngdu664.bsf.util.Util;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class SnowballShotgunItem extends Item {
    public SnowballShotgunItem() {
        super(new Properties().tab(ItemRegister.GROUP).stacksTo(1).durability(256));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        ItemStack itemStack = Util.findAmmo(player, false);
        for (int i = 0; i < 4; i++) {
            if (itemStack != null && !level.isClientSide) {
                boolean k = Util.isAmmoTank(itemStack);
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
                } else {
                    snowballEntity = new AdvancedSnowballEntity(level, player, SnowballType.EXPLOSIVE, 3.0F, 5.0F);
                    snowballEntity.setItem(new ItemStack(ItemRegister.EXPLOSIVE_SNOWBALL.get()));
                }
                snowballEntity.punch = 1.51F;
                snowballEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.0F, 10.0F);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundRegister.SNOWBALL_CANNON_SHOOT.get(), SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
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
            }
        }
        stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(p.getUsedItemHand()));
        player.getCooldowns().addCooldown(this, 20);
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.pass(stack);
    }
}
