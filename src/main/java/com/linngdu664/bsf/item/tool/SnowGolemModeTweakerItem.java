package com.linngdu664.bsf.item.tool;

import com.linngdu664.bsf.util.ItemGroup;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SnowGolemModeTweakerItem extends Item {
    private int state=0;
    private boolean useLocator=false;

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if(pLevel.isClientSide){
            if(pPlayer.isShiftKeyDown()){
                useLocator=!useLocator;
                pPlayer.sendMessage(useLocator ? new TranslatableComponent("snow_golem_locator_true.tip") : new TranslatableComponent("snow_golem_locator_false.tip"), Util.NIL_UUID);
            }else{
                state=(state+1)%5;
                pPlayer.sendMessage(new TranslatableComponent(switch (state) {
                    case 0 -> "snow_golem_standby.tip";
                    case 1 -> "snow_golem_follow.tip";
                    case 2 -> "snow_golem_follow_and_attack.tip";
                    case 3 -> "snow_golem_attack.tip";
                    default -> "snow_golem_turret.tip";
                }), Util.NIL_UUID);
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isUseLocator() {
        return useLocator;
    }

    public void setUseLocator(boolean useLocator) {
        this.useLocator = useLocator;
    }

    public SnowGolemModeTweakerItem() {
        super(new Properties().tab(ItemGroup.MAIN).stacksTo(1));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
