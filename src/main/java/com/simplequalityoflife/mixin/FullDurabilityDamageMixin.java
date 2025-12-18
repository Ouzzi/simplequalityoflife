package com.simplequalityoflife.mixin;

import com.simplequalityoflife.Simplequalityoflife;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry; // WICHTIG: Dieser Import hat gefehlt/war falsch
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class FullDurabilityDamageMixin {

    // Signatur angepasst: EntityAttribute -> RegistryEntry<EntityAttribute>
    @Inject(method = "getAttributeValue", at = @At("RETURN"), cancellable = true)
    private void modifyAttackDamage(RegistryEntry<EntityAttribute> attribute, CallbackInfoReturnable<Double> info) {
        // Name angepasst: GENERIC_ATTACK_DAMAGE -> ATTACK_DAMAGE
        if (attribute != EntityAttributes.ATTACK_DAMAGE) return;

        // "this" Check wie gehabt
        if (!((Object) this instanceof PlayerEntity player)) return;

        if (!Simplequalityoflife.getConfig().qOL.enableFullDurabilityBonus) return;

        ItemStack stack = player.getMainHandStack();

        if (stack.isEmpty() || !stack.isDamageable()) return;

        float maxDamage = stack.getMaxDamage();
        float currentDamage = stack.getDamage();
        float durabilityPercent = (maxDamage - currentDamage) / maxDamage;

        if (durabilityPercent >= Simplequalityoflife.getConfig().qOL.fullDurabilityThreshold) {
            double originalDamage = info.getReturnValue();
            info.setReturnValue(originalDamage * Simplequalityoflife.getConfig().qOL.fullDurabilityBonusMultiplier);
        }
    }
}