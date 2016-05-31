package com.autocomple.superwidget;

import com.autocomple.superwidget.command.RemoveFromIndexCommand;
import com.autocomple.superwidget.tile.CompositeTile;
import com.autocomple.superwidget.tile.Tile;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.event.shared.EventBus;

import java.util.ArrayList;
import java.util.List;

public abstract class SuperHasValues<Value> extends SuperWidget implements HasValues<Value> {

    private List<Value> values;
    private Cell<Value> cell;

    protected SuperHasValues(HasValuesTile<Value> rootTile, EventBus commandEventBus) {
        super(rootTile, commandEventBus);

        this.cell = rootTile.getCell();
    }

    @Override
    public void setValues(List<Value> values) {
        clear();

        for (int i = 0; i < values.size(); i++) {
            addValue(values.get(i), i);
        }

        this.values = values;

    }

    @Override
    public void addValue(Value value) {
        addValue(value, ensureValues().size());
    }

    @Override
    public void addValue(Value value, int index) {
        Tile child = createChild(value);

        addTile(child, index);

        updateTile(child, value);
    }

    @Override
    public void removeValue(Value value) {
        int valueIndex = ensureValues().indexOf(value);

        if (valueIndex > -1) {

            ensureValues().remove(value);

            RemoveFromIndexCommand.sendTo(getRootTile(), valueIndex);
        }

    }

    private List<Value> ensureValues() {
        if (values == null) {
            values = new ArrayList<>();
        }

        return values;
    }

    protected abstract Tile createChild(Value value);

    protected Cell<Value> getCell() {
        return cell;
    }

    public static class HasValuesTile<Value> extends CompositeTile {
        private Cell<Value> cell;

        public HasValuesTile(Cell<Value> cell, EventBus commandEventBus) {
            super(commandEventBus);
            this.cell = cell;
        }

        /**
         * @param internalWidthInUnits  the width of inner mosaic in units
         * @param internalHeightInUnits the height of inner mosaic in units
         */
        public HasValuesTile(int internalWidthInUnits,
                             int internalHeightInUnits,
                             Cell<Value> cell,
                             EventBus commandEventBus) {
            super(internalWidthInUnits, internalHeightInUnits, commandEventBus);
            this.cell = cell;
        }


        public Cell<Value> getCell() {
            return cell;
        }
    }
}
