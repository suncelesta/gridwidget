package com.autocomple.superwidget.layout;

import com.google.gwt.dom.client.Element;

public class PixelRuler implements Ruler {
    @Override
    public void adjust() {

    }

    @Override
    public double getUnitHeight() {
        return 1;
    }

    @Override
    public double getUnitWidth() {
        return 1;
    }

    @Override
    public int measureHeight(Element element) {
        return element.getOffsetHeight();
    }

    @Override
    public int measureWidth(Element element) {
        return element.getOffsetWidth();
    }
}
