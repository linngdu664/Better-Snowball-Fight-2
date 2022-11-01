package com.linngdu664.bsf.item.weapon;

import com.linngdu664.bsf.Main;
import com.linngdu664.bsf.entity.AbstractBSFSnowballEntity;
import com.linngdu664.bsf.network.ForwardConeParticlesSender;
import com.linngdu664.bsf.util.LaunchFrom;
import com.linngdu664.bsf.util.LaunchFunc;
import com.linngdu664.bsf.util.SoundRegister;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SnowballCannonItem extends AbstractBSFWeaponItem {
    public SnowballCannonItem() {
        super(256, Rarity.RARE);
    }

    public static float getPowerForTime(int pCharge) {
        float f = (float) pCharge / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }

    public static LaunchFunc getLaunchFunc(double damageDropRate) {
        return new LaunchFunc() {
            @Override
            public LaunchFrom getLaunchForm() {
                return LaunchFrom.CANNON;
            }

            @Override
            public void launchProperties(AbstractBSFSnowballEntity bsfSnowballEntity) {
                bsfSnowballEntity.punch = damageDropRate * 1.51F;
            }
        };
    }

    @Override
    public void releaseUsing(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull LivingEntity pEntityLiving, int pTimeLeft) {
        if (pEntityLiving instanceof Player player) {
            int i = this.getUseDuration(pStack) - pTimeLeft;
            float f = getPowerForTime(i);
            if (f >= 0.1F) {
                ItemStack itemStack = findAmmo(player, false, true);
                if (itemStack != null) {
                    AbstractBSFSnowballEntity snowballEntity = ItemToEntity(itemStack.getItem(), player, pLevel, getLaunchFunc(f));
                    BSFShootFromRotation(snowballEntity, player.getXRot(), player.getYRot(), f * 3.0F, 1.0F);
                    pLevel.addFreshEntity(snowballEntity);
                    pStack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(p.getUsedItemHand()));
                    Vec3 cameraVec = Vec3.directionFromRotation(player.getXRot(), player.getYRot());
                    //add push
                    if (pLevel.isClientSide()) {
                        player.push(-0.2 * cameraVec.x * f, -0.2 * cameraVec.y * f, -0.2 * cameraVec.z * f);
                        //add particles
                    } else {
                        Main.PACKET_HANDLER.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new ForwardConeParticlesSender(player, cameraVec, 4.5F, 90, 1.5F, 0.1));
                        pLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundRegister.SNOWBALL_CANNON_SHOOT.get(), SoundSource.PLAYERS, 1.0F, 1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    }
                    consumeAmmo(itemStack, player);
                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }


    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pHand);
        pPlayer.startUsingItem(pHand);
        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack pStack) {
        return 72000;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TranslatableComponent("snowball_cannon1.tooltip").withStyle(ChatFormatting.BLUE));
        pTooltipComponents.add(new TranslatableComponent("snowball_cannon2.tooltip").withStyle(ChatFormatting.BLUE));
        pTooltipComponents.add(new TranslatableComponent("snowball_cannon3.tooltip").withStyle(ChatFormatting.BLUE));
        pTooltipComponents.add(new TranslatableComponent("snowball_cannon.tooltip").withStyle(ChatFormatting.DARK_AQUA));
    }
}
