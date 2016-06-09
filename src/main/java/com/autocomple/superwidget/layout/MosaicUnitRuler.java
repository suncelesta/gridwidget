package com.autocomple.superwidget.layout;

import com.google.gwt.dom.client.Element;

public class MosaicUnitRuler implements Ruler {

    private Element referenceElement;
    private int referenceWidthInUnits;
    private int referenceHeightInUnits;

    private double unitHeight;
    private double unitWidth;

    public MosaicUnitRuler(Element referenceElement,
                           int referenceWidthInUnits,
                           int referenceHeightInUnits) {
        this.referenceElement = referenceElement;
        this.referenceWidthInUnits = referenceWidthInUnits;
        this.referenceHeightInUnits = referenceHeightInUnits;
    }

    @Override
    public void adjust() {
        unitWidth = (double) referenceElement.getOffsetWidth() / referenceWidthInUnits;
        unitHeight = (double) referenceElement.getOffsetHeight() / referenceHeightInUnits;
    }

    @Override
    public double getUnitHeight() {
        return unitHeight;
    }

    @Override
    public double getUnitWidth() {
        return unitWidth;
    }

    @Override
    public int measureHeight(Element element) {
        return getDimensionInUnits(element.getOffsetHeight(), unitHeight);
    }

    @Override
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
