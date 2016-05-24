package com.autocomple.superwidget.command;

import com.autocomple.superwidget.tile.Tile;
import com.google.gwt.event.shared.HasHandlers;

public class RemoveTileCommand extends RemoveCommand {
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
}
