package com.reclipse.projector.datagen.data;

import com.reclipse.projector.registries.PBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class PBlockLootTableProvider extends BlockLootSubProvider {
    public PBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(PBlocks.PROJECTOR.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return PBlocks.BLOCKS.getEntries().stream().map(e -> (Block) e.get())::iterator;
    }
}
