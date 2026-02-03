package com.simplequalityoflife.mixin;

import com.simplequalityoflife.Simplequalityoflife;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public class ServerWeatherMixin {

    @Inject(method = "tickWeather", at = @At("HEAD"), cancellable = true)
    private void disableWeather(CallbackInfo ci) {
        // Performance-Check: Erst Konfig prüfen
        if (Simplequalityoflife.getConfig().qOL.disableWeather) {
            ServerWorld world = (ServerWorld) (Object) this;

            // Optimierung: Nur eingreifen, wenn das Wetter tatsächlich schlecht ist!
            // Das verhindert unnötige Server-Operationen und Netzwerk-Spam.
            if (world.isRaining() || world.isThundering()) {
                world.setWeather(0, 0, false, false);
            }

            // WICHTIG: Die originale Methode abbrechen.
            // Das spart CPU, da Minecraft keine Zufallszahlen für Blitz/Regen berechnen muss.
            ci.cancel();
        }
    }
}