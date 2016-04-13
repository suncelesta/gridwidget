package com.autocomple.mosaic;

import com.autocomple.mosaic.command.Command;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.LayoutPanel;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * A tile of the widget mosaic.
 */
public abstract class Tile extends LayoutPanel {

    private EventBus commandEventBus;

    private Dimension placerHeight;
    private Dimension placerWidth;

    /**
     * Constructs a new {@code Tile}.
     *
     * @param commandEventBus the event bus used to provide command events
     */
    protected Tile(EventBus commandEventBus) {
        this.commandEventBus = commandEventBus;

        // to stretch over all the space in the placer element
        // provided by outer layout panel
        setHeight("100%");
        setWidth("100%");
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

    public void setPlacerHeight(Dimension height) {
        this.placerHeight = height;
    }

    public void setPlacerWidth(Dimension width) {
        this.placerWidth = width;
    }

    protected Mosaic.UnitMatrix getMatrix(double unitHeight,
                                          double unitWidth) {

        int heightInUnits = round(getRelativeDimension(placerHeight, getParent().getOffsetHeight()) / unitHeight);
        int widthInUnits = round(getRelativeDimension(placerWidth, getParent().getOffsetWidth()) / unitWidth);

        Mosaic.UnitMatrix result = new Mosaic.UnitMatrix(heightInUnits, widthInUnits);

        for (int i = 0; i < heightInUnits; i++) {
            for (int j = 0; j < widthInUnits; j++) {
                result.setOccupied(i, j, true);
            }
        }

        return result;
    }

    // this is more correct for tile placement than simply ceiling,
    // as we care about at least half a pixel
    private int round(double value) {
        return new BigDecimal(value).setScale(0, RoundingMode.HALF_UP).intValue();
    }

    protected Dimension getPlacerHeight() {
        return placerHeight;
    }

    protected Dimension getPlacerWidth() {
        return placerWidth;
    }

    private double getRelativeDimension(Dimension dimension,
                                        int parentDimension) {
        switch (dimension.getUnit()) {
            case PCT:
                return parentDimension * dimension.getValue() / 100;
            case PX:
                return dimension.getValue();
            default:
                throw new UnsupportedOperationException();
        }
    }
}
