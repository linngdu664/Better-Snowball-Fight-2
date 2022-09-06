package com.linngdu664.bsf.item.weapon;

import com.linngdu664.bsf.entity.BSFSnowballEntity;
import com.linngdu664.bsf.entity.snowball.nomal_snowball.IceSnowballEntity;
import com.linngdu664.bsf.util.LaunchFrom;
import com.linngdu664.bsf.util.LaunchFunc;

public class FreezingSnowballCannonItem extends SnowballCannonItem{
    @Override
    public LaunchFunc getLaunchFunc(double damageDropRate) {
        return new LaunchFunc() {
            @Override
            public LaunchFrom getLaunchForm() {
                return LaunchFrom.CANNON;
            }

            @Override
            public void launchProperties(BSFSnowballEntity bsfSnowballEntity) {
                if(bsfSnowballEntity instanceof IceSnowballEntity){
                    bsfSnowballEntity.blazeDamage=10;
                    bsfSnowballEntity.frozenTime=200;
                }else{
                    bsfSnowballEntity.blazeDamage+=1;
                }
                bsfSnowballEntity.blazeDamage*=damageDropRate;
                bsfSnowballEntity.punch = damageDropRate * 1.51F;
                bsfSnowballEntity.setting();
            }
        };
    }
}
