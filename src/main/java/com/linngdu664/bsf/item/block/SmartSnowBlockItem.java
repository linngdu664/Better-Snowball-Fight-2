package com.linngdu664.bsf.item.block;

import com.linngdu664.bsf.util.ItemGroup;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class SmartSnowBlockItem extends BlockItem {
    public SmartSnowBlockItem(Block pBlock) {
        super(pBlock, new Properties().tab(ItemGroup.MAIN));
    }
}
