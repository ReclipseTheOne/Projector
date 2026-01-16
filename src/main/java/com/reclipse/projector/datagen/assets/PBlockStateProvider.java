package com.reclipse.projector.datagen.assets;

import com.reclipse.projector.Projector;
import com.reclipse.projector.registries.PBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class PBlockStateProvider extends BlockStateProvider {
    public PBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Projector.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        horizontalBlock(PBlocks.PROJECTOR.get(), models().getExistingFile(modLoc("block/projector")));
    }
}
