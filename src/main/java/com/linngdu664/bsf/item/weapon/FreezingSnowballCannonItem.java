package com.linngdu664.bsf.item.weapon;

import com.linngdu664.bsf.entity.BSFSnowballEntity;
import com.linngdu664.bsf.entity.snowball.nomal_snowball.FrozenSnowballEntity;
import com.linngdu664.bsf.entity.snowball.nomal_snowball.IceSnowballEntity;
import com.linngdu664.bsf.util.LaunchFrom;
import com.linngdu664.bsf.util.LaunchFunc;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FreezingSnowballCannonItem extends SnowballCannonItem {
    public static LaunchFunc getLaunchFunc(double damageDropRate) {
        return new LaunchFunc() {
            @Override
            public LaunchFrom getLaunchForm() {
                return LaunchFrom.FREEZING_CANNON;
            }

            @Override
            public void launchProperties(BSFSnowballEntity bsfSnowballEntity) {
                if (bsfSnowballEntity instanceof IceSnowballEntity || bsfSnowballEntity instanceof FrozenSnowballEntity) {
                    bsfSnowballEntity.blazeDamage += 4;
                    bsfSnowballEntity.frozenTime = 200;
                } else {
                    bsfSnowballEntity.blazeDamage += 1;
                    bsfSnowballEntity.frozenTime = 140;
                }
                bsfSnowballEntity.blazeDamage *= damageDropRate;
                bsfSnowballEntity.punch = damageDropRate * 1.51F;
            }
        };
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TranslatableComponent("snowball_cannon4.tooltip").withStyle(ChatFormatting.BLUE));
        pTooltipComponents.add(new TranslatableComponent("snowball_cannon2.tooltip").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(new TranslatableComponent("snowball_cannon3.tooltip").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(new TranslatableComponent("snowball_cannon.tooltip").withStyle(ChatFormatting.DARK_AQUA));
    }
}
