package com.autocomple.mosaic.command;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public abstract class Command<H extends Command.Handler> extends GwtEvent<H> {

    public interface Handler<C extends Command> extends EventHandler {
        void onCommand(C command);
    }
}
