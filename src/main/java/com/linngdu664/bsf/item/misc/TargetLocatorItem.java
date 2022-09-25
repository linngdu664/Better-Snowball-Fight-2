package com.linngdu664.bsf.item.misc;

import com.linngdu664.bsf.entity.BSFSnowballEntity;
import com.linngdu664.bsf.entity.snowball.nomal_snowball.GPSSnowballEntity;
import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.item.snowball.normal_snowball.CompactedSnowballItem;
import com.linngdu664.bsf.item.tank.normal_snowball.CompactedSnowballStorageTank;
import com.linngdu664.bsf.util.ItemGroup;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class TargetLocatorItem extends Item {
    private LivingEntity livingEntity;

    public TargetLocatorItem() {
        super(new Properties().tab(ItemGroup.MAIN).rarity(Rarity.UNCOMMON).stacksTo(1).durability(100));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        int i;
        for (i = 0; i < pPlayer.getInventory().getContainerSize(); i++) {
            if (pPlayer.getInventory().getItem(i).getItem() instanceof CompactedSnowballItem ||
                    pPlayer.getInventory().getItem(i).getItem() instanceof CompactedSnowballStorageTank) {
                break;
            }
        }
        if (i != pPlayer.getInventory().getContainerSize()) {
            pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
            if (!pLevel.isClientSide) {
                BSFSnowballEntity snowballEntity = new GPSSnowballEntity(pPlayer, pLevel, this);
                snowballEntity.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 2.0F, 1.0F);
                pLevel.addFreshEntity(snowballEntity);
            }
            if (!pPlayer.getAbilities().instabuild) {
                ItemStack stack = pPlayer.getInventory().getItem(i);
                if (stack.getItem() instanceof CompactedSnowballItem) {
                    stack.shrink(1);
                    if (stack.isEmpty()) {
                        pPlayer.getInventory().removeItem(stack);
                    }
                } else {
                    stack.hurtAndBreak(1, pPlayer, p -> p.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.EMPTY_SNOWBALL_STORAGE_TANK.get()), true));
                }
                itemStack.hurtAndBreak(1, pPlayer, p -> p.broadcastBreakEvent(pUsedHand));
            }
        }
        return InteractionResultHolder.pass(itemStack);
    }

    public LivingEntity getLivingEntity() {
        return livingEntity;
    }

    public void setLivingEntity(LivingEntity livingEntity) {
        this.livingEntity = livingEntity;
    }
}
