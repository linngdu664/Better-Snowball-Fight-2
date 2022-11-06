package com.linngdu664.bsf.entity.snowball.special;

import com.linngdu664.bsf.entity.AbstractBSFSnowballEntity;
import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.util.LaunchFunc;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class EnderSnowballEntity extends AbstractBSFSnowballEntity {
    public EnderSnowballEntity(LivingEntity livingEntity, Level level, LaunchFunc launchFunc) {
        super(livingEntity, level);
        this.setLaunchFrom(launchFunc.getLaunchForm());
        launchFunc.launchProperties(this);
        this.setItem(new ItemStack(ItemRegister.ENDER_SNOWBALL.get()));
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (!isCaught && !level.isClientSide) {
            Entity entity = pResult.getEntity();
            Entity owner = getOwner();
            double x = owner.getX(), y = owner.getY(), z = owner.getZ();
            Vec3 v1 = owner.getDeltaMovement();
            Vec3 v2 = entity.getDeltaMovement();
            owner.moveTo(entity.getX(), entity.getY(), entity.getZ());
            owner.setDeltaMovement(v2);
            if (owner instanceof ServerPlayer serverPlayer) {
                serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(owner));
            }
            //owner.push(v2.x,v2.y,v2.z);
            entity.moveTo(x, y, z);
            entity.setDeltaMovement(v1);
            //entity.push(v1.x,v1.y,v1.z);
            ((ServerLevel) level).sendParticles(ParticleTypes.PORTAL, entity.getX(), entity.getEyeY(), entity.getZ(), 32, 1, 1, 1, 0.1);
            ((ServerLevel) level).sendParticles(ParticleTypes.PORTAL, owner.getX(), owner.getEyeY(), owner.getZ(), 32, 1, 1, 1, 0.1);
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult p_37258_) {
        super.onHitBlock(p_37258_);
        this.discard();
    }

    @Override
    public Item getCorrespondingItem() {
        return ItemRegister.ENDER_SNOWBALL.get();
    }

    public float getPower() {
        return 1.6f;
    }

    @Override
    public void tick() {
        super.tick();
        ((ServerLevel) level).sendParticles(ParticleTypes.PORTAL, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
    }
}
