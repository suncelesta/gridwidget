package com.autocomple.superwidget.command;

import com.autocomple.superwidget.tile.Tile;
import com.google.gwt.event.shared.HasHandlers;

public class PrependCommand extends Command<PrependCommand.PrependHandler> {
    private Tile tile;

    public PrependCommand(Tile tile) {
        this.tile = tile;
    }

    public static void sendTo(HasHandlers source, Tile tile) {
        source.fireEvent(new PrependCommand(tile));
    }


    public static final Type<PrependHandler> TYPE = new Type<>();

    @Override
    public Type<PrependHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PrependHandler handler) {
        handler.onCommand(this);
    }

    public Tile getTile() {
        return tile;
    }

    public interface PrependHandler extends Command.Handler<PrependCommand> {}
}
