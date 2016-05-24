package com.autocomple.superwidget.command;

import com.autocomple.superwidget.tile.Tile;
import com.google.gwt.event.shared.HasHandlers;

public class PrependCommand extends AddCommand {
    public PrependCommand(Tile tile) {
        super(tile);
    }

    public static void sendTo(HasHandlers source, Tile tile) {
        source.fireEvent(new PrependCommand(tile));
    }
}
