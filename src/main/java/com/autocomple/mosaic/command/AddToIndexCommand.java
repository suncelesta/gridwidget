package com.autocomple.mosaic.command;

import com.autocomple.mosaic.Tile;
import com.google.gwt.event.shared.HasHandlers;

public class AddToIndexCommand extends AddCommand {
    private int index;

    public AddToIndexCommand(int index, Tile tile) {
        super(tile);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static void sendTo(HasHandlers source, int index, Tile tile) {
        source.fireEvent(new AddToIndexCommand(index, tile));
    }
}
