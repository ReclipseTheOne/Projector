package com.reclipse.projector.datagen.assets;

import com.reclipse.projector.Projector;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class PItemModelProvider extends ItemModelProvider {
    public PItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Projector.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent("projector", modLoc("block/projector"));
    }
}
