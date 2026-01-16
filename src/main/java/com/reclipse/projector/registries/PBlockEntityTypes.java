package com.reclipse.projector.registries;

import com.reclipse.projector.Projector;
import com.reclipse.projector.content.blockentities.ProjectorBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class PBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Projector.MODID);

    public static final Supplier<BlockEntityType<ProjectorBlockEntity>> PROJECTOR =
            BLOCK_ENTITY_TYPES.register("projector", () -> BlockEntityType.Builder
                    .of(ProjectorBlockEntity::new, PBlocks.PROJECTOR.get())
                    .build(null));
}
