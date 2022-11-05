package com.linngdu664.bsf.entity.snowball.special;

import com.linngdu664.bsf.entity.AbstractBSFSnowballEntity;
import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.util.LaunchFunc;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

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
        if(!isCaught){
            Entity entity = pResult.getEntity();
            Entity owner = getOwner();
            Vec3 pos1 = new Vec3(owner.getX(), owner.getY(), owner.getZ());
            Vec3 v1 = owner.getDeltaMovement();
            Vec3 v2 = entity.getDeltaMovement();
            owner.moveTo(entity.getX(),entity.getY(),entity.getZ());
            owner.setDeltaMovement(0,0,0);
            owner.push(v2.x,v2.y,v2.z);
            entity.moveTo(pos1.x,pos1.y,pos1.z);
            entity.setDeltaMovement(0,0,0);
            entity.push(v1.x,v1.y,v1.z);
            if (!level.isClientSide) {
                this.discard();
            }
        }
    }

    @Override
    public Item getCorrespondingItem() {
        return ItemRegister.ENDER_SNOWBALL.get();
    }

    public float getPower() {
        return 1.6f;
    }
}
