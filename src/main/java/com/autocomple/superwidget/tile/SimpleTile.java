package com.autocomple.superwidget.tile;

import com.autocomple.superwidget.command.UpdateCommand;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;

/***
 * A mosaic tile that represents a single value,
 * and which contents are rendered by a cell.
 *
 * @param <Value> the type of the represented value
 */
public class SimpleTile<Value> extends Tile {

    private Cell<Value> cell;

    /**
     * @param cell     the cell used to render tile content
     */
    protected SimpleTile(Cell<Value> cell, EventBus commandEventBus) {
        super(new SimpleLayoutPanel(), commandEventBus);
        this.cell = cell;
    }

    @Override
    protected void addCommandHandlers() {
        addUpdateCommandHandler();
    }

    protected void addUpdateCommandHandler() {
        addCommandHandler(UpdateCommand.TYPE, new UpdateCommand.UpdateHandler<Value>() {
            @Override
            public void onCommand(UpdateCommand<Value> command) {
                update(null, command.getValue());
            }
        });
    }

    protected void update(Cell.Context cellContext, Value value) {
        cell.setValue(cellContext, getElement(), value);
    }
}
