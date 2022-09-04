package com.linngdu664.bsf.item.snowball;

import com.linngdu664.bsf.item.setter.ItemRegister;
import com.linngdu664.bsf.util.Target;
import com.linngdu664.bsf.util.TrackingSnowballMode;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class TrackingSnowballItem extends Item {
    private final Target target;
    private final TrackingSnowballMode damageMode;

    public TrackingSnowballItem(Target target, TrackingSnowballMode damageMode) {
        super(new Properties().tab(ItemRegister.GROUP).stacksTo(16));
        this.target = target;
        this.damageMode = damageMode;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        if (pPlayer.getOffhandItem().getItem() == ItemRegister.EMPTY_SNOWBALL_STORAGE_TANK.get()) {
            if (target == Target.PLAYER) {
                switch (TrackingSnowballMode.getByType(damageMode.getType())) {
                    case NO_DAMAGE -> pPlayer.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(ItemRegister.PLAYER_TRACKING_SNOWBALL.get()));
                    case HAVE_DAMAGE -> pPlayer.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(ItemRegister.PLAYER_TRACKING_SNOWBALL_WITH_DAMAGE.get()));
                    case EXPLOSIVE -> pPlayer.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(ItemRegister.EXPLOSIVE_PLAYER_TRACKING_SNOWBALL.get()));
                }
            } else {
                switch (TrackingSnowballMode.getByType(damageMode.getType())) {
                    case NO_DAMAGE -> pPlayer.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(ItemRegister.MONSTER_TRACKING_SNOWBALL.get()));
                    case HAVE_DAMAGE -> pPlayer.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(ItemRegister.MONSTER_TRACKING_SNOWBALL_WITH_DAMAGE.get()));
                    case EXPLOSIVE -> pPlayer.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(ItemRegister.EXPLOSIVE_MONSTER_TRACKING_SNOWBALL.get()));
                }
            }
            pPlayer.getOffhandItem().setDamageValue(96 - pPlayer.getMainHandItem().getCount());
            if (!pPlayer.getAbilities().instabuild) {
                itemStack.shrink(pPlayer.getMainHandItem().getCount());
            }
        } else if ((pPlayer.getOffhandItem().getItem() == ItemRegister.PLAYER_TRACKING_SNOWBALL_STORAGE_TANK.get() && target == Target.PLAYER && damageMode == TrackingSnowballMode.NO_DAMAGE ||
                pPlayer.getOffhandItem().getItem() == ItemRegister.PLAYER_TRACKING_SNOWBALL_WITH_DAMAGE_STORAGE_TANK.get() && target == Target.PLAYER && damageMode == TrackingSnowballMode.HAVE_DAMAGE ||
                pPlayer.getOffhandItem().getItem() == ItemRegister.EXPLOSIVE_PLAYER_TRACKING_SNOWBALL_STORAGE_TANK.get() && target == Target.PLAYER && damageMode == TrackingSnowballMode.EXPLOSIVE ||
                pPlayer.getOffhandItem().getItem() == ItemRegister.MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get() && target == Target.MONSTER && damageMode == TrackingSnowballMode.NO_DAMAGE ||
                pPlayer.getOffhandItem().getItem() == ItemRegister.MONSTER_TRACKING_SNOWBALL_WITH_DAMAGE_STORAGE_TANK.get() && target == Target.MONSTER && damageMode == TrackingSnowballMode.HAVE_DAMAGE ||
                pPlayer.getOffhandItem().getItem() == ItemRegister.EXPLOSIVE_MONSTER_TRACKING_SNOWBALL_STORAGE_TANK.get() && target == Target.MONSTER && damageMode == TrackingSnowballMode.EXPLOSIVE)
                && pPlayer.getOffhandItem().getDamageValue() != 0) {
            if (pPlayer.getOffhandItem().getDamageValue() >= pPlayer.getMainHandItem().getCount()) {
                pPlayer.getOffhandItem().setDamageValue(pPlayer.getOffhandItem().getDamageValue() - pPlayer.getMainHandItem().getCount());
                if (!pPlayer.getAbilities().instabuild) {
                    itemStack.shrink(pPlayer.getMainHandItem().getCount());
                }
            } else {
                if (!pPlayer.getAbilities().instabuild) {
                    itemStack.shrink(pPlayer.getOffhandItem().getDamageValue());
                }
                pPlayer.getOffhandItem().setDamageValue(0);
            }
        }
        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(itemStack, pLevel.isClientSide());
    }
}
