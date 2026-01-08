package com.simplequalityoflife.mixin;

import com.simplequalityoflife.Simplequalityoflife;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.level.ServerWorldProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public class ServerWeatherMixin {

    @Unique
    private boolean wasRainingLastTick = false;

    @Inject(method = "tick", at = @At("HEAD"))
    private void captureRainState(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        ServerWorld world = (ServerWorld) (Object) this;
        this.wasRainingLastTick = world.isRaining();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void checkRainProbability(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        ServerWorld world = (ServerWorld) (Object) this;
        boolean isRainingNow = world.isRaining();

        if (!wasRainingLastTick && isRainingNow) {
            // Zugriff wieder über .qOL
            int probability = Simplequalityoflife.getConfig().qOL.rainProbability;

            // Sicherstellen, dass user keine Quatsch-Zahlen eingegeben hat
            if (probability < 0) probability = 0;
            if (probability > 100) probability = 100;

            if (probability < 100) {
                if (world.getRandom().nextInt(100) >= probability) {
                    ServerWorldProperties properties = (ServerWorldProperties) world.getLevelProperties();
                    properties.setRaining(false);
                    int clearTime = 12000 + world.getRandom().nextInt(12000);
                    properties.setClearWeatherTime(clearTime);
                }
            }
        }
    }
}