package com.linngdu664.bsf.item.misc;

import com.linngdu664.bsf.util.BSFMthUtil;
import com.linngdu664.bsf.util.ItemGroup;
import com.linngdu664.bsf.util.TargetGetter;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RepulsiveFieldGenerator extends Item {
    public RepulsiveFieldGenerator() {
        super(new Properties().tab(ItemGroup.MAIN).rarity(Rarity.RARE).stacksTo(1));
    }

    @Override
    public void releaseUsing(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull LivingEntity pLivingEntity, int pTimeCharged) {
        Player player = (Player) pLivingEntity;
        int t = getUseDuration(pStack) - pTimeCharged;
        if (t > 10) {
            t = 10;
        }
        double r = 2 + 0.1 * t;
        float angleCos = Mth.cos((10.0F + t) * Mth.PI * 0.016666667F);
        Vec3 vec3 = Vec3.directionFromRotation(player.getXRot(), player.getYRot());
        List<Projectile> list = TargetGetter.getTargetList(player, Projectile.class, r);
        for (Projectile projectile : list) {
            Vec3 rVec = new Vec3(projectile.getX() - player.getX(), projectile.getY() - player.getEyeY(), projectile.getZ() - player.getZ());
            if (player.distanceToSqr(projectile) < r * r && BSFMthUtil.vec3AngleCos(rVec, vec3) < angleCos) {
                Vec3 dvVec = projectile.getDeltaMovement().scale(-2);
                projectile.push(dvVec.x, dvVec.y, dvVec.z);
            }
        }
        player.getCooldowns().addCooldown(this, 20);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        pPlayer.startUsingItem(pUsedHand);
        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack pStack) {
        return 72000;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack pStack) {
        return UseAnim.SPEAR;
    }
}
