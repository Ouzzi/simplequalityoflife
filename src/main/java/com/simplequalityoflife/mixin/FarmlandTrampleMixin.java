package com.simplequalityoflife.mixin;

import com.simplequalityoflife.Simplequalityoflife;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmlandBlock.class)
public class FarmlandTrampleMixin {

    @Inject(method = "onLandedUpon", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/FarmlandBlock;setToDirt(Lnet/minecraft/entity/Entity;Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V"), cancellable = true)
    private void preventTrampleIfFeatherFalling(World world, BlockState state, BlockPos pos, Entity entity, double fallDistance, CallbackInfo ci) {
        // 1. Config Check (Ganz am Anfang)
        if (!Simplequalityoflife.getConfig().qOL.preventFarmlandTrampleWithFeatherFalling) return;

        if (entity instanceof LivingEntity living) {
            ItemStack feetStack = living.getEquippedStack(EquipmentSlot.FEET);
            if (feetStack.isEmpty()) {
                return;
            }

            var registry = world.getRegistryManager().getOptional(RegistryKeys.ENCHANTMENT);

            if (registry.isPresent()) {
                var featherFalling = registry.get().getOptional(Enchantments.FEATHER_FALLING);

                if (featherFalling.isPresent()) {
                    int level = EnchantmentHelper.getLevel(featherFalling.get(), feetStack);

                    if (level > 0) {
                        ci.cancel();
                    }
                }
            }
        }
    }
}