package com.reclipse.projector.client.widgets;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.function.Consumer;

public class IntegerSlider extends AbstractSliderButton {
    private final int minValue;
    private final int maxValue;
    private final String label;
    private final Consumer<Integer> onValueChanged;

    public IntegerSlider(int x, int y, int width, int height, String label, int minValue, int maxValue, int currentValue, Consumer<Integer> onValueChanged) {
        super(x, y, width, height, Component.empty(), valueToSlider(currentValue, minValue, maxValue));
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.label = label;
        this.onValueChanged = onValueChanged;
        updateMessage();
    }

    private static double valueToSlider(int value, int min, int max) {
        return (double) (value - min) / (double) (max - min);
    }

    private int sliderToValue() {
        return (int) Math.round(Mth.lerp(this.value, this.minValue, this.maxValue));
    }

    @Override
    protected void updateMessage() {
        setMessage(Component.literal(label + ": " + sliderToValue()));
    }

    @Override
    protected void applyValue() {
        onValueChanged.accept(sliderToValue());
    }

    public int getValue() {
        return sliderToValue();
    }

    public void setValue(int newValue) {
        this.value = valueToSlider(Mth.clamp(newValue, minValue, maxValue), minValue, maxValue);
        updateMessage();
    }
}
