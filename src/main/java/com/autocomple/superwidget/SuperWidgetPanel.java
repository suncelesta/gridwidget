package com.autocomple.superwidget;

import com.autocomple.superwidget.tile.Tile;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;

/***
 * A panel that allows to embed {@link Tile} into
 * elements that don't provide resize events.
 */
public class SuperWidgetPanel extends ResizeLayoutPanel {
    public SuperWidgetPanel() {
        getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
    }
}
