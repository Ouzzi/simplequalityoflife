package com.simplequalityoflife.network;

import com.simplequalityoflife.Simplequalityoflife;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/**
 * Server -> Client Paket, das die (als JSON serialisierte) Server-Config überträgt.
 */
public record ConfigSyncPayload(String json) implements CustomPayload {

    public static final CustomPayload.Id<ConfigSyncPayload> ID =
            new CustomPayload.Id<>(Identifier.of(Simplequalityoflife.MOD_ID, "config_sync"));

    public static final PacketCodec<RegistryByteBuf, ConfigSyncPayload> CODEC =
            PacketCodec.tuple(PacketCodecs.STRING, ConfigSyncPayload::json, ConfigSyncPayload::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
