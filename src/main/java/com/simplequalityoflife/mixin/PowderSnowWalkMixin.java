package com.simplequalityoflife.mixin;

import com.simplequalityoflife.Simplequalityoflife;
import net.minecraft.block.PowderSnowBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.RegistryKeys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowderSnowBlock.class)
public class PowderSnowWalkMixin {

    @Inject(method = "canWalkOnPowderSnow", at = @At("RETURN"), cancellable = true)
    private static void allowFrostWalker(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        // Feature Check aus der Config
        if (!Simplequalityoflife.getConfig().frostWalkerWalkOnPowderSnow) return;

        // Wenn bereits true zurückgegeben wurde (z.B. wegen Lederstiefeln), nichts tun
        if (cir.getReturnValue()) return;

        if (entity instanceof LivingEntity living) {
            var world = living.getEntityWorld();
            var registryManager = world.getRegistryManager();
            
            // Sicherstellen, dass wir auf die Enchantment Registry zugreifen können
            var registry = registryManager.getOptional(RegistryKeys.ENCHANTMENT);
            
            if (registry.isPresent()) {
                var frostWalker = registry.get().getOptional(Enchantments.FROST_WALKER);

                if (frostWalker.isPresent()) {
                    // Prüfen, ob das Enchantment vorhanden ist (> 0)
                    int level = EnchantmentHelper.getEquipmentLevel(frostWalker.get(), living);
                    if (level > 0) {
                        cir.setReturnValue(true);
                    }
                }
            }
        }
    }
}