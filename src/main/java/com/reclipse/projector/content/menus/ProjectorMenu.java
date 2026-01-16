package com.reclipse.projector.content.menus;

import com.reclipse.projector.content.blockentities.ProjectorBlockEntity;
import com.reclipse.projector.registries.PMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ProjectorMenu extends AbstractContainerMenu {
    private final ProjectorBlockEntity blockEntity;
    private final ContainerLevelAccess access;

    public ProjectorMenu(int containerId, @NotNull Inventory inv, @NotNull FriendlyByteBuf buf) {
        this(containerId, inv, (ProjectorBlockEntity) inv.player.level().getBlockEntity(buf.readBlockPos()));
    }

    public ProjectorMenu(int containerId, @NotNull Inventory inv, @NotNull ProjectorBlockEntity blockEntity) {
        super(PMenuTypes.PROJECTOR.get(), containerId);
        this.blockEntity = blockEntity;
        this.access = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());
    }

    public ProjectorBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public BlockPos getBlockPos() {
        return blockEntity.getBlockPos();
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, blockEntity.getBlockState().getBlock());
    }
}
