package com.autocomple.superwidget.command;

import com.autocomple.superwidget.tile.Tile;

public abstract class AddCommand extends Command<AddCommand.AddHandler> {
    private Tile tile;

    public static final Type<AddHandler> TYPE = new Type<>();

    public AddCommand(Tile tile) {
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

    public Tile getTile() {
        return tile;
    }

    public interface AddHandler extends Command.Handler<AddCommand> {}
}
