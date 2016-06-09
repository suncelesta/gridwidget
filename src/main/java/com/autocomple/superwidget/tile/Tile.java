package com.autocomple.superwidget.tile;

import com.autocomple.superwidget.command.Command;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashSet;
import java.util.Set;

/**
 * A tile of the widget mosaic.
 */
public abstract class Tile extends Composite implements RequiresResize {

    private EventBus commandEventBus;

    private ContainerStyle containerStyle = new ContainerStyle();

    private Set<HandlerRegistration> commandHandlers = new HashSet<>();

    public static class ContainerStyle {
        private String height;
        private String width;
        private String className;

        public void setHeight(double value, Style.Unit unit) {
            this.height = Double.toString(value) + unit.getType();
        }

        public void setWidth(double value, Style.Unit unit) {
            this.width = Double.toString(value) + unit.getType();
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getHeight() {
            return height;
        }

        public String getWidth() {
            return width;
        }
    }

    public ContainerStyle getContainerStyle() {
        return containerStyle;
    }

    /**
     * Constructs a new {@code Tile}.
     *
     * @param wrappedWidget the wrapped widget; must extend {@link RequiresResize}
     * @param commandEventBus event bus used to receive commands
     */
    protected Tile(Widget wrappedWidget, EventBus commandEventBus) {
        initWidget(wrappedWidget);

        setCommandEventBus(commandEventBus);
    }

    protected abstract void addCommandHandlers();

    public void setCommandEventBus(EventBus commandEventBus) {
        this.commandEventBus = commandEventBus;

        removeAllCommandHandlers();

        if (commandEventBus != null) {
            addCommandHandlers();
        }
    }

    private void removeAllCommandHandlers() {
        for (HandlerRegistration commandHandler : commandHandlers) {
            commandHandler.removeHandler();
        }
    }

    /**
     * Adds the handler for a command event. Command events
     * are used to manipulate with tile's content: add,
     * remove and update it.
     *
     * @param <H> handler type
     *
     * @param type event type
     *
     * @param handler the handler
     * @return a {@link HandlerRegistration} to remove the handler
     */
    protected <H extends Command.Handler> HandlerRegistration addCommandHandler(
            GwtEvent.Type<H> type, final H handler) {

        if (commandEventBus == null) return null;

        HandlerRegistration handlerRegistration = commandEventBus.addHandlerToSource(type, this, handler);

        commandHandlers.add(handlerRegistration);

        return handlerRegistration;
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        if (event instanceof Command && commandEventBus != null) {
            commandEventBus.fireEventFromSource(event, this);
        } else {
            super.fireEvent(event);
        }
    }
}
