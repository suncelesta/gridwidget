package com.autocomple.mosaic;

import com.autocomple.mosaic.command.UpdateCommand;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.event.shared.EventBus;

/***
 * A mosaic tile that represents a single value,
 * and which contents are rendered by a cell.
 *
 * @param <Value> the type of the represented value
 */
public class SingletonTile<Value> extends Tile {

    private Cell<Value> cell;

    /**
     * @param commandEventBus the event bus used to provide command events
     * @param cell     the cell used to render unit content
     */
    protected SingletonTile(EventBus commandEventBus,
                            Cell<Value> cell) {
        this(commandEventBus, getDefaultResources(), cell);
    }

    /**
     * @param commandEventBus the event bus used to provide command events
     * @param resources tile resources
     * @param cell     the cell used to render unit content
     */
    protected SingletonTile(EventBus commandEventBus,
                            Resources resources,
                            Cell<Value> cell) {
        super(commandEventBus, resources);
        this.cell = cell;

        addUpdateCommandHandler();
    }

    protected void update(Cell.Context cellContext, Value value) {
        cell.setValue(cellContext, getElement(), value);
    }

    protected void addUpdateCommandHandler() {
        addCommandHandler(new UpdateCommand.UpdateHandler<Value>() {
            @Override
            public void onCommand(UpdateCommand<Value> command) {
                update(null, command.getValue());
            }
        }, UpdateCommand.TYPE);
    }
}
