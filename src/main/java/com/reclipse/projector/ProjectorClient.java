package com.reclipse.projector;

import com.reclipse.projector.client.renderer.ProjectorBlockEntityRenderer;
import com.reclipse.projector.client.screens.ProjectorScreen;
import com.reclipse.projector.registries.PBlockEntityTypes;
import com.reclipse.projector.registries.PMenuTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@Mod(value = Projector.MODID, dist = Dist.CLIENT)
public final class ProjectorClient {
    public ProjectorClient(IEventBus modEventBus, ModContainer container) {
        modEventBus.addListener(this::registerMenuScreens);
        modEventBus.addListener(this::registerRenderers);
    }

    private void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(PMenuTypes.PROJECTOR.get(), ProjectorScreen::new);
    }

    private void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(PBlockEntityTypes.PROJECTOR.get(), ProjectorBlockEntityRenderer::new);
    }
}
