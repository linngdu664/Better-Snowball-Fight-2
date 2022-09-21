package com.linngdu664.bsf.item.misc;

import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.util.ItemGroup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

public class UnstableCoreItem extends Item {
    public UnstableCoreItem() {
        super(new Properties().tab(ItemGroup.MAIN).rarity(Rarity.EPIC));
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        ItemStack itemStack = context.getItemInHand();
        Level level = context.getLevel();
        Block block = level.getBlockState(context.getClickedPos()).getBlock();
        if (block == Blocks.LODESTONE) {
            ((ServerLevel) level).sendParticles(ParticleTypes.DRAGON_BREATH, context.getClickedPos().getX(), context.getClickedPos().getY(), context.getClickedPos().getZ(), 64, 0, 0, 0, 0.12);
            itemStack.shrink(1);
            assert player != null;
            player.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.GRAVITY_CORE.get(), 1), true);
            player.getInventory().placeItemBackInInventory(new ItemStack(ItemRegister.REPULSION_CORE.get(), 1), true);
        }
        return super.useOn(context);
    }
}
