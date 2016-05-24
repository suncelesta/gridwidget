package com.autocomple.superwidget.command;

public abstract class RemoveCommand extends Command<RemoveCommand.RemoveHandler> {

    public static final Type<RemoveHandler> TYPE = new Type<>();
    @Override
    public Type<RemoveHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RemoveHandler handler) {
        handler.onCommand(this);
    }

    public interface RemoveHandler extends Command.Handler<RemoveCommand> {}
}