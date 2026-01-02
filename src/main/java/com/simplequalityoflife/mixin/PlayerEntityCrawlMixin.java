package com.simplequalityoflife.mixin;

import com.simplequalityoflife.util.CrawlAccessor;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityCrawlMixin extends LivingEntity implements CrawlAccessor {

    @Unique
    private static final TrackedData<Boolean> FORCED_CRAWL = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    protected PlayerEntityCrawlMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void initCrawlTracker(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(FORCED_CRAWL, false);
    }

    @Inject(method = "updatePose", at = @At("HEAD"), cancellable = true)
    private void forceCrawlPose(CallbackInfo ci) {
        if (this.getDataTracker().get(FORCED_CRAWL)) {
            this.setPose(EntityPose.SWIMMING);
            ci.cancel();
        }
    }

    // -------------------------------------

    @Override
    public void simpleQualityOfLife$setCrawling(boolean crawling) {
        this.getDataTracker().set(FORCED_CRAWL, crawling);
        if (crawling) {
            this.setPose(EntityPose.SWIMMING);
        }
        this.calculateDimensions();
    }

    @Override
    public boolean simpleQualityOfLife$isCrawling() {
        return this.getDataTracker().get(FORCED_CRAWL);
    }
}