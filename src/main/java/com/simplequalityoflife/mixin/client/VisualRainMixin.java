package com.simplequalityoflife.mixin.client;

import com.simplequalityoflife.Simplequalityoflife;
import net.minecraft.client.render.WeatherRendering;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WeatherRendering.class)
public class VisualRainMixin {

    /**
     * Wir leiten den Abruf der Regen-Intensität (RainGradient) um.
     * Das betrifft sowohl die visuelle Darstellung (Regensäulen) als auch die Anzahl der Partikel (Splashes).
     */
    @Redirect(
        method = {
            "buildPrecipitationPieces", // Zuständig für die Regen-Textur in der Luft
            "addParticlesAndSound"      // Zuständig für Partikel am Boden und Sound
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;getRainGradient(F)F"
        )
    )
    private float modifyRainDensity(World world, float delta) {
        // Holen des originalen Regen-Wertes (0.0 bis 1.0)
        float originalGradient = world.getRainGradient(delta);

        // Holen unserer Config (0 bis 100)
        int densityPercent = Simplequalityoflife.getConfig().qOL.clientRainParticleDensity;

        // Wenn die Einstellung auf 100 steht, geben wir den originalen Wert zurück
        if (densityPercent >= 100) {
            return originalGradient;
        }

        // Wenn die Einstellung auf 0 steht, sagen wir dem Renderer: "Es regnet nicht"
        if (densityPercent <= 0) {
            return 0.0F;
        }

        // Dazwischen: Wir skalieren die Intensität herunter.
        // Beispiel: Original 1.0 (starker Regen) * 50% Einstellung = 0.5 (sieht aus wie leichter Regen)
        return originalGradient * (densityPercent / 100.0F);
    }
}