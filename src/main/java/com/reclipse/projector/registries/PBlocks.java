package com.reclipse.projector.registries;

import com.reclipse.projector.Projector;
import com.reclipse.projector.content.blocks.ProjectorBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class PBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Projector.MODID);

    public static final DeferredBlock<ProjectorBlock> PROJECTOR = BLOCKS.register("projector",
            () -> new ProjectorBlock(BlockBehaviour.Properties.of()
                    .strength(1.8F)
                    .sound(SoundType.METAL)
                    .noOcclusion()));

    static {
        PItems.ITEMS.register("projector", () -> new BlockItem(PROJECTOR.get(), new Item.Properties()));
    }
}
