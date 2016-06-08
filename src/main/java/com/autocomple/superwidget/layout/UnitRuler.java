package com.autocomple.superwidget.layout;

import com.google.gwt.dom.client.Element;

import java.math.BigDecimal;
import java.math.RoundingMode;

//todo refactor
public class UnitRuler {
    private static final int SCROLLBAR_WIDTH = getScrollBarWidth();

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
        unitWidth = (double) getDiscountedDimension(referenceElement, false) / referenceWidthInUnits;
        unitHeight = (double) getDiscountedDimension(referenceElement, true) / referenceHeightInUnits;
    }

    public double getUnitHeight() {
        return unitHeight;
    }

    public double getUnitWidth() {
        return unitWidth;
    }

    public int measureHeight(Element element) {
        return getDimensionInUnits(element, true);
    }

    public int measureWidth(Element element) {
        return getDimensionInUnits(element, false);
    }

    private int getDimensionInUnits(Element element, boolean vertical) {
        int dimensionInPx = getDimensionInPx(element, vertical);
        double normalizationParameter = getNormalizationParameter(element.getParentElement(), vertical);

        double unitSize = vertical ? unitHeight : unitWidth;

        return round((double)discount(dimensionInPx) / unitSize);
    }

    private int getDiscountedDimension(Element element, boolean vertical) {
        return discount(getDimensionInPx(element, vertical));
    }

    private double getNormalizationParameter(Element element, boolean vertical) {
        int dimensionInPx = getDimensionInPx(element, vertical);

        return (double) discount(dimensionInPx) / dimensionInPx;
    }

    private int getDimensionInPx(Element element, boolean vertical) {
        return vertical ?
                element.getOffsetHeight() :
                element.getOffsetWidth();
    }

    private int discount(int dimension) {
        return dimension - SCROLLBAR_WIDTH;
    }

    //todo: use Math.round from JS?
    // this is more correct for tile placement than simply ceiling,
    // as we care about at least half a pixel
    private int round(double value) {
        return new BigDecimal(value).setScale(0, RoundingMode.HALF_UP).intValue();
    }


    private static native int getScrollBarWidth() /*-{
        var scr = null;
        var inn = null;
        var wNoScroll = 0;
        var wScroll = 0;

        // Outer scrolling div
        scr = $wnd.document.createElement('div');
        scr.style.position = 'absolute';
        scr.style.top = '-1000px';
        scr.style.left = '-1000px';
        scr.style.width = '100px';
        scr.style.height = '50px';
        // Start with no scrollbar
        scr.style.overflow = 'hidden';

        // Inner content div
        inn = $wnd.document.createElement('div');
        inn.style.width = '100%';
        inn.style.height = '200px';

        // Put the inner div in the scrolling div
        scr.appendChild(inn);
        // Append the scrolling div to the doc

        $wnd.document.body.appendChild(scr);

        // Width of the inner div sans scrollbar
        wNoScroll = inn.offsetWidth;
        // Add the scrollbar
        scr.style.overflow = 'auto';
        // Width of the inner div width scrollbar
        wScroll = inn.offsetWidth;

        // Remove the scrolling div from the doc
        $wnd.document.body.removeChild(
            $wnd.document.body.lastChild);

        // Pixel width of the scroller
        return (wNoScroll - wScroll);
    }-*/;
}
