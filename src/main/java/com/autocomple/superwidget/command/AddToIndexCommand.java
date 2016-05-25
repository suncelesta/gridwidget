package com.autocomple.superwidget.command;

import com.autocomple.superwidget.tile.Tile;
import com.google.gwt.event.shared.HasHandlers;

public class AddToIndexCommand extends Command<AddToIndexCommand.AddToIndexHandler> {
    private int index;
    private Tile tile;

    public AddToIndexCommand(int index, Tile tile) {
        this.tile = tile;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static void sendTo(HasHandlers source, int index, Tile tile) {
        source.fireEvent(new AddToIndexCommand(index, tile));
    }

    public static final Type<AddToIndexHandler> TYPE = new Type<>();

    @Override
    public Type<AddToIndexHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AddToIndexHandler handler) {
        handler.onCommand(this);
    }

    public Tile getTile() {
        return tile;
    }

    public interface AddToIndexHandler extends Command.Handler<AddToIndexCommand> {}
}
