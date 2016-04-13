package com.autocomple.mosaic.command;

import com.autocomple.mosaic.Tile;
import com.google.gwt.event.shared.HasHandlers;

public class AddCommand extends Command<AddCommand.AddHandler> {
    private int index;
    private Tile tile;


    public static final Type<AddHandler> TYPE = new Type<>();

    public AddCommand(int index, Tile tile) {
        this.index = index;
        this.tile = tile;
    }

    @Override
    public Type<AddHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AddHandler handler) {
        handler.onCommand(this);
    }

    public static void sendTo(HasHandlers source, int index, Tile tile) {
        source.fireEvent(new AddCommand(index, tile));
    }

    public int getIndex() {
        return index;
    }

    public Tile getTile() {
        return tile;
    }

    public interface AddHandler extends Command.Handler<AddCommand> {}
}
