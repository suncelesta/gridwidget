package com.autocomple.superwidget.layout;

import com.google.gwt.dom.client.Element;

public interface Ruler {
    void adjust();

    double getUnitHeight();

    double getUnitWidth();

    int measureHeight(Element element);

    int measureWidth(Element element);
}
