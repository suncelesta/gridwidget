package com.autocomple.superwidget.util;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.UIObject;

public class Container extends UIObject {
    private Object layoutStrategyData;

    public Container(Element element) {
        setElement(element);
    }

    public Object getLayoutStrategyData() {
        return layoutStrategyData;
    }

    public void setLayoutStrategyData(Object layoutStrategyData) {
        this.layoutStrategyData = layoutStrategyData;
    }
}
