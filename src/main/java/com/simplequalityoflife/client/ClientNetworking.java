package com.simplequalityoflife.client;

import com.google.gson.Gson;
import com.simplequalityoflife.Simplequalityoflife;
import com.simplequalityoflife.config.SimplequalityoflifeConfig;
import com.simplequalityoflife.network.ConfigSyncPayload;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;

/**
 * Client-Seite des Config-Syncs: empfängt die Server-Config und stellt sie als "wirksame" Config bereit,
 * solange der Client mit einem entfernten Server verbunden ist.
 */
@Environment(EnvType.CLIENT)
public final class ClientNetworking {

    private static final Gson GSON = new Gson();

    // Die vom Server synchronisierte Config (nur gesetzt, wenn mit einem entfernten Server verbunden).
    private static volatile SimplequalityoflifeConfig syncedConfig = null;

    private ClientNetworking() {
    }

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(ConfigSyncPayload.ID, (payload, context) -> {
            SimplequalityoflifeConfig parsed;
            try {
                parsed = GSON.fromJson(payload.json(), SimplequalityoflifeConfig.class);
            } catch (Exception e) {
                parsed = null;
            }
            if (parsed != null) {
                // Rein client-seitige Vorlieben NICHT vom Server überschreiben (persönliche Einstellungen):
                SimplequalityoflifeConfig local = Simplequalityoflife.getLocalConfig();
                parsed.qOL.clientRainParticleDensity = local.qOL.clientRainParticleDensity;
                parsed.qOL.enableAutowalk = local.qOL.enableAutowalk;
            }
            final SimplequalityoflifeConfig result = parsed;
            // Auf den Render-Thread, da der Receiver auf dem Netzwerk-Thread läuft.
            context.client().execute(() -> syncedConfig = result);
        });

        // Server verlassen -> wieder die lokale Config nutzen.
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> syncedConfig = null);
    }

    /**
     * Die wirksame server-synchronisierte Config – oder null, wenn wir selbst der Server sind
     * (Singleplayer / LAN-Host nutzen ihre lokale, live editierbare Config) oder noch nichts ankam.
     */
    public static SimplequalityoflifeConfig getSyncedConfig() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null || mc.getServer() != null) {
            return null; // integrierter Server vorhanden -> wir besitzen die Wahrheit selbst
        }
        return syncedConfig;
    }
}
