package com.autocomple.superwidget.command;

import com.autocomple.superwidget.tile.Tile;
import com.google.gwt.event.shared.HasHandlers;

public class AppendCommand extends Command<AppendCommand.AppendHandler> {
    private Tile tile;

    public AppendCommand(Tile tile) {
        this.tile = tile;
    }

    public static void sendTo(HasHandlers source, Tile tile) {
        source.fireEvent(new AppendCommand(tile));
    }


    public static final Type<AppendHandler> TYPE = new Type<>();

    @Override
    public Type<AppendHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AppendHandler handler) {
        handler.onCommand(this);
    }

    public Tile getTile() {
        return tile;
    }

    public interface AppendHandler extends Command.Handler<AppendCommand> {}
}
