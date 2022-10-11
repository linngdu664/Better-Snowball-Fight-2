package com.linngdu664.bsf.util;

import com.linngdu664.bsf.entity.AbstractBSFSnowballEntity;

public interface LaunchFunc {
    LaunchFrom getLaunchForm();

    void launchProperties(AbstractBSFSnowballEntity bsfSnowballEntity);
}
