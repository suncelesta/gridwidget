package com.autocomple.mosaic;

import com.google.gwt.dom.client.Style;

public class Dimension {
    private double value;
    private Style.Unit unit;

    public Dimension(double value, Style.Unit unit) {
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public Style.Unit getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return Double.toString(value) + unit.getType();
    }
}