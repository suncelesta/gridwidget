package com.autocomple.superwidget.layout;

import com.google.gwt.dom.client.Element;

public class UnitRuler {

    private Element referenceElement;
    private int referenceWidthInUnits;
    private int referenceHeightInUnits;

    private double unitHeight;
    private double unitWidth;

    public UnitRuler(Element referenceElement,
                     int referenceWidthInUnits,
                     int referenceHeightInUnits) {
        this.referenceElement = referenceElement;
        this.referenceWidthInUnits = referenceWidthInUnits;
        this.referenceHeightInUnits = referenceHeightInUnits;
    }

    public void adjust() {
        unitWidth = (double) referenceElement.getOffsetWidth() / referenceWidthInUnits;
        unitHeight = (double) referenceElement.getOffsetHeight() / referenceHeightInUnits;
    }

    public double getUnitHeight() {
        return unitHeight;
    }

    public double getUnitWidth() {
        return unitWidth;
    }

    public int measureHeight(Element element) {
        return getDimensionInUnits(element.getOffsetHeight(), unitHeight);
    }

    public int measureWidth(Element element) {
        return getDimensionInUnits(element.getOffsetWidth(), unitWidth);
    }

    private int getDimensionInUnits(int dimensionInPx, double unitSize) {
        return round(dimensionInPx / unitSize);
    }

    private native int round(double value) /*-{
        return Math.round(value);
    }-*/;
}
