package com.autocomple.superwidget.tile;

import com.autocomple.superwidget.Mosaic;
import com.autocomple.superwidget.command.Command;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesBuilder;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;

/**
 * A tile of the widget mosaic.
 */
public abstract class Tile extends ResizeComposite {

    private static Resources DEFAULT_RESOURCES;

    private EventBus commandEventBus;

    private ContainerSettings containerSettings = new ContainerSettings();

    private Style style;

    protected static class ContainerSettings {
        private SafeStylesBuilder stylesBuilder = new SafeStylesBuilder();
        private String className;

        public SafeStyles getStyles() {
            return stylesBuilder.toSafeStyles();
        }

        public void setHeight(double value, com.google.gwt.dom.client.Style.Unit unit) {
            stylesBuilder.height(value, unit);
        }

        public void setWidth(double value, com.google.gwt.dom.client.Style.Unit unit) {
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
     * @param resources tile resources
     */
    protected Tile(Widget wrappedWidget, Resources resources) {
        this.style = resources.tileStyle();
        this.style.ensureInjected();

        containerSettings.setClassName(style.tileContainer());

        initWidget(wrappedWidget);
    }

    public void setCommandEventBus(EventBus commandEventBus) {
        this.commandEventBus = commandEventBus;
        addCommandHandlers();
    }

    protected abstract void addCommandHandlers();

    /**
     * Adds the handler for a command event. Command events
     * are used to manipulate with tile's content: add,
     * remove and update it.
     *
     * @param handler the handler
     * @param type event type
     *
     * @param <H> handler type
     *
     * @return a {@link HandlerRegistration} to remove the handler
     */
    protected <H extends Command.Handler> HandlerRegistration addCommandHandler(
            final H handler, GwtEvent.Type<H> type) {
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

    protected void setContainerHeight(double value, com.google.gwt.dom.client.Style.Unit unit) {
        containerSettings.setHeight(value, unit);
    }

    protected void setContainerWidth(double value, com.google.gwt.dom.client.Style.Unit unit) {
        containerSettings.setWidth(value, unit);
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

    // TODO: 20.05.16 just fixed class name for estimator, styles only in widgets
    public interface Style extends CssResource {
        String DEFAULT_CSS = "com/autocomple/superwidget/tile/tile.css";

        String tileContainer();
    }

    public interface Resources extends ClientBundle {

        @Source(Style.DEFAULT_CSS)
        Tile.Style tileStyle();
    }

    protected static Resources getDefaultResources() {
        if (DEFAULT_RESOURCES == null) {
            DEFAULT_RESOURCES = GWT.create(Resources.class);
        }
        return DEFAULT_RESOURCES;
    }

    Style getStyle() {
        return style;
    }
}
