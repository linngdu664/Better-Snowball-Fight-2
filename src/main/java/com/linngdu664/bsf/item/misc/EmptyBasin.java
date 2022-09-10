package com.linngdu664.bsf.item.misc;

import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.util.ItemGroup;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

public class EmptyBasin extends Item {
    public EmptyBasin() {
        super(new Properties().tab(ItemGroup.MAIN).stacksTo(16));
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        Player player = pContext.getPlayer();
        Level level = pContext.getLevel();
        ItemStack itemStack = pContext.getItemInHand();
        Block block = level.getBlockState(pContext.getClickedPos()).getBlock();
        assert player != null;
        if (block == Blocks.SNOW_BLOCK || block == Blocks.SNOW) {
            player.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.BASIN_OF_SNOW.get()), true);
            itemStack.shrink(1);
        } else if (block == Blocks.POWDER_SNOW) {
            player.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.BASIN_OF_POWDER_SNOW.get()), true);
            itemStack.shrink(1);
        }
        return super.useOn(pContext);
    }
}