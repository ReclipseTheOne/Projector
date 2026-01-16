package com.reclipse.projector.registries;

import com.reclipse.projector.Projector;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class PCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, Projector.MODID);

    public static final Supplier<CreativeModeTab> MAIN = TABS.register("main", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(PBlocks.PROJECTOR.get()))
            .title(Component.translatable("creative_tabs.projector.main"))
            .displayItems((params, out) -> {
                out.accept(PBlocks.PROJECTOR.get());
            })
            .build());
}
