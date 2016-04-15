package com.autocomple.mosaic.command;

import com.autocomple.mosaic.Tile;
import com.google.gwt.event.shared.HasHandlers;

public class RemoveCommand extends Command<RemoveCommand.RemoveHandler> {
    private Tile tile;

    public static final Type<RemoveHandler> TYPE = new Type<>();

    public RemoveCommand(Tile tile) {
        this.tile = tile;
    }

    @Override
    public Type<RemoveHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RemoveHandler handler) {
        handler.onCommand(this);
    }


    public static void sendTo(HasHandlers source, Tile tile) {
        source.fireEvent(new RemoveCommand(tile));
    }

    public Tile getTile() {
        return tile;
    }

    public interface RemoveHandler extends Command.Handler<RemoveCommand> {}
}