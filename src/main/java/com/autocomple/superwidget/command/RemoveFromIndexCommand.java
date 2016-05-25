package com.autocomple.superwidget.command;

import com.google.gwt.event.shared.HasHandlers;

public class RemoveFromIndexCommand extends Command<RemoveFromIndexCommand.RemoveFromIndexHandler> {
    private int index;

    public RemoveFromIndexCommand(int index) {
        this.index = index;
    }

    public static void sendTo(HasHandlers source, int tile) {
        source.fireEvent(new RemoveFromIndexCommand(tile));
    }

    public int getIndex() {
        return index;
    }

    public static final Type<RemoveFromIndexHandler> TYPE = new Type<>();

    @Override
    public Type<RemoveFromIndexHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RemoveFromIndexHandler handler) {
        handler.onCommand(this);
    }

    public interface RemoveFromIndexHandler extends Command.Handler<RemoveFromIndexCommand> {}
}