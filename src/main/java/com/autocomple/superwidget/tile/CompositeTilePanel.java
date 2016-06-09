package com.autocomple.superwidget.tile;

import com.autocomple.superwidget.util.Container;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class CompositeTilePanel extends ComplexPanel implements RequiresResize {

    public CompositeTilePanel() {
        setElement(Document.get().createDivElement());
        getElement().getStyle().setPosition(Style.Position.RELATIVE);
    }

    @Override
    public void add(Widget child) {
        Container childContainer = createContainer(child);

        add(child, childContainer.getElement());
    }

    public void insert(Widget child, int beforeIndex) {
        Container childContainer = createContainer(child);

        insert(child, childContainer.getElement(), beforeIndex, true);
    }

    @Override
    public boolean remove(Widget w) {
        boolean removed = super.remove(w);

        if (removed) {
            ((Container)w.getLayoutData()).getElement().removeFromParent();
            w.setLayoutData(null);
        }

        return removed;
    }

    /**
     * This method must be called whenever the implementor's size has been
     * modified.
     */
    @Override
    public void onResize() {
        for (Widget child : getChildren()) {
            if (child instanceof RequiresResize) {
                ((RequiresResize)child).onResize();
            }
        }
    }

    public Container getChildContainer(Widget child) {
        checkIsChild(child);

        return (Container)child.getLayoutData();
    }

    public void setChildTop(Widget child, double topValue, Style.Unit topUnit) {
        checkIsChild(child);

        Scheduler.get().scheduleFinally(() -> {
            UIObject childContainer = getChildContainer(child);

            Style childContainerStyle = childContainer.getElement().getStyle();

            childContainerStyle.setTop(topValue, topUnit);
        });
    }

    public void setChildLeft(Widget child, double leftValue, Style.Unit leftUnit) {
        checkIsChild(child);

        Scheduler.get().scheduleFinally(() -> {
            UIObject childContainer = getChildContainer(child);

            Style childContainerStyle = childContainer.getElement().getStyle();

            childContainerStyle.setLeft(leftValue, leftUnit);
        });
    }

    public void setChildContainerHeight(Widget child, String height) {
        checkIsChild(child);

        if (height != null) {
            getChildContainer(child).setHeight(height);
        }
    }

    public void setChildContainerWidth(Widget child, String width) {
        checkIsChild(child);

        if (width != null) {
            getChildContainer(child).setWidth(width);
        }
    }

    public void setChildContainerClassName(Widget child, String className) {
        checkIsChild(child);

        if (className != null) {
            getChildContainer(child).setStyleName(className);
        }
    }

    private void checkIsChild(Widget child) {
        assert getWidgetIndex(child) > -1 : "The specified widget is not a child of this panel";
    }

    private Container createContainer(Widget child) {
        Element containerElement = Document.get().createDivElement();
        containerElement.getStyle().setPosition(Style.Position.ABSOLUTE);

        getElement().appendChild(containerElement);

        Container container = new Container(containerElement);

        child.setLayoutData(container);

        return container;
    }
}
