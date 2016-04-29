package com.autocomple.mosaic;

import com.autocomple.mosaic.command.*;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.EventBus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A mosaic tile which itself contains a mosaic.
 * The mosaic is divided into fixed number of rows and columns,
 * with height and width of cells (units) is changed on resize.
 */
public class MosaicTile extends Tile {
    private static final int DEFAULT_INTERNAL_WIDTH_IN_UNITS = 100;
    private static final int DEFAULT_INTERNAL_HEIGHT_IN_UNITS = 200;

    private int internalWidthInUnits;
    private int internalHeightInUnits;

    private static final int SCROLLBAR_WIDTH = getScrollBarWidth();

    double unitWidth;
    double unitHeight;

    private Mosaic mosaic;

    private List<Tile> tileList = new ArrayList<>();
    private HashMap<Tile, Mosaic.Position> tilePositions = new HashMap<>();


    /**
     * @param commandEventBus the event bus used to provide command events
     */
    public MosaicTile(EventBus commandEventBus) {
        this(commandEventBus, getDefaultResources());
    }

    /**
     * @param commandEventBus the event bus used to provide command events
     * @param resources tile resources
     */
    public MosaicTile(EventBus commandEventBus,
                      Resources resources) {
        this(commandEventBus, resources, DEFAULT_INTERNAL_WIDTH_IN_UNITS, DEFAULT_INTERNAL_HEIGHT_IN_UNITS);
    }

    /**
     * @param commandEventBus the event bus used to provide command events
     * @param resources tile resources
     * @param internalWidthInUnits the width of inner mosaic in units
     * @param internalHeightInUnits the height of inner mosaic in units
     */
    public MosaicTile(EventBus commandEventBus,
                      Resources resources,
                      int internalWidthInUnits,
                      int internalHeightInUnits) {
        super(commandEventBus, resources);

        this.internalWidthInUnits = internalWidthInUnits;
        this.internalHeightInUnits = internalHeightInUnits;

        init();
    }

    private void init() {
        this.mosaic = new Mosaic(internalHeightInUnits, internalWidthInUnits);

        addAddCommandHandler();
        addRemoveCommandHandler();
    }

    @Override
    public void onResize() {
        unitWidth = ((double)getElement().getParentElement().getOffsetWidth() - SCROLLBAR_WIDTH) / internalWidthInUnits;
        unitHeight = ((double)getElement().getParentElement().getOffsetHeight() - SCROLLBAR_WIDTH) / internalHeightInUnits;

        Scheduler.get().scheduleDeferred(this::rearrangeTiles);

        super.onResize();
    }

    private void rearrangeTiles() {
        mosaic.clear();

        for (Tile tile : tileList) {
            renderTile(tile);
        }
    }

    protected void addTile(Tile tile, int index) {
        getLayoutPanel().insert(tile, index);

        tileList.add(index, tile);

        clearTilesFrom(index + 1);

        for (int i = index; i < getLayoutPanel().getWidgetCount(); i++) {
            final Tile tileToRender = tileList.get(i);
            Scheduler.get().scheduleFinally(() -> {renderTile(tileToRender);});
        }
    }

    private void clearTilesFrom(int index) {
        for (int i = index; i < getLayoutPanel().getWidgetCount(); i++) {
            Tile tile = tileList.get(i);
            if (tilePositions.containsKey(tile)) {
                mosaic.removeTile(tile.getMatrix(getMatrixHeight(tile), getMatrixWidth(tile)), tilePositions.get(tile));
            }
        }
    }

    private void renderTile(Tile tile) {
        Element tileContainer = getLayoutPanel().getWidgetContainerElement(tile);
        Element tileSizeEstimator = tile.getSizeEstimatorElement();

        tileContainer.setClassName(tileSizeEstimator.getClassName());
        tileContainer.getStyle().setProperty("width", tileSizeEstimator.getStyle().getWidth());
        tileContainer.getStyle().setProperty("height", tileSizeEstimator.getStyle().getHeight());

        Mosaic.UnitMatrix tileMatrix = tile.getMatrix(getMatrixHeight(tile), getMatrixWidth(tile));

        Mosaic.Position tilePosition = mosaic.placeTile(tileMatrix);

        if (tilePosition != null) {
            tilePositions.put(tile, tilePosition);

            setChildLeftWidth(tile, tileMatrix.getWidth(), tilePosition.getLeft());

            setChildTopHeight(tile, tileMatrix.getHeight(), tilePosition.getTop());

        } else {
            tilePositions.remove(tile);
        }
    }

    private int getMatrixHeight(Tile tile) {
        return round(getLayoutPanel().getWidgetContainerElement(tile).getOffsetHeight() / unitHeight);
    }

    private int getMatrixWidth(Tile tile) {

        return round(getLayoutPanel().getWidgetContainerElement(tile).getOffsetWidth() / unitWidth);
    }

    // this is more correct for tile placement than simply ceiling,
    // as we care about at least half a pixel
    private int round(double value) {
        return new BigDecimal(value).setScale(0, RoundingMode.HALF_UP).intValue();
    }

    private void setChildLeftWidth(Tile child, int width, int left) {
        getLayoutPanel().setWidgetLeftWidth(child,
                left * unitWidth, com.google.gwt.dom.client.Style.Unit.PX,
                width * unitWidth, com.google.gwt.dom.client.Style.Unit.PX);
    }

    private void setChildTopHeight(Tile child, int height, int top) {
        getLayoutPanel().setWidgetTopHeight(child,
                top * unitHeight, com.google.gwt.dom.client.Style.Unit.PX,
                height * unitHeight, com.google.gwt.dom.client.Style.Unit.PX);
    }

    protected void addAddCommandHandler() {
        addCommandHandler(command -> {
            if (command instanceof AppendCommand) {
                addTile(command.getTile(), getLayoutPanel().getWidgetCount());
            } else if (command instanceof PrependCommand) {
                addTile(command.getTile(), 0);
            } else if (command instanceof AddToIndexCommand) {
                addTile(command.getTile(), ((AddToIndexCommand)command).getIndex());
            }
        }, AddCommand.TYPE);
    }

    protected void addRemoveCommandHandler() {
        addCommandHandler(command -> removeTile(command.getTile()), RemoveCommand.TYPE);
    }

    protected void removeTile(Tile tile) {
        getLayoutPanel().remove(tile);

        tileList.remove(tile);

        rearrangeTiles();
    }

    private static native int getScrollBarWidth() /*-{
        var scr = null;
        var inn = null;
        var wNoScroll = 0;
        var wScroll = 0;

        // Outer scrolling div
        scr = $wnd.document.createElement('div');
        scr.style.position = 'absolute';
        scr.style.top = '-1000px';
        scr.style.left = '-1000px';
        scr.style.width = '100px';
        scr.style.height = '50px';
        // Start with no scrollbar
        scr.style.overflow = 'hidden';

        // Inner content div
        inn = $wnd.document.createElement('div');
        inn.style.width = '100%';
        inn.style.height = '200px';

        // Put the inner div in the scrolling div
        scr.appendChild(inn);
        // Append the scrolling div to the doc

        $wnd.document.body.appendChild(scr);

        // Width of the inner div sans scrollbar
        wNoScroll = inn.offsetWidth;
        // Add the scrollbar
        scr.style.overflow = 'auto';
        // Width of the inner div width scrollbar
        wScroll = inn.offsetWidth;

        // Remove the scrolling div from the doc
        $wnd.document.body.removeChild(
            $wnd.document.body.lastChild);

        // Pixel width of the scroller
        return (wNoScroll - wScroll);
    }-*/;
}
