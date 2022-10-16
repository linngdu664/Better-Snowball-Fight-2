package com.linngdu664.bsf.event;

import com.linngdu664.bsf.item.misc.SnowFallBootsItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LivingFallEvent {
    @SubscribeEvent
    public void LivingFall(net.minecraftforge.event.entity.living.LivingFallEvent event) {
        if (event.getEntity() instanceof Player player) {
            ItemStack shoes = player.getItemBySlot(EquipmentSlot.FEET);
            Level level = player.getLevel();
            Block block1 = level.getBlockState(new BlockPos(player.getX(), player.getY(), player.getZ())).getBlock();
            Block block2 = level.getBlockState(new BlockPos(player.getX(), player.getY() - 1, player.getZ())).getBlock();
            if (!level.isClientSide && shoes.getItem() instanceof SnowFallBootsItem && (block1.equals(Blocks.SNOW) || block2.equals(Blocks.SNOW_BLOCK))) {
                event.setDamageMultiplier(0);
                shoes.hurtAndBreak((int) Math.ceil((event.getDistance() - 3) * 0.25), player, (p) -> p.broadcastBreakEvent(EquipmentSlot.FEET));
            }
        }
    }
}
