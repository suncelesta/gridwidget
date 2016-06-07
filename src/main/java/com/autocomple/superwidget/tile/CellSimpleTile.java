package com.autocomple.superwidget.tile;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.event.shared.EventBus;

public class CellSimpleTile<Value> extends SimpleTile<Value> {
    private Cell<Value> cell;

    /**
     * Constructs a new {@code SimpleTile}.
     *  @param commandEventBus event bus used to receive commands
     * @param cell the cell used to render tile content
     */
    public CellSimpleTile(Cell<Value> cell, EventBus commandEventBus) {
        super(commandEventBus);
        this.cell = cell;
    }

    @Override
    protected void update(Value value) {
        cell.setValue(getContext(), getElement(), value);
    }

    protected Cell.Context getContext() {
        return null;
    }
}
