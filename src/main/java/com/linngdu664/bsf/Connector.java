package com.linngdu664.bsf;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class Connector implements IMixinConnector {
    @Override
    public void connect() {
        Mixins.addConfiguration("mixins.bsf.json");
    }
}
