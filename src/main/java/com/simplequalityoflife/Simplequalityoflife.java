package com.simplequalityoflife;

import com.simplequalityoflife.command.ModCommands;
import com.simplequalityoflife.config.SimplequalityoflifeConfig;
import com.simplequalityoflife.event.FurnaceLavaFillHandler;
import com.simplequalityoflife.event.HoeHarvestHandler;
import com.simplequalityoflife.network.ModNetworking;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class Simplequalityoflife implements ModInitializer {
	public static final String MOD_ID = "simplequalityoflife";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // Optionaler Override: Auf einem entfernten Server liefert der Client hierüber die vom Server
    // synchronisierte Config. Bleibt auf dem Dedicated Server / in Singleplayer null.
    private static volatile Supplier<SimplequalityoflifeConfig> configOverride = null;

	@Override
	public void onInitialize() {
        LOGGER.info("Initializing Simplequalityoflife mod...");

        AutoConfig.register(SimplequalityoflifeConfig.class, GsonConfigSerializer::new);

        ModNetworking.registerCommon();
        ModCommands.register();
        HoeHarvestHandler.register();
        FurnaceLavaFillHandler.register();
	}

    /**
     * Die wirksame Config. Auf einem Client, der mit einem entfernten Server verbunden ist, sind das die
     * vom Server synchronisierten Werte (der Server besitzt die Wahrheit). Auf dem Server / in Singleplayer
     * ist es die lokale Config.
     */
    public static SimplequalityoflifeConfig getConfig() {
        Supplier<SimplequalityoflifeConfig> override = configOverride;
        if (override != null) {
            SimplequalityoflifeConfig synced = override.get();
            if (synced != null) return synced;
        }
        return getLocalConfig();
    }

    /** Immer die lokale Config aus dem Holder (ohne Server-Sync). */
    public static SimplequalityoflifeConfig getLocalConfig() {
        try {
            return AutoConfig.getConfigHolder(SimplequalityoflifeConfig.class).getConfig();
        } catch (Exception e) {
            // Not registered yet (very early call) -> safe defaults.
            return new SimplequalityoflifeConfig();
        }
    }

    /** Wird vom Client gesetzt, um die server-synchronisierte Config bereitzustellen. */
    public static void setConfigOverride(Supplier<SimplequalityoflifeConfig> override) {
        configOverride = override;
    }
}
