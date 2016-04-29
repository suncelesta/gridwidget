package com.autocomple.mosaic;

import com.autocomple.mosaic.command.Command;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ResizeComposite;

/**
 * A tile of the widget mosaic.
 */
public abstract class Tile extends ResizeComposite {

    private static Resources DEFAULT_RESOURCES;

    private EventBus commandEventBus;
    private LayoutPanel layoutPanel;
    private Element sizeEstimatorElement;
    private Style style;

    /**
     * Constructs a new {@code Tile}.
     *
     * @param commandEventBus the event bus used to provide command events
     * @param resources tile resources
     */
    protected Tile(EventBus commandEventBus,
                   Resources resources) {
        this.commandEventBus = commandEventBus;

        this.style = resources.tileStyle();
        this.style.ensureInjected();

        this.layoutPanel = new LayoutPanel();
        this.sizeEstimatorElement = Document.get().createDivElement();
        this.sizeEstimatorElement.addClassName(style.tileContainer());

        initWidget(layoutPanel);
    }
    /**
     * Adds the handler for a command event. Command events
     * are used to manipulate with unit's content: add,
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

    public void setHeight(double value, com.google.gwt.dom.client.Style.Unit unit) {
        sizeEstimatorElement.getStyle().setHeight(value, unit);
    }

    public void setWidth(double value, com.google.gwt.dom.client.Style.Unit unit) {
        sizeEstimatorElement.getStyle().setWidth(value, unit);
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

    protected LayoutPanel getLayoutPanel() {
        return layoutPanel;
    }

    protected Element getSizeEstimatorElement() {
        return sizeEstimatorElement;
    }

    public interface Style extends CssResource {
        String DEFAULT_CSS = "com/autocomple/mosaic/tile.css";

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
