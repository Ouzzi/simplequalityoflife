package com.simplequalityoflife.mixin;

import com.simplequalityoflife.Simplequalityoflife;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class FullDurabilityEfficiencyMixin {

    @Inject(method = "getBlockBreakingSpeed", at = @At("RETURN"), cancellable = true)
    private void modifyMiningSpeed(BlockState block, CallbackInfoReturnable<Float> info) {
        if (!Simplequalityoflife.getConfig().qOL.enableFullDurabilityBonus) return;

        PlayerEntity player = (PlayerEntity) (Object) this;
        ItemStack stack = player.getMainHandStack();

        // Nur wenn das Item Schaden nehmen kann
        if (stack.isEmpty() || !stack.isDamageable()) return;

        float maxDamage = stack.getMaxDamage();
        float currentDamage = stack.getDamage(); // getDamage() gibt den SCHADEN zurück, nicht die Haltbarkeit
        float durabilityPercent = (maxDamage - currentDamage) / maxDamage;

        if (durabilityPercent >= Simplequalityoflife.getConfig().qOL.fullDurabilityThreshold) {
            float originalSpeed = info.getReturnValue();
            // Multipliziere die Geschwindigkeit
            info.setReturnValue((float) (originalSpeed * Simplequalityoflife.getConfig().qOL.fullDurabilityBonusMultiplier));
        }
    }
}