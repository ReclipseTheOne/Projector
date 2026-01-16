package com.reclipse.projector.content.blockentities;

import com.reclipse.projector.content.blocks.ProjectorBlock;
import com.reclipse.projector.content.menus.ProjectorMenu;
import com.reclipse.projector.registries.PBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.FastColor;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ProjectorBlockEntity extends BlockEntity implements MenuProvider {
    private String text = "";
    private int color = 0xFFFFFF; // White by default (RGB only, no alpha)
    private int fontSize = 10;
    private int padding = 0;
    private int offset = 0;
    private int rotation = 0; // 0-360 degrees
    private boolean dropShadow = false;
    private boolean followPlayer = false; // Horizontal rotation follows player

    public ProjectorBlockEntity(BlockPos pos, BlockState blockState) {
        super(PBlockEntityTypes.PROJECTOR.get(), pos, blockState);
    }

    // Getters
    public String getText() {
        return text;
    }

    public int getColor() {
        return color;
    }

    public int getRed() {
        return FastColor.ARGB32.red(color);
    }

    public int getGreen() {
        return FastColor.ARGB32.green(color);
    }

    public int getBlue() {
        return FastColor.ARGB32.blue(color);
    }

    public int getFontSize() {
        return fontSize;
    }

    public int getPadding() {
        return padding;
    }

    public int getOffset() {
        return offset;
    }

    public int getRotation() {
        return rotation;
    }

    public boolean hasDropShadow() {
        return dropShadow;
    }

    public boolean isFollowPlayer() {
        return followPlayer;
    }

    public Direction getFacing() {
        return getBlockState().getValue(ProjectorBlock.FACING);
    }

    // Setters
    public void setText(String text) {
        this.text = text;
        setChanged();
        syncToClient();
    }

    public void setColor(int color) {
        this.color = color & 0xFFFFFF; // Strip alpha, store RGB only
        setChanged();
        syncToClient();
    }

    public void setRed(int red) {
        this.color = FastColor.ARGB32.color(Math.clamp(red, 0, 255), getGreen(), getBlue());
        setChanged();
        syncToClient();
    }

    public void setGreen(int green) {
        this.color = FastColor.ARGB32.color(getRed(), Math.clamp(green, 0, 255), getBlue());
        setChanged();
        syncToClient();
    }

    public void setBlue(int blue) {
        this.color = FastColor.ARGB32.color(getRed(), getGreen(), Math.clamp(blue, 0, 255));
        setChanged();
        syncToClient();
    }

    public void setFontSize(int fontSize) {
        this.fontSize = Math.clamp(fontSize, 1, 100);
        setChanged();
        syncToClient();
    }

    public void setPadding(int padding) {
        this.padding = Math.clamp(padding, 0, 20);
        setChanged();
        syncToClient();
    }

    public void setOffset(int offset) {
        this.offset = Math.clamp(offset, 0, 10);
        setChanged();
        syncToClient();
    }

    public void setRotation(int rotation) {
        this.rotation = Math.clamp(rotation, 0, 360);
        setChanged();
        syncToClient();
    }

    public void setDropShadow(boolean dropShadow) {
        this.dropShadow = dropShadow;
        setChanged();
        syncToClient();
    }

    public void setFollowPlayer(boolean followPlayer) {
        this.followPlayer = followPlayer;
        setChanged();
        syncToClient();
    }

    // HSL conversion helpers
    public static float[] rgbToHsl(int r, int g, int b) {
        float rf = r / 255f;
        float gf = g / 255f;
        float bf = b / 255f;

        float max = Math.max(rf, Math.max(gf, bf));
        float min = Math.min(rf, Math.min(gf, bf));
        float h, s, l = (max + min) / 2f;

        if (max == min) {
            h = s = 0f;
        } else {
            float d = max - min;
            s = l > 0.5f ? d / (2f - max - min) : d / (max + min);

            if (max == rf) {
                h = (gf - bf) / d + (gf < bf ? 6f : 0f);
            } else if (max == gf) {
                h = (bf - rf) / d + 2f;
            } else {
                h = (rf - gf) / d + 4f;
            }
            h /= 6f;
        }

        return new float[]{h * 360f, s * 100f, l * 100f};
    }

    public static int hslToRgb(float h, float s, float l) {
        h = h / 360f;
        s = s / 100f;
        l = l / 100f;

        float r, g, b;

        if (s == 0f) {
            r = g = b = l;
        } else {
            float q = l < 0.5f ? l * (1f + s) : l + s - l * s;
            float p = 2f * l - q;
            r = hueToRgb(p, q, h + 1f / 3f);
            g = hueToRgb(p, q, h);
            b = hueToRgb(p, q, h - 1f / 3f);
        }

        return FastColor.ARGB32.color(Math.round(r * 255f), Math.round(g * 255f), Math.round(b * 255f));
    }

    private static float hueToRgb(float p, float q, float t) {
        if (t < 0f) t += 1f;
        if (t > 1f) t -= 1f;
        if (t < 1f / 6f) return p + (q - p) * 6f * t;
        if (t < 1f / 2f) return q;
        if (t < 2f / 3f) return p + (q - p) * (2f / 3f - t) * 6f;
        return p;
    }

    private void syncToClient() {
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putString("text", text);
        tag.putInt("color", color);
        tag.putInt("fontSize", fontSize);
        tag.putInt("padding", padding);
        tag.putInt("offset", offset);
        tag.putInt("rotation", rotation);
        tag.putBoolean("dropShadow", dropShadow);
        tag.putBoolean("followPlayer", followPlayer);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        text = tag.getString("text");
        color = tag.getInt("color");
        fontSize = tag.getInt("fontSize");
        padding = tag.getInt("padding");
        offset = tag.getInt("offset");
        rotation = tag.getInt("rotation");
        dropShadow = tag.getBoolean("dropShadow");
        followPlayer = tag.getBoolean("followPlayer");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.projector.projector");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ProjectorMenu(containerId, playerInventory, this);
    }
}
