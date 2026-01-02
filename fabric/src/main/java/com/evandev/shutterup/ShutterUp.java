package com.evandev.shutterup;

import net.fabricmc.api.ModInitializer;

public class ShutterUp implements ModInitializer {
    
    @Override
    public void onInitialize() {

        Constants.LOG.info("Hello Fabric world!");
        CommonClass.init();
    }
}
