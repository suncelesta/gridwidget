package com.autocomple.superwidget.command;

import com.autocomple.superwidget.tile.Tile;
import com.google.gwt.event.shared.HasHandlers;

public class RemoveTileCommand extends Command<RemoveTileCommand.RemoveTileHandler> {
    private Tile tile;

    public RemoveTileCommand(Tile tile) {
        this.tile = tile;
    }

    public static void sendTo(HasHandlers source, Tile tile) {
        source.fireEvent(new RemoveTileCommand(tile));
    }

    public Tile getTile() {
        return tile;
    }

    public static final Type<RemoveTileHandler> TYPE = new Type<>();
    @Override
    public Type<RemoveTileHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RemoveTileHandler handler) {
        handler.onCommand(this);
    }

    public interface RemoveTileHandler extends Command.Handler<RemoveTileCommand> {}
}
