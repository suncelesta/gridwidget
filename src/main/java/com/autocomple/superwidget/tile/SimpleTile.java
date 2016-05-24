package com.autocomple.superwidget.tile;

import com.autocomple.superwidget.command.UpdateCommand;
import com.google.gwt.cell.client.Cell;
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
    protected SimpleTile(Cell<Value> cell) {
        this(getDefaultResources(), cell);
    }

    /**
     * @param resources tile resources
     * @param cell     the cell used to render tile content
     */
    protected SimpleTile(Resources resources,
                         Cell<Value> cell) {
        super(new SimpleLayoutPanel(), resources);
        this.cell = cell;
    }

    @Override
    protected void addCommandHandlers() {
        addUpdateCommandHandler();
    }

    protected void addUpdateCommandHandler() {
        addCommandHandler(new UpdateCommand.UpdateHandler<Value>() {
            @Override
            public void onCommand(UpdateCommand<Value> command) {
                update(null, command.getValue());
            }
        }, UpdateCommand.TYPE);
    }

    protected void update(Cell.Context cellContext, Value value) {
        cell.setValue(cellContext, getElement(), value);
    }
}
