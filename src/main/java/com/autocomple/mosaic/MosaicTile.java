package com.autocomple.mosaic;

import com.autocomple.mosaic.command.AddCommand;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * A mosaic tile which itself contains a mosaic.
 * The mosaic is divided into fixed number of rows and columns,
 * with height and width of cells (units) is changed on resize.
 */
public class MosaicTile extends Tile {
    private static final int DEFAULT_INTERNAL_WIDTH_IN_UNITS = 100;
    private static final int DEFAULT_INTERNAL_HEIGHT_IN_UNITS = 200;

    private int internalWidthInUnits = DEFAULT_INTERNAL_WIDTH_IN_UNITS;
    private int internalHeightInUnits = DEFAULT_INTERNAL_HEIGHT_IN_UNITS;

    double unitWidth;
    double unitHeight;

    private Mosaic mosaic;

    private List<Tile> tileList = new ArrayList<>();

    /**
     * @param commandEventBus the event bus used to provide command events
     * @param internalWidthInUnits the width of inner mosaic in units
     * @param internalHeightInUnits the height of inner mosaic in units
     */
    public MosaicTile(EventBus commandEventBus, int internalWidthInUnits, int internalHeightInUnits) {
        super(commandEventBus);

        this.internalWidthInUnits = internalWidthInUnits;
        this.internalHeightInUnits = internalHeightInUnits;
        this.mosaic = new Mosaic(internalHeightInUnits, internalWidthInUnits);

        addAddCommandHandler();
    }

    /**
     * @param commandEventBus the event bus used to provide command events
     */
    public MosaicTile(EventBus commandEventBus) {
        super(commandEventBus);

        this.mosaic = new Mosaic(internalHeightInUnits, internalWidthInUnits);

        addAddCommandHandler();
    }

    @Override
    public void onResize() {
        unitWidth = (double)getOffsetWidth() / internalWidthInUnits;
        unitHeight = (double)getOffsetHeight() / internalHeightInUnits;

        Scheduler.get().scheduleDeferred(this::rearrangeTiles);

        super.onResize();
    }

    private void rearrangeTiles() {
        mosaic.clear();

        for (Tile tile : tileList) {
            renderTile(tile);
        }
    }

    protected void addTile(Tile tile) {
        add(tile);

        tileList.add(tile);

        renderTile(tile);
    }

    private void renderTile(Tile tile) {
        Mosaic.UnitMatrix tileMatrix = tile.getMatrix(unitHeight, unitWidth);

        Mosaic.Position tilePosition = mosaic.placeTile(tileMatrix);

        if (tilePosition != null) {
            setWidgetLeftWidth(tile, tilePosition.getLeft() * unitWidth, Style.Unit.PX,
                    tile.getPlacerWidth().getValue(), tile.getPlacerWidth().getUnit());

            setWidgetTopHeight(tile, tilePosition.getTop() * unitHeight, Style.Unit.PX,
                    tile.getPlacerHeight().getValue(), tile.getPlacerHeight().getUnit());
        }
    }

    //todo: add to different indices
    protected void addAddCommandHandler() {
        addCommandHandler(command -> addTile(command.getTile()), AddCommand.TYPE);
    }

    //todo: remove command
    protected void removeUnit(Tile tile) {
        remove(tile);
    }
}
