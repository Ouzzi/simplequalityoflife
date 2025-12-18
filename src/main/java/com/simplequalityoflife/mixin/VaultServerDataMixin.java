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
            Simplequalityoflife.LOGGER.info("Vault Check: Spieler " + playerUuid + " war noch nie hier. Erlaubt.");
            return false;
        }

        long lastLoot = lastLootTimes.get(playerUuid);
        long diff = worldTime - lastLoot;

        // Config laden mit Fallback
        long configDays = 50; // Standard 100 Tage
        try {
            if (Simplequalityoflife.getConfig() != null) {
                configDays = Simplequalityoflife.getConfig().qOL.vaultCooldownDays;
                Simplequalityoflife.LOGGER.info("Geladener Vault Cooldown aus Config: " + configDays + " Tage");
            }
        } catch (Exception e) {
            Simplequalityoflife.LOGGER.error("Fehler beim Laden der Vault Config, nutze Standard 100 Tage", e);
        }

        long cooldownTicks = configDays * 24000L;

        // Debug Ausgabe in die Konsole
        Simplequalityoflife.LOGGER.info("Vault Check für " + playerUuid + ":");
        Simplequalityoflife.LOGGER.info(" - Letzter Loot: " + lastLoot);
        Simplequalityoflife.LOGGER.info(" - Jetzige Zeit: " + worldTime);
        Simplequalityoflife.LOGGER.info(" - Differenz: " + diff + " Ticks");
        Simplequalityoflife.LOGGER.info(" - Cooldown Zeit: " + cooldownTicks + " Ticks (" + configDays + " Tage)");

        // Wenn Zeit zurückgedreht wurde (diff negativ), erlauben wir es sicherheitshalber
        if (diff < 0) return false;

        boolean isOnCooldown = diff < cooldownTicks;
        Simplequalityoflife.LOGGER.info(" - Noch im Cooldown? " + isOnCooldown);

        return isOnCooldown;
    }

    /*

    @Override
    public boolean hasLootedRecently(UUID playerUuid, long worldTime) {
        if (!lastLootTimes.containsKey(playerUuid)) return false;
        long lastLoot = lastLootTimes.get(playerUuid);

        // TEST: Nur 10 Sekunden Cooldown (200 Ticks)
        return (worldTime - lastLoot) < 200L;
    }
     */

    @Override
    public void markLooted(UUID playerUuid, long worldTime) {
        lastLootTimes.put(playerUuid, worldTime);
        Simplequalityoflife.LOGGER.info("Vault: Spieler " + playerUuid + " hat gelootet bei Zeit " + worldTime);
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