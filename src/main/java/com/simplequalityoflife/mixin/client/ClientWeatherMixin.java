package com.simplequalityoflife.mixin.client;

import com.simplequalityoflife.Simplequalityoflife;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.WeatherRendering;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticlesMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public class ClientWeatherMixin {

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/WeatherRendering;addParticlesAndSound(Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/client/render/Camera;ILnet/minecraft/particle/ParticlesMode;I)V"
            )
    )
    private void redirectAddParticlesAndSound(WeatherRendering instance, ClientWorld world, Camera camera, int ticks, ParticlesMode particlesMode, int weatherRadius) {
        // Zugriff wieder über .qOL
        int density = Simplequalityoflife.getConfig().qOL.clientRainParticleDensity;

        if (density >= 100) {
            instance.addParticlesAndSound(world, camera, ticks, particlesMode, weatherRadius);
            return;
        }

        if (density <= 0) {
            return;
        }

        if (world.getRandom().nextInt(100) < density) {
            instance.addParticlesAndSound(world, camera, ticks, particlesMode, weatherRadius);
        }
    }
}