package com.reclipse.projector.datagen;

import com.reclipse.projector.Projector;
import com.reclipse.projector.datagen.assets.PBlockStateProvider;
import com.reclipse.projector.datagen.assets.PEnUsLangProvider;
import com.reclipse.projector.datagen.assets.PItemModelProvider;
import com.reclipse.projector.datagen.data.PBlockLootTableProvider;
import com.reclipse.projector.datagen.data.PRecipeProvider;
import com.reclipse.projector.datagen.data.PTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Projector.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class PDataGatherer {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeClient(), new PBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new PItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new PEnUsLangProvider(packOutput));

        PTagsProvider.createTagProviders(generator, packOutput, lookupProvider, existingFileHelper, event.includeServer());
        generator.addProvider(event.includeServer(), new PRecipeProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(), List.of(
                new LootTableProvider.SubProviderEntry(PBlockLootTableProvider::new, LootContextParamSets.BLOCK)
        ), lookupProvider));
    }
}
