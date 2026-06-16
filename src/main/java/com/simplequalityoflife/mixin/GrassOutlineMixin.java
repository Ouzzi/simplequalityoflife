package com.simplequalityoflife.mixin;

import com.simplequalityoflife.Simplequalityoflife;
import com.simplequalityoflife.util.VegetationUtil;
import net.minecraft.block.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// WICHTIG: Wir greifen jetzt in den BlockState ein, nicht mehr in den Block direkt.
// Das fängt alle Blöcke ab, auch die, die eigene Formen definieren.
@Mixin(AbstractBlock.AbstractBlockState.class)
public class GrassOutlineMixin {

    @Inject(method = "getOutlineShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;", at = @At("HEAD"), cancellable = true)
    private void removeOutlineForSharpness(BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (!Simplequalityoflife.getConfig().qOL.sharpnessCutsGrass) return;

        if (!(context instanceof EntityShapeContext entityContext) || !(entityContext.getEntity() instanceof PlayerEntity player)) {
            return;
        }

        if (player.isSneaking()) {
            return;
        }

        ItemStack mainHand = player.getMainHandStack();
        if (mainHand.isEmpty()) return;

        if (!(mainHand.isIn(ItemTags.SWORDS) || mainHand.isIn(ItemTags.AXES))) {
            return;
        }

        var registryManager = player.getEntityWorld().getRegistryManager();
        var enchantmentRegistry = registryManager.getOptional(RegistryKeys.ENCHANTMENT);

        if (enchantmentRegistry.isPresent()) {
            var sharpnessEntry = enchantmentRegistry.get().getOptional(Enchantments.SHARPNESS);

            if (sharpnessEntry.isPresent()) {
                int level = EnchantmentHelper.getLevel(sharpnessEntry.get(), mainHand);

                if (level >= 3) {
                    BlockState state = (BlockState) (Object) this;

                    if (VegetationUtil.isCuttable(state)) {
                        cir.setReturnValue(VoxelShapes.empty());
                    }
                }
            }
        }
    }
}