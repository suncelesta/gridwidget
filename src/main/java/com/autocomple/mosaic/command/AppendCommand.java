package com.autocomple.mosaic.command;

import com.autocomple.mosaic.Tile;
import com.google.gwt.event.shared.HasHandlers;

public class AppendCommand extends AddCommand {
    public AppendCommand(Tile tile) {
        super(tile);
    }

    public static void sendTo(HasHandlers source, Tile tile) {
        source.fireEvent(new AppendCommand(tile));
    }
}
