package com.autocomple.superwidget;

import com.autocomple.superwidget.tile.CellSimpleTile;
import com.autocomple.superwidget.tile.Tile;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public class SuperList<Value> extends SuperHasValues<Value> {

    private static Resources DEFAULT_RESOURCES;

    private Style style;

    public SuperList(Cell<Value> cell) {
        this(cell, getDefaultResources());
    }

    public SuperList(Cell<Value> cell,
                     Resources resources) {
        this(cell, resources, new SimpleEventBus());
    }

    public SuperList(Cell<Value> cell,
                     Resources resources,
                     EventBus commandEventBus) {

        super(new ListTile<>(cell, commandEventBus), commandEventBus);

        this.style = resources.superListStyle();
        this.style.ensureInjected();

        getRootTile().getContainerStyle().setClassName(style.superListWidget());
    }

    @Override
    protected Tile createChild(Value value) {
        Tile child = new ListTileItem<>(getCell(), getCommandEventBus());

        child.getContainerStyle().setClassName(style.superListItem());

        return child;
    }

    static class ListTile<Value> extends HasValuesTile<Value> {
        public ListTile(Cell<Value> cell, EventBus commandEventBus) {
            super(cell, commandEventBus);
        }
    }

    static class ListTileItem<Value> extends CellSimpleTile<Value> {
        /**
         * @param cell the cell used to render tile content
         */
        public ListTileItem(Cell<Value> cell, EventBus commandEventBus) {
            super(cell, commandEventBus);

            getContainerStyle().setWidth(100, com.google.gwt.dom.client.Style.Unit.PCT);
        }
    }

    public interface Style extends CssResource {
        String DEFAULT_CSS = "com/autocomple/superwidget/superlist.css";

        String superListItem();

        String superListWidget();
    }

    public interface Resources extends ClientBundle {

        @Source(Style.DEFAULT_CSS)
        Style superListStyle();
    }

    protected static Resources getDefaultResources() {
        if (DEFAULT_RESOURCES == null) {
            DEFAULT_RESOURCES = GWT.create(Resources.class);
        }
        return DEFAULT_RESOURCES;
    }
}
