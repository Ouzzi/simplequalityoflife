package com.simplequalityoflife;

import com.simplequalityoflife.command.ModCommands;
import com.simplequalityoflife.config.SimplequalityoflifeConfig;
import com.simplequalityoflife.event.HoeHarvestHandler;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Simplequalityoflife implements ModInitializer {
	public static final String MOD_ID = "simplequalityoflife";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static SimplequalityoflifeConfig CONFIG;

	@Override
	public void onInitialize() {
        LOGGER.info("Initializing Simplequalityoflife mod...");

        AutoConfig.register(SimplequalityoflifeConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(SimplequalityoflifeConfig.class).getConfig();


        ModCommands.register();
        HoeHarvestHandler.register();
	}

    public static SimplequalityoflifeConfig getConfig() {
        if (CONFIG == null) {
            return new SimplequalityoflifeConfig();
        }
        return CONFIG;
    }
}