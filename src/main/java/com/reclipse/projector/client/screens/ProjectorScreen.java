package com.reclipse.projector.client.screens;

import com.reclipse.projector.ProjectorConfig;
import com.reclipse.projector.client.widgets.FloatSlider;
import com.reclipse.projector.content.blockentities.ProjectorBlockEntity;
import com.reclipse.projector.content.menus.ProjectorMenu;
import com.reclipse.projector.networking.ProjectorUpdatePayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class ProjectorScreen extends AbstractContainerScreen<ProjectorMenu> {
    private EditBox textInput;
    private EditBox redInput;
    private EditBox greenInput;
    private EditBox blueInput;
    private EditBox fontSizeInput;

    private FloatSlider offsetXSlider;
    private FloatSlider offsetYSlider;
    private FloatSlider offsetZSlider;
    private FloatSlider rotationSlider;

    private EditBox offsetXInput;
    private EditBox offsetYInput;
    private EditBox offsetZInput;
    private EditBox rotationInput;

    private boolean updatingFromSlider = false;

    private boolean hslMode = false;
    private Button modeToggleButton;
    private Button dropShadowButton;
    private Checkbox followPlayerCheckbox;

    // Cached values for HSL mode
    private float cachedHue = 0;
    private float cachedSat = 0;
    private float cachedLight = 100;

    private final int SCREEN_WIDTH = 250;
    private final int SCREEN_HEIGHT = 230;

    private final int LEFT_PADDING = 10;
    private final int RIGHT_PADDING = 10;
	
	private final int COLOR_INPUT_WIDTH = 40;
	private final int COLOR_SQUARE_SIZE = 8;

	private final int COLOR_AND_EDITBOX_TOTAL_SIZE = 4 + COLOR_SQUARE_SIZE + 4 + COLOR_INPUT_WIDTH + 14;

    public ProjectorScreen(ProjectorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = SCREEN_WIDTH;
        this.imageHeight = SCREEN_HEIGHT;
    }

    @Override
    protected void init() {
        super.init();

        int x = leftPos + LEFT_PADDING;
        int y = topPos + 20;

        int rowHeight = 22;

        ProjectorBlockEntity be = menu.getBlockEntity();

        // Text input
        textInput = new EditBox(font, x + 30, y, 200, 18, Component.literal("Text"));
        textInput.setMaxLength(512);
        textInput.setValue(be.getText());
        textInput.setResponder(this::onTextChanged);
        addRenderableWidget(textInput);

        y += rowHeight + 5;

        // RGB/HSL inputs
        redInput = new EditBox(font, x + 2 + 4 + COLOR_SQUARE_SIZE, y, COLOR_INPUT_WIDTH, 18, Component.literal(""));
        redInput.setMaxLength(3);
        redInput.setResponder(this::onColorChanged);
        addRenderableWidget(redInput);

        greenInput = new EditBox(font, x + 2 + 4 + COLOR_SQUARE_SIZE + COLOR_AND_EDITBOX_TOTAL_SIZE, y, COLOR_INPUT_WIDTH, 18, Component.literal(""));
        greenInput.setMaxLength(3);
        greenInput.setResponder(this::onColorChanged);
        addRenderableWidget(greenInput);

        blueInput = new EditBox(font, x + 2 + 4 + COLOR_SQUARE_SIZE + COLOR_AND_EDITBOX_TOTAL_SIZE * 2, y, COLOR_INPUT_WIDTH, 18, Component.literal(""));
        blueInput.setMaxLength(3);
        blueInput.setResponder(this::onColorChanged);
        addRenderableWidget(blueInput);

        y += rowHeight;

        // Mode toggle button
        modeToggleButton = Button.builder(Component.literal("RGB"), btn -> toggleColorMode())
                .bounds(x, y, 50, 18)
                .build();
        addRenderableWidget(modeToggleButton);

        // Font size input next to mode button
        fontSizeInput = new EditBox(font, x + 190, y, 40, 18, Component.literal("Size"));
        fontSizeInput.setMaxLength(3);
        fontSizeInput.setValue(String.valueOf(be.getFontSize()));
        fontSizeInput.setResponder(this::onSettingChanged);
        addRenderableWidget(fontSizeInput);

        y += rowHeight + 8;

        int sliderWidth = 150;
        int sliderInputWidth = 45;
        int sliderInputGap = 5;

        offsetXSlider = new FloatSlider(x, y, sliderWidth, 20, "X Offset",
                ProjectorConfig.sliderOffsetMinX, ProjectorConfig.sliderOffsetMaxX,
                be.getOffsetX(), this::onOffsetXSliderChanged);
        addRenderableWidget(offsetXSlider);
        offsetXInput = new EditBox(font, x + sliderWidth + sliderInputGap, y + 1, sliderInputWidth, 18, Component.literal(""));
        offsetXInput.setMaxLength(8);
        offsetXInput.setValue(String.valueOf(be.getOffsetX()));
        offsetXInput.setResponder(this::onOffsetXInputChanged);
        addRenderableWidget(offsetXInput);
        y += rowHeight + 2;

        offsetYSlider = new FloatSlider(x, y, sliderWidth, 20, "Y Offset",
                ProjectorConfig.sliderOffsetMinY, ProjectorConfig.sliderOffsetMaxY,
                be.getOffsetY(), this::onOffsetYSliderChanged);
        addRenderableWidget(offsetYSlider);
        offsetYInput = new EditBox(font, x + sliderWidth + sliderInputGap, y + 1, sliderInputWidth, 18, Component.literal(""));
        offsetYInput.setMaxLength(8);
        offsetYInput.setValue(String.valueOf(be.getOffsetY()));
        offsetYInput.setResponder(this::onOffsetYInputChanged);
        addRenderableWidget(offsetYInput);
        y += rowHeight + 2;

        offsetZSlider = new FloatSlider(x, y, sliderWidth, 20, "Z Offset",
                ProjectorConfig.sliderOffsetMinZ, ProjectorConfig.sliderOffsetMaxZ,
                be.getOffsetZ(), this::onOffsetZSliderChanged);
        addRenderableWidget(offsetZSlider);
        offsetZInput = new EditBox(font, x + sliderWidth + sliderInputGap, y + 1, sliderInputWidth, 18, Component.literal(""));
        offsetZInput.setMaxLength(8);
        offsetZInput.setValue(String.valueOf(be.getOffsetZ()));
        offsetZInput.setResponder(this::onOffsetZInputChanged);
        addRenderableWidget(offsetZInput);
        y += rowHeight + 2;

        rotationSlider = new FloatSlider(x, y, sliderWidth, 20, "Y-Rotation",
                ProjectorConfig.sliderRotationMin, ProjectorConfig.sliderRotationMax,
                be.getRotation(), this::onRotationSliderChanged);
        addRenderableWidget(rotationSlider);
        rotationInput = new EditBox(font, x + sliderWidth + sliderInputGap, y + 1, sliderInputWidth, 18, Component.literal(""));
        rotationInput.setMaxLength(8);
        rotationInput.setValue(String.valueOf(be.getRotation()));
        rotationInput.setResponder(this::onRotationInputChanged);
        addRenderableWidget(rotationInput);
        y += rowHeight + 5;

        // Drop shadow toggle
        dropShadowButton = Button.builder(
                        Component.literal("Shadow: " + (be.hasDropShadow() ? "ON" : "OFF")),
                        btn -> toggleDropShadow())
                .bounds(x, y, 80, 20)
                .build();
        addRenderableWidget(dropShadowButton);

        // Follow player checkbox
        followPlayerCheckbox = Checkbox.builder(Component.literal("Follow Player"), font)
                .pos(x + 90, y)
                .selected(be.isFollowPlayer())
                .onValueChange((checkbox, value) -> sendUpdate())
                .build();
        addRenderableWidget(followPlayerCheckbox);

        // Initialize color inputs
        updateColorInputs();
    }

    private void updateColorInputs() {
        ProjectorBlockEntity be = menu.getBlockEntity();
        int color = be.getColor();

        if (hslMode) {
            float[] hsl = ProjectorBlockEntity.rgbToHsl(
                    FastColor.ARGB32.red(color),
                    FastColor.ARGB32.green(color),
                    FastColor.ARGB32.blue(color)
            );
            cachedHue = hsl[0];
            cachedSat = hsl[1];
            cachedLight = hsl[2];

            redInput.setValue(String.valueOf(Math.round(hsl[0])));
            greenInput.setValue(String.valueOf(Math.round(hsl[1])));
            blueInput.setValue(String.valueOf(Math.round(hsl[2])));
        } else {
            redInput.setValue(String.valueOf(FastColor.ARGB32.red(color)));
            greenInput.setValue(String.valueOf(FastColor.ARGB32.green(color)));
            blueInput.setValue(String.valueOf(FastColor.ARGB32.blue(color)));
        }
    }

    private void toggleColorMode() {
        hslMode = !hslMode;
        modeToggleButton.setMessage(Component.literal(hslMode ? "HSL" : "RGB"));
        updateColorInputs();
    }

    private void toggleDropShadow() {
        ProjectorBlockEntity be = menu.getBlockEntity();
        boolean newValue = !be.hasDropShadow();
        dropShadowButton.setMessage(Component.literal("Shadow: " + (newValue ? "ON" : "OFF")));
        sendUpdate();
    }

    private void onTextChanged(String text) {
        sendUpdate();
    }

    private void onColorChanged(String value) {
        sendUpdate();
    }

    private void onSettingChanged(String value) {
        sendUpdate();
    }

    private void onOffsetXSliderChanged(float value) {
        updatingFromSlider = true;
        offsetXInput.setValue(String.valueOf(value));
        updatingFromSlider = false;
        sendUpdate();
    }

    private void onOffsetXInputChanged(String value) {
        if (updatingFromSlider) return;
        float floatVal = parseFloat(value, -Float.MAX_VALUE, Float.MAX_VALUE, offsetXSlider.getValue());
        if (floatVal >= ProjectorConfig.sliderOffsetMinX && floatVal <= ProjectorConfig.sliderOffsetMaxX) {
            offsetXSlider.setValue(floatVal);
        }
        sendUpdate();
    }

    private void onOffsetYSliderChanged(float value) {
        updatingFromSlider = true;
        offsetYInput.setValue(String.valueOf(value));
        updatingFromSlider = false;
        sendUpdate();
    }

    private void onOffsetYInputChanged(String value) {
        if (updatingFromSlider) return;
        float floatVal = parseFloat(value, -Float.MAX_VALUE, Float.MAX_VALUE, offsetYSlider.getValue());
        if (floatVal >= ProjectorConfig.sliderOffsetMinY && floatVal <= ProjectorConfig.sliderOffsetMaxY) {
            offsetYSlider.setValue(floatVal);
        }
        sendUpdate();
    }

    private void onOffsetZSliderChanged(float value) {
        updatingFromSlider = true;
        offsetZInput.setValue(String.valueOf(value));
        updatingFromSlider = false;
        sendUpdate();
    }

    private void onOffsetZInputChanged(String value) {
        if (updatingFromSlider) return;
        float floatVal = parseFloat(value, -Float.MAX_VALUE, Float.MAX_VALUE, offsetZSlider.getValue());
        if (floatVal >= ProjectorConfig.sliderOffsetMinZ && floatVal <= ProjectorConfig.sliderOffsetMaxZ) {
            offsetZSlider.setValue(floatVal);
        }
        sendUpdate();
    }

    private void onRotationSliderChanged(float value) {
        updatingFromSlider = true;
        rotationInput.setValue(String.valueOf(value));
        updatingFromSlider = false;
        sendUpdate();
    }

    private void onRotationInputChanged(String value) {
        if (updatingFromSlider) return;
        float floatVal = parseFloat(value, -Float.MAX_VALUE, Float.MAX_VALUE, rotationSlider.getValue());
        if (floatVal >= ProjectorConfig.sliderRotationMin && floatVal <= ProjectorConfig.sliderRotationMax) {
            rotationSlider.setValue(floatVal);
        }
        sendUpdate();
    }

    private void sendUpdate() {
        int color;

        if (hslMode) {
            float h = parseFloat(redInput.getValue(), 0, 360, cachedHue);
            float s = parseFloat(greenInput.getValue(), 0, 100, cachedSat);
            float l = parseFloat(blueInput.getValue(), 0, 100, cachedLight);
            cachedHue = h;
            cachedSat = s;
            cachedLight = l;
            color = ProjectorBlockEntity.hslToRgb(h, s, l);
        } else {
            int r = parseInt(redInput.getValue(), 0, 255, menu.getBlockEntity().getRed());
            int g = parseInt(greenInput.getValue(), 0, 255, menu.getBlockEntity().getGreen());
            int b = parseInt(blueInput.getValue(), 0, 255, menu.getBlockEntity().getBlue());
            color = FastColor.ARGB32.color(r, g, b);
        }

        int fontSize = parseInt(fontSizeInput.getValue(), 1, 100, menu.getBlockEntity().getFontSize());
        boolean dropShadow = dropShadowButton.getMessage().getString().contains("ON");

        float offsetX = parseFloat(offsetXInput.getValue(), -Float.MAX_VALUE, Float.MAX_VALUE, offsetXSlider.getValue());
        float offsetY = parseFloat(offsetYInput.getValue(), -Float.MAX_VALUE, Float.MAX_VALUE, offsetYSlider.getValue());
        float offsetZ = parseFloat(offsetZInput.getValue(), -Float.MAX_VALUE, Float.MAX_VALUE, offsetZSlider.getValue());
        float rotation = parseFloat(rotationInput.getValue(), -Float.MAX_VALUE, Float.MAX_VALUE, rotationSlider.getValue());

        PacketDistributor.sendToServer(new ProjectorUpdatePayload(
                menu.getBlockPos(),
                textInput.getValue(),
                new ProjectorUpdatePayload.Metadata(
                    color,
                    fontSize,
					new ProjectorUpdatePayload.Metadata.Offset(
	                    offsetX,
	                    offsetY,
	                    offsetZ
					),
                    rotation,
                    dropShadow,
                    followPlayerCheckbox.selected()
                )
        ));
    }

    private int parseInt(String value, int min, int max, int defaultValue) {
        try {
            int parsed = Integer.parseInt(value);
            return Math.clamp(parsed, min, max);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private float parseFloat(String value, float min, float max, float defaultValue) {
        try {
            float parsed = Float.parseFloat(value);
            return Math.clamp(parsed, min, max);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        // Draw color preview
        int previewX = leftPos + 222;
        int previewY = topPos + 47;
        int previewSize = 18;

        int color;
        if (hslMode) {
            color = ProjectorBlockEntity.hslToRgb(cachedHue, cachedSat, cachedLight);
        } else {
            int r = parseInt(redInput.getValue(), 0, 255, 255);
            int g = parseInt(greenInput.getValue(), 0, 255, 255);
            int b = parseInt(blueInput.getValue(), 0, 255, 255);
            color = FastColor.ARGB32.color(r, g, b);
        }

        int displayColor = FastColor.ARGB32.opaque(color);
        guiGraphics.fill(previewX, previewY, previewX + previewSize, previewY + previewSize, displayColor);
        guiGraphics.renderOutline(previewX, previewY, previewSize, previewSize, 0xFF000000);

        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.fill(leftPos, topPos, leftPos + imageWidth, topPos + imageHeight, 0xFFC6C6C6);
        guiGraphics.renderOutline(leftPos, topPos, imageWidth, imageHeight, 0xFF000000);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(font, title, (imageWidth - font.width(title)) / 2, 6, 0x404040, false);

        int y = 25;
        guiGraphics.drawString(font, "Text:", 10, y, 0x404040, false);

        y += 27;

        if (hslMode) {
            guiGraphics.drawString(font, "H", 10, y, 0x404040, false);
            guiGraphics.drawString(font, "S", 80, y, 0x404040, false);
            guiGraphics.drawString(font, "L", 150, y, 0x404040, false);
        } else {
            guiGraphics.fill(LEFT_PADDING + 2, y, 12 + COLOR_SQUARE_SIZE, y + COLOR_SQUARE_SIZE, 0xFFFF0000);
            guiGraphics.renderOutline(LEFT_PADDING + 2, y, COLOR_SQUARE_SIZE, COLOR_SQUARE_SIZE, 0xFF000000);
            guiGraphics.fill(COLOR_AND_EDITBOX_TOTAL_SIZE + 12, y, COLOR_AND_EDITBOX_TOTAL_SIZE + 12 + COLOR_SQUARE_SIZE, y + COLOR_SQUARE_SIZE, 0xFF00FF00);
            guiGraphics.renderOutline(LEFT_PADDING + 2 + COLOR_AND_EDITBOX_TOTAL_SIZE, y, COLOR_SQUARE_SIZE, COLOR_SQUARE_SIZE, 0xFF000000);
            guiGraphics.fill(COLOR_AND_EDITBOX_TOTAL_SIZE * 2 + 12, y, COLOR_AND_EDITBOX_TOTAL_SIZE * 2 + 12 + COLOR_SQUARE_SIZE, y + COLOR_SQUARE_SIZE, 0xFF0000FF);
            guiGraphics.renderOutline(LEFT_PADDING + 2 + COLOR_AND_EDITBOX_TOTAL_SIZE * 2, y, COLOR_SQUARE_SIZE, COLOR_SQUARE_SIZE, 0xFF000000);
        }

        y += 22;
        guiGraphics.drawString(font, "Size:", 173, y, 0x404040, false);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (textInput.isFocused() || redInput.isFocused() || greenInput.isFocused() ||
                blueInput.isFocused() || fontSizeInput.isFocused() ||
                offsetXInput.isFocused() || offsetYInput.isFocused() || offsetZInput.isFocused() ||
                rotationInput.isFocused()) {
            if (keyCode == 256) { // Escape
                return super.keyPressed(keyCode, scanCode, modifiers);
            }
            return textInput.keyPressed(keyCode, scanCode, modifiers) ||
                    redInput.keyPressed(keyCode, scanCode, modifiers) ||
                    greenInput.keyPressed(keyCode, scanCode, modifiers) ||
                    blueInput.keyPressed(keyCode, scanCode, modifiers) ||
                    fontSizeInput.keyPressed(keyCode, scanCode, modifiers) ||
                    offsetXInput.keyPressed(keyCode, scanCode, modifiers) ||
                    offsetYInput.keyPressed(keyCode, scanCode, modifiers) ||
                    offsetZInput.keyPressed(keyCode, scanCode, modifiers) ||
                    rotationInput.keyPressed(keyCode, scanCode, modifiers);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
