package com.reclipse.projector;

import com.reclipse.projector.networking.ProjectorUpdatePayload;
import com.reclipse.projector.registries.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

@Mod(Projector.MODID)
public final class Projector {
    public static final String MODID = "projector";
    public static final String MODNAME = "Projector";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Projector(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::registerPayloads);

        PItems.ITEMS.register(modEventBus);
        PBlocks.BLOCKS.register(modEventBus);
        PCreativeTabs.TABS.register(modEventBus);
        PBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
        PMenuTypes.MENU_TYPES.register(modEventBus);
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MODID);
        registrar.playToServer(
                ProjectorUpdatePayload.TYPE,
                ProjectorUpdatePayload.STREAM_CODEC,
                ProjectorUpdatePayload::handle
        );
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
