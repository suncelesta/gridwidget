package com.autocomple.superwidget.tile;

import com.autocomple.superwidget.Mosaic;
import com.autocomple.superwidget.command.Command;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesBuilder;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;

/**
 * A tile of the widget mosaic.
 */
public abstract class Tile extends ResizeComposite {

    private EventBus commandEventBus;

    private ContainerSettings containerSettings = new ContainerSettings();

    protected HandlerRegistration attachHandler;

    public static class ContainerSettings {
        private SafeStylesBuilder stylesBuilder = new SafeStylesBuilder();
        private String className;

        public SafeStyles getStyles() {
            return stylesBuilder.toSafeStyles();
        }

        public void setHeight(double value, Style.Unit unit) {
            stylesBuilder.height(value, unit);
        }

        public void setWidth(double value, Style.Unit unit) {
            stylesBuilder.width(value, unit);
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }
    }

    public ContainerSettings getContainerSettings() {
        return containerSettings;
    }

    /**
     * Constructs a new {@code Tile}.
     *
     * @param wrappedWidget the wrapped widget; must extend {@link RequiresResize}
     */
    protected Tile(Widget wrappedWidget, EventBus commandEventBus) {
        initWidget(wrappedWidget);

        this.attachHandler = addAttachHandler((e) -> {
            String className = getContainerSettings().getClassName();
            if (className != null) {
                getElement().getParentElement().addClassName(className);
            }
        });


        this.commandEventBus = commandEventBus;
        addCommandHandlers();
    }

    protected abstract void addCommandHandlers();

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
        return commandEventBus.addHandlerToSource(type, this, handler);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        if (event instanceof Command) {
            commandEventBus.fireEventFromSource(event, this);
        } else {
            super.fireEvent(event);
        }
    }

    protected Mosaic.UnitMatrix getMatrix(int height,
                                          int width) {

        Mosaic.UnitMatrix result = new Mosaic.UnitMatrix(height, width);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                result.setOccupied(i, j, true);
            }
        }

        return result;
    }
}
