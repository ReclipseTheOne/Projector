package com.reclipse.projector.client.widgets;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.function.Consumer;

/**
 * A slider widget that works with float values, supporting decimal precision.
 */
public class FloatSlider extends AbstractSliderButton {
    private final float minValue;
    private final float maxValue;
    private final String label;
    private final Consumer<Float> onValueChanged;
    private final int decimalPlaces;

    public FloatSlider(int x, int y, int width, int height, String label, float minValue, float maxValue, float currentValue, Consumer<Float> onValueChanged) {
        this(x, y, width, height, label, minValue, maxValue, currentValue, 1, onValueChanged);
    }

    public FloatSlider(int x, int y, int width, int height, String label, float minValue, float maxValue, float currentValue, int decimalPlaces, Consumer<Float> onValueChanged) {
        super(x, y, width, height, Component.empty(), valueToSliderClamped(currentValue, minValue, maxValue));
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.label = label;
        this.decimalPlaces = decimalPlaces;
        this.onValueChanged = onValueChanged;
        updateMessage();
    }

    private static double valueToSliderClamped(float value, float min, float max) {
        float clamped = Mth.clamp(value, min, max);
        return (clamped - min) / (max - min);
    }

    private static double valueToSlider(float value, float min, float max) {
        return (value - min) / (max - min);
    }

    private float sliderToValue() {
        return (float) Mth.lerp(this.value, this.minValue, this.maxValue);
    }

    @Override
    protected void updateMessage() {
        String format = "%." + decimalPlaces + "f";
        setMessage(Component.literal(label + ": " + String.format(format, sliderToValue())));
    }

    @Override
    protected void applyValue() {
        onValueChanged.accept(sliderToValue());
    }

    public float getValue() {
        return sliderToValue();
    }

    public void setValue(float newValue) {
        this.value = valueToSlider(Mth.clamp(newValue, minValue, maxValue), minValue, maxValue);
        updateMessage();
    }
}
