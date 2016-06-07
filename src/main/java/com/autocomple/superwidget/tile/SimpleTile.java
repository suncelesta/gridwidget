package com.autocomple.superwidget.tile;

import com.autocomple.superwidget.command.UpdateCommand;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/***
 * A mosaic tile that represents a single value,
 * and which contents are rendered by a cell.
 *
 * @param <Value> the type of the represented value
 */
public abstract class SimpleTile<Value> extends Tile {

    /**
     * Constructs a new {@code SimpleTile}.
     *
     * @param wrappedWidget   the wrapped widget; must extend {@link RequiresResize}
     * @param commandEventBus event bus used to receive commands
     */
    public SimpleTile(Widget wrappedWidget, EventBus commandEventBus) {
        super(wrappedWidget, commandEventBus);
    }

    /**
     * Constructs a new {@code SimpleTile}.
     *
     * @param commandEventBus event bus used to receive commands
     */
    public SimpleTile(EventBus commandEventBus) {
        super(new SimplePanel(), commandEventBus);
    }

    @Override
    protected void addCommandHandlers() {
        addUpdateCommandHandler();
    }

    protected void addUpdateCommandHandler() {
        addCommandHandler(UpdateCommand.TYPE, new UpdateCommand.UpdateHandler<Value>() {
            @Override
            public void onCommand(UpdateCommand<Value> command) {
                update(command.getValue());
            }
        });
    }

    protected abstract void update(Value value);

    /**
     * This method must be called whenever the implementor's size has been
     * modified.
     */
    @Override
    public void onResize() {
        if (getWidget() instanceof RequiresResize) {
            ((RequiresResize)getWidget()).onResize();
        }
    }
}
