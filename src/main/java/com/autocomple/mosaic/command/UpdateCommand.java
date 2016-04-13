package com.autocomple.mosaic.command;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class UpdateCommand<Value> extends Command<UpdateCommand.UpdateHandler> {
    private Value value;

    public static final GwtEvent.Type<UpdateHandler> TYPE = new GwtEvent.Type<>();

    public UpdateCommand(Value value) {
        this.value = value;
    }

    @Override
    public GwtEvent.Type<UpdateHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(UpdateHandler handler) {
        handler.onCommand(this);
    }

    public Value getValue() {
        return value;
    }

    public static <Value> void sendTo(HasHandlers source, Value value) {
        source.fireEvent(new UpdateCommand(value));
    }

    public interface UpdateHandler<Value> extends Command.Handler<UpdateCommand<Value>> {}
}
