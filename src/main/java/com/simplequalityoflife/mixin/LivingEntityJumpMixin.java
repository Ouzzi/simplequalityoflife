package com.simplequalityoflife.mixin;

import com.simplequalityoflife.util.CrawlAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityJumpMixin {

    @Inject(method = "jump", at = @At("HEAD"))
    private void stopCrawlOnJump(CallbackInfo ci) {
        // "this" ist hier LivingEntity, also casten wir
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof PlayerEntity player) {
            // Wir nutzen unser Interface
            if (player instanceof CrawlAccessor crawler) {
                if (crawler.simpleQualityOfLife$isCrawling()) {
                    crawler.simpleQualityOfLife$setCrawling(false);
                }
            }
        }
    }
}