package com.simplequalityoflife.mixin;

import com.simplequalityoflife.Simplequalityoflife;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.equipment.trim.ArmorTrim;
import net.minecraft.item.equipment.trim.ArmorTrimMaterials;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinBrain.class)
public class PiglinGoldMixin {

    @Inject(method = "isWearingPiglinSafeArmor", at = @At("HEAD"), cancellable = true)
    private static void onIsWearingPiglinSafeArmor(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (!(entity instanceof PlayerEntity player)) return;

        var config = Simplequalityoflife.getConfig().qOL;

        // 1. Gold Trims
        if (config.piglinsIgnoreGoldTrims) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) continue;

                ItemStack stack = player.getEquippedStack(slot);
                if (stack.isEmpty()) continue;

                ArmorTrim trim = stack.get(DataComponentTypes.TRIM);
                if (trim != null && trim.material().matchesKey(ArmorTrimMaterials.GOLD)) {
                    cir.setReturnValue(true);
                    return;
                }
            }
        }

        // 2. Gold Werkzeuge
        if (config.piglinsIgnoreGoldTools) {
            if (isGoldTool(player.getMainHandStack()) || isGoldTool(player.getOffHandStack())) {
                cir.setReturnValue(true);
            }
        }
    }

    @Unique
    private static boolean isGoldTool(ItemStack stack) {
        if (stack.isEmpty()) return false;
        Item item = stack.getItem();
        // Direkter Vergleich mit bekannten Gold-Items statt ToolMaterial Import
        return item == Items.GOLDEN_SWORD ||
                item == Items.GOLDEN_SPEAR ||
                item == Items.GOLDEN_PICKAXE ||
                item == Items.GOLDEN_AXE ||
                item == Items.GOLDEN_SHOVEL ||
                item == Items.GOLDEN_HOE;
    }
}