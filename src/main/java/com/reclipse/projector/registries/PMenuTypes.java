package com.reclipse.projector.registries;

import com.reclipse.projector.Projector;
import com.reclipse.projector.content.menus.ProjectorMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class PMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(BuiltInRegistries.MENU, Projector.MODID);

    public static final Supplier<MenuType<ProjectorMenu>> PROJECTOR =
            MENU_TYPES.register("projector", () -> IMenuTypeExtension.create(ProjectorMenu::new));
}
