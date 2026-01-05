package com.simplequalityoflife.mixin;

import com.simplequalityoflife.Simplequalityoflife;
import com.simplequalityoflife.util.IVaultCooldown;
import net.minecraft.block.vault.VaultServerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mixin(VaultServerData.class)
public class VaultServerDataMixin implements IVaultCooldown {

    @Unique
    private Map<UUID, Long> lastLootTimes = new HashMap<>();

    @Override
    public boolean hasLootedRecently(UUID playerUuid, long worldTime) {
        if (!lastLootTimes.containsKey(playerUuid)) {
            return false;
        }

        long lastLoot = lastLootTimes.get(playerUuid);
        long diff = worldTime - lastLoot;

        // Config sicher abrufen
        long configDays = 100;
        try {
            if (Simplequalityoflife.getConfig() != null) {
                configDays = Simplequalityoflife.getConfig().qOL.vaultCooldownDays;
            }
        } catch (Exception e) {
            // Fallback
        }

        long cooldownTicks = configDays * 24000L;

        // Sicherheitshalber: Wenn diff negativ (Zeitumstellung), erlauben wir es nicht sofort,
        // sondern behandeln es wie "noch im Cooldown", außer es ist extrem.
        if (diff < 0) return true;

        return diff < cooldownTicks;
    }

    @Override
    public void markLooted(UUID playerUuid, long worldTime) {
        lastLootTimes.put(playerUuid, worldTime);
    }

    // NEU: Implementierung der Lösch-Methode
    @Override
    public void removeLootData(UUID playerUuid) {
        lastLootTimes.remove(playerUuid);
    }

    @Override
    public Map<UUID, Long> getLootTimesMap() {
        return lastLootTimes;
    }

    @Override
    public void setLootTimesMap(Map<UUID, Long> map) {
        this.lastLootTimes = new HashMap<>(map);
    }
}