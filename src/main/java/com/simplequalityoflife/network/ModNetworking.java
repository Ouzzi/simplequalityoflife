package com.simplequalityoflife.network;

import com.google.gson.Gson;
import com.simplequalityoflife.Simplequalityoflife;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Gemeinsame (common) Netzwerk-Registrierung: Der Server schickt beim Join seine Config an den Client,
 * damit auf einem entfernten Server der SERVER die Wahrheit besitzt (Client/Server-Konsistenz).
 */
public final class ModNetworking {

    private static final Gson GSON = new Gson();

    private ModNetworking() {
    }

    public static void registerCommon() {
        // Payload-Typ registrieren (S2C). Muss auf beiden Seiten geschehen -> common init.
        PayloadTypeRegistry.playS2C().register(ConfigSyncPayload.ID, ConfigSyncPayload.CODEC);

        // Beim Join die SERVER-Config an den Client senden (nur wenn dessen Client den Channel kennt).
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.player;
            if (player != null && ServerPlayNetworking.canSend(player, ConfigSyncPayload.ID)) {
                ServerPlayNetworking.send(player, new ConfigSyncPayload(GSON.toJson(Simplequalityoflife.getLocalConfig())));
            }
        });
    }
}
