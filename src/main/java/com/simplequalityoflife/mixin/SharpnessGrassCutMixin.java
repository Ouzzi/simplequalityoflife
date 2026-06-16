package com.simplequalityoflife.mixin;

import com.simplequalityoflife.Simplequalityoflife;
import com.simplequalityoflife.util.VegetationUtil;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class SharpnessGrassCutMixin {

    @Inject(method = "attack", at = @At("HEAD"))
    private void onAttack(Entity target, CallbackInfo ci) {
        if (!Simplequalityoflife.getConfig().qOL.sharpnessCutsGrass) return;

        PlayerEntity player = (PlayerEntity) (Object) this;
        // attack() runs on both sides; breaking blocks on the client desyncs and spawns ghost drops.
        if (player.getEntityWorld().isClient()) return;
        ItemStack mainHand = player.getMainHandStack();
        boolean isWeapon = mainHand.isIn(ItemTags.SWORDS) || mainHand.isIn(ItemTags.AXES);

        if (isWeapon) {
            var registry = player.getEntityWorld().getRegistryManager().getOptional(RegistryKeys.ENCHANTMENT);
            if (registry.isPresent()) {
                var sharpness = registry.get().getOptional(Enchantments.SHARPNESS);

                if (sharpness.isPresent()) {
                    int level = EnchantmentHelper.getLevel(sharpness.get(), mainHand);
                    if (level >= 3) {
                        cutGrassAround(player, target);
                    }
                }
            }
        }
    }

    @Unique
    private void cutGrassAround(PlayerEntity player, Entity target) {
        Box box = target.getBoundingBox();
        BlockPos min = BlockPos.ofFloored(box.minX, box.minY, box.minZ);
        BlockPos max = BlockPos.ofFloored(box.maxX, box.maxY, box.maxZ);

        for (BlockPos pos : BlockPos.iterate(min, max)) {
            BlockState state = player.getEntityWorld().getBlockState(pos);

            if (VegetationUtil.isCuttable(state)) {
                // Block abbauen und Drops fallen lassen (true)
                player.getEntityWorld().breakBlock(pos, true, player);
            }
        }
    }
}