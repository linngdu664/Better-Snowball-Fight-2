package com.linngdu664.bsf.entity.snowball.nomal_snowball;

import com.linngdu664.bsf.entity.BSFSnowballEntity;
import com.linngdu664.bsf.item.ItemRegister;
import com.linngdu664.bsf.util.LaunchFunc;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class PowderSnowballEntity extends BSFSnowballEntity {
    public boolean isStart = false;
    public int timer = 0;

    public PowderSnowballEntity(LivingEntity livingEntity, Level level, LaunchFunc launchFunc) {
        super(livingEntity, level);
        this.setLaunchFrom(launchFunc.getLaunchForm());
        launchFunc.launchProperties(this);
        this.setItem(new ItemStack(ItemRegister.POWDER_SNOWBALL.get()));
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult p_37258_) {
        super.onHitBlock(p_37258_);
        isStart = true;
        Vec3 vec3 = this.getDeltaMovement();
        this.push(-vec3.x, -vec3.y, -vec3.z);
        this.setNoGravity(true);
        ((ServerLevel) level).sendParticles(ParticleTypes.SNOWFLAKE, this.getX(), this.getY(), this.getZ(), 100, 0, 0, 0, 0.64);
    }

    @Override
    public void tick() {
        super.tick();
        if(isStart){
            ((ServerLevel) level).sendParticles(ParticleTypes.SNOWFLAKE, this.getX(), this.getY(), this.getZ(), 100, 0, 0, 0, 0.32);
            timer++;
            if(timer>200){
                this.discard();
            }
        }
    }

    @Override
    protected Item getRegisterItem() {
        return ItemRegister.POWDER_SNOWBALL.get();
    }

}
