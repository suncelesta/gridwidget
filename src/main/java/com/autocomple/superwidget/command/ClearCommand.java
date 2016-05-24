package com.autocomple.superwidget.command;

import com.google.gwt.event.shared.HasHandlers;

public class ClearCommand extends RemoveCommand {

    public static final Type<RemoveHandler> TYPE = new Type<>();

    public static void sendTo(HasHandlers source) {
        source.fireEvent(new ClearCommand());
    }
}
