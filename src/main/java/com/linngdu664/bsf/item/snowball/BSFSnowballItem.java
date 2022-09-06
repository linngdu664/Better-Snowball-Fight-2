package com.linngdu664.bsf.item.snowball;

import com.linngdu664.bsf.entity.BSFSnowballEntity;
import com.linngdu664.bsf.util.LaunchFrom;
import com.linngdu664.bsf.util.LaunchFunc;
import net.minecraft.world.item.Item;

public class BSFSnowballItem extends Item {
    public BSFSnowballItem(Properties pProperties) {
        super(pProperties);
    }
    public LaunchFunc getLaunchFunc(float playerBadEffectRate){
        return new LaunchFunc() {
            @Override
            public LaunchFrom getLaunchForm() {
                return LaunchFrom.HAND;
            }

            @Override
            public void launchProperties(BSFSnowballEntity bsfSnowballEntity) {
                bsfSnowballEntity.damage *= playerBadEffectRate;
                bsfSnowballEntity.blazeDamage *= playerBadEffectRate;
                bsfSnowballEntity.setting();
            }
        };
    }
}
