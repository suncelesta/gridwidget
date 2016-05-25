package com.autocomple.superwidget.command;

import com.google.gwt.event.shared.HasHandlers;

public class ClearCommand extends Command<ClearCommand.RemoveHandler> {

    public static void sendTo(HasHandlers source) {
        source.fireEvent(new ClearCommand());
    }

    public static final Type<RemoveHandler> TYPE = new Type<>();
    @Override
    public Type<RemoveHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RemoveHandler handler) {
        handler.onCommand(this);
    }

    public interface RemoveHandler extends Command.Handler<ClearCommand> {}
}
