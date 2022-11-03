package com.linngdu664.bsf.event;

import com.linngdu664.bsf.enchantment.BSFEnchantments;
import com.linngdu664.bsf.item.misc.SnowFallBootsItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LivingFallEvent {
    @SubscribeEvent
    public void livingFall(net.minecraftforge.event.entity.living.LivingFallEvent event) {
        if (event.getEntity() instanceof Player player) {
            Level level = player.getLevel();
            ItemStack shoes = player.getItemBySlot(EquipmentSlot.FEET);
            if (!level.isClientSide && shoes.getItem() instanceof SnowFallBootsItem) {
                Block block1 = level.getBlockState(new BlockPos(player.getX(), player.getY(), player.getZ())).getBlock();
                Block block2 = level.getBlockState(new BlockPos(player.getX(), player.getY() - 1, player.getZ())).getBlock();
                if (block1.equals(Blocks.SNOW) || block2.equals(Blocks.SNOW_BLOCK) || snowAroundPlayer(level, player, block1)) {
                    event.setDamageMultiplier(0);
                    float h = event.getDistance();
                    if (level instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.SNOWFLAKE, player.getX(), player.getY(), player.getZ(), (int) h * 8, 0, 0, 0, h * 0.01);
                    }
                    shoes.hurtAndBreak((int) Math.ceil((h - 3) * 0.25), player, (p) -> p.broadcastBreakEvent(EquipmentSlot.FEET));
                    level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOW_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
                    if(EnchantmentHelper.getItemEnchantmentLevel(BSFEnchantments.KINETIC_ENERGY_STORAGE.get(),shoes)>0 && h>5){
                        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, (int)h*6, EnchantmentHelper.getItemEnchantmentLevel(BSFEnchantments.KINETIC_ENERGY_STORAGE.get(),shoes)-1));
                    }
                }
            }
        }
    }

    private boolean snowAroundPlayer(Level level, Player player, Block block1) {
        if (block1.equals(Blocks.AIR)) {
            boolean b = false;
            ok:
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (level.getBlockState(new BlockPos(player.getX() + i, player.getY(), player.getZ() + j)).getBlock().equals(Blocks.SNOW) || level.getBlockState(new BlockPos(player.getX() + i, player.getY() - 1, player.getZ() + j)).getBlock().equals(Blocks.SNOW_BLOCK)) {
                        b = true;
                        break ok;
                    }
                }
            }
            return b;
        } else {
            return false;
        }
    }
}
