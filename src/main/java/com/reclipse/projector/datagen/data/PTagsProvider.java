package com.reclipse.projector.datagen.data;

import com.reclipse.projector.Projector;
import com.reclipse.projector.registries.PBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class PTagsProvider {
    public static void createTagProviders(DataGenerator generator, PackOutput packOutput,
                                          CompletableFuture<HolderLookup.Provider> lookupProvider,
                                          ExistingFileHelper existingFileHelper, boolean includeServer) {
        PBlockTagsProvider blockTagsProvider = new PBlockTagsProvider(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(includeServer, blockTagsProvider);
        generator.addProvider(includeServer, new PItemTagsProvider(packOutput, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper));
    }

    public static class PBlockTagsProvider extends BlockTagsProvider {
        public PBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                  @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, Projector.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(PBlocks.PROJECTOR.get());
        }
    }

    public static class PItemTagsProvider extends ItemTagsProvider {
        public PItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                 CompletableFuture<TagLookup<net.minecraft.world.level.block.Block>> blockTags,
                                 @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, blockTags, Projector.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            // Add item tags here if needed
        }
    }
}
