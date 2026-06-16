package com.simplequalityoflife.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BlockTags;

/**
 * Shared definition of "small vegetation" used by the Sharpness-cuts-grass features
 * (both the attack handler in SharpnessGrassCutMixin and the outline removal in GrassOutlineMixin).
 * Keeping it in one place avoids the two lists drifting apart.
 */
public final class VegetationUtil {

    private VegetationUtil() {
    }

    public static boolean isCuttable(BlockState state) {
        return state.isIn(BlockTags.FLOWERS)
                || state.getBlock() == Blocks.SHORT_GRASS
                || state.getBlock() == Blocks.TALL_GRASS
                || state.getBlock() == Blocks.FERN
                || state.getBlock() == Blocks.LARGE_FERN
                || state.getBlock() == Blocks.DEAD_BUSH
                || state.getBlock() == Blocks.PINK_PETALS
                || state.getBlock() == Blocks.NETHER_SPROUTS
                || state.getBlock() == Blocks.CRIMSON_ROOTS
                || state.getBlock() == Blocks.WARPED_ROOTS
                || state.isIn(BlockTags.REPLACEABLE);
    }
}
