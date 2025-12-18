package com.simplequalityoflife;

import com.simplequalityoflife.client.AutoWalkHandler;
import com.simplequalityoflife.event.HoeHarvestHandler;
import net.fabricmc.api.ClientModInitializer;

public class SimplequalityoflifeClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        AutoWalkHandler.register();
        HoeHarvestHandler.register();

    }
}