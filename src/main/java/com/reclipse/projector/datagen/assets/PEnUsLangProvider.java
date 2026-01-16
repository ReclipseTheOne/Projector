package com.reclipse.projector.datagen.assets;

import com.reclipse.projector.Projector;
import com.reclipse.projector.registries.PBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class PEnUsLangProvider extends LanguageProvider {
    public PEnUsLangProvider(PackOutput output) {
        super(output, Projector.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addBlock(PBlocks.PROJECTOR, "Text Projector");
        add("creative_tabs.projector.main", "Projector");
    }
}
