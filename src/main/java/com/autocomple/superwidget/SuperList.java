package com.autocomple.superwidget;

import com.autocomple.superwidget.tile.SimpleTile;
import com.autocomple.superwidget.tile.Tile;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.event.shared.EventBus;

public class SuperList<Value> extends SuperHasValues<Value> {

    public SuperList(Cell<Value> cell) {
        super(new ListTile<>(cell));
    }

    public SuperList(Cell<Value> cell, EventBus commandEventBus) {
        super(new ListTile<>(cell), commandEventBus);
    }

    @Override
    protected Tile createChild(Value value) {
        Tile child = new ListTileItem<>(getCell());

        child.setCommandEventBus(getCommandEventBus());

        return child;
    }

    static class ListTile<Value> extends HasValuesTile<Value> {
        public ListTile(Cell<Value> cell) {
            super(cell);
        }
    }

    static class ListTileItem<Value> extends SimpleTile<Value> {
        /**
         * @param cell the cell used to render tile content
         */
        public ListTileItem(Cell<Value> cell) {
            super(cell);

            setContainerWidth(100, com.google.gwt.dom.client.Style.Unit.PCT);
            //todo: temp
            setContainerHeight(40, com.google.gwt.dom.client.Style.Unit.PX);
        }

        //todo: use in resources
        public interface Style extends Tile.Style {

            @ClassName("tile-list-item")
            String tileContainer();
        }
    }
}
