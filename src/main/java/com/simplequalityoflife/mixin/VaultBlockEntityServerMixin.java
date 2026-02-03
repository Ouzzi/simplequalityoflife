package com.simplequalityoflife.mixin;

import com.simplequalityoflife.Simplequalityoflife;
import com.simplequalityoflife.util.IVaultCooldown;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.VaultBlockEntity;
import net.minecraft.block.vault.VaultConfig;
import net.minecraft.block.vault.VaultServerData;
import net.minecraft.block.vault.VaultSharedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Mixin(VaultBlockEntity.Server.class)
public class VaultBlockEntityServerMixin {

    // --- TICK LOGIK ---
    @Inject(method = "tick", at = @At("HEAD"))
    private static void tickCooldowns(ServerWorld world, BlockPos pos, BlockState state, VaultConfig config, VaultServerData serverData, VaultSharedData sharedData, CallbackInfo ci) {

        if (world.getTime() % 40 != 0) return;

        if (serverData instanceof IVaultCooldown cooldownData) {
            Set<UUID> rewardedPlayers = ((VaultServerDataAccessor) serverData).getRewardedPlayersSet();

            if (rewardedPlayers.isEmpty()) return;

            long now = world.getTime();
            Set<UUID> connectedPlayers = ((VaultSharedDataAccessor) sharedData).getConnectedPlayersSet();

            Set<UUID> playersToCheck = new HashSet<>(rewardedPlayers);
            boolean changed = false;

            for (UUID uuid : playersToCheck) {
                if (!cooldownData.hasLootedRecently(uuid, now)) {
                    rewardedPlayers.remove(uuid);
                    connectedPlayers.remove(uuid);
                    cooldownData.removeLootData(uuid);
                    changed = true;
                }
            }

            if (changed) {
                ((VaultServerDataAccessor) serverData).setDirty(true);

                world.markDirty(pos);
                world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
            }
        }
    }

    // --- ALTE LOGIK (Als Sicherheit beim Klicken) ---
    @Inject(method = "tryUnlock", at = @At("HEAD"))
    private static void checkCooldownClick(ServerWorld world, BlockPos pos, BlockState state, VaultConfig config, VaultServerData serverData, VaultSharedData sharedData, PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        // Diese Methode fängt Fälle ab, wo der Tick vielleicht noch nicht lief,
        // der Spieler aber schon klickt.
        if (serverData instanceof IVaultCooldown cooldownData) {
            UUID uuid = player.getUuid();
            Set<UUID> rewardedPlayers = ((VaultServerDataAccessor) serverData).getRewardedPlayersSet();

            if (rewardedPlayers.contains(uuid) && !cooldownData.hasLootedRecently(uuid, world.getTime())) {
                // Sofortiger Reset beim Klick
                rewardedPlayers.remove(uuid);
                Set<UUID> connectedPlayers = ((VaultSharedDataAccessor) sharedData).getConnectedPlayersSet();
                connectedPlayers.remove(uuid);

                ((VaultServerDataAccessor) serverData).setDirty(true);
                ((VaultSharedDataAccessor) sharedData).setDirty(true);
                world.updateListeners(pos, state, state, 3);

                Simplequalityoflife.LOGGER.info("Vault Klick-Reset für " + player.getName().getString());
            }
        }
    }

    @Inject(method = "tryUnlock", at = @At("TAIL"))
    private static void saveCooldown(ServerWorld world, BlockPos pos, BlockState state, VaultConfig config, VaultServerData serverData, VaultSharedData sharedData, PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        Set<UUID> rewardedPlayers = ((VaultServerDataAccessor) serverData).getRewardedPlayersSet();

        // Wenn Loot erfolgreich war -> Zeit speichern
        if (rewardedPlayers.contains(player.getUuid())) {
            if (serverData instanceof IVaultCooldown cooldownData) {
                cooldownData.markLooted(player.getUuid(), world.getTime());
                ((VaultServerDataAccessor) serverData).setDirty(true);

                // Debug
                Simplequalityoflife.LOGGER.info("Vault: Zeit gespeichert für " + player.getName().getString());
            }
        }
    }
}