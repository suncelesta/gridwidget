package com.autocomple.mosaic.command;

import com.autocomple.mosaic.Tile;

public class RemoveCommand extends Command<RemoveCommand.RemoveHandler> {
    private int index;
    private Tile tile;

    public static final Type<RemoveHandler> TYPE = new Type<>();

    public RemoveCommand(Tile tile) {
        this.tile = tile;
    }

    public RemoveCommand(int index) {
        this.index = index;
    }

    @Override
    public Type<RemoveHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RemoveHandler handler) {
        handler.onCommand(this);
    }

    public int getIndex() {
        return index;
    }

    public Tile getTile() {
        return tile;
    }

    public interface RemoveHandler extends Command.Handler<RemoveCommand> {}
}