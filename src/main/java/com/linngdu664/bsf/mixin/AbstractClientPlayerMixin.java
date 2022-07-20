package com.linngdu664.bsf.mixin;

import com.linngdu664.bsf.item.setter.ItemRegister;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin extends Player {
    public final ClientLevel clientLevel;

    public AbstractClientPlayerMixin(ClientLevel pClientLevel, GameProfile pGameProfile) {
        super(pClientLevel, pClientLevel.getSharedSpawnPos(), pClientLevel.getSharedSpawnAngle(), pGameProfile);
        this.clientLevel = pClientLevel;
    }

    @Inject(method = "getFieldOfViewModifier", at = @At("TAIL"), cancellable = true)
    public void getFieldOfViewModifier(CallbackInfoReturnable<Float> cir) {
        ItemStack itemstack = this.getUseItem();
        float f = 1.0F;
        if (this.isUsingItem()) {
            if (itemstack.is(ItemRegister.SNOWBALL_CANNON.get()) || itemstack.is(ItemRegister.FREEZING_SNOWBALL_CANNON.get()) || itemstack.is(ItemRegister.POWERFUL_SNOWBALL_CANNON.get())) {
                System.out.println("fsdf");
                int i = this.getTicksUsingItem();
                float f1 = (float)i / 20.0F;
                if (f1 > 1.0F) {
                    f1 = 1.0F;
                } else {
                    f1 *= f1;
                }
                f *= 1.0F - f1 * 0.15F;
            }
        }
        cir.setReturnValue(net.minecraftforge.client.ForgeHooksClient.getFieldOfView(this, f));
    }
}
