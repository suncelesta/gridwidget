package com.autocomple.superwidget;

import com.autocomple.superwidget.tile.CompositeTile;
import com.autocomple.superwidget.tile.Tile;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;

/***
 * A panel that allows to embed {@link Tile} into
 * elements that don't provide resize events.
 */
public class SuperWidgetPanel extends ResizeLayoutPanel {
    private Canvas canvas;

    public SuperWidgetPanel() {
        getElement().getStyle().setPosition(Style.Position.ABSOLUTE);

        this.canvas = new Canvas();

        super.setWidget(canvas);
    }

    public void setWidget(SuperWidget superWidget) {
        canvas.clear();
        canvas.setCommandEventBus(superWidget.getCommandEventBus());
        canvas.addTile(superWidget.getRootTile(), 0);
    }

    private class Canvas extends SuperWidget {
        public Canvas() {
            super(new CompositeTile(null));
        }

        public void setCommandEventBus(EventBus commandEventBus) {
            getRootTile().setCommandEventBus(commandEventBus);
        }
    }
}
