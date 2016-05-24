package com.autocomple.superwidget.command;

import com.google.gwt.event.shared.HasHandlers;

public class RemoveFromIndexCommand extends RemoveCommand {
    private int index;

    public RemoveFromIndexCommand(int index) {
        this.index = index;
    }

    public static void sendTo(HasHandlers source, int tile) {
        source.fireEvent(new RemoveFromIndexCommand(tile));
    }

    public int getIndex() {
        return index;
    }
}